package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.model.ModelLayerLocationCodec;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.entity.SkinTypedValue;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DefaultPackRenderLayer extends PackRenderLayer<PackRenderLayer.State> {

    public static final MapCodec<DefaultPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SkinTypedValue.codec(ModelLayerLocationCodec.CODEC).optionalFieldOf("model_layer").forGetter(l -> Optional.ofNullable(l.modelLayers)),
            SkinTypedValue.codec(ResourceLocation.CODEC).fieldOf("texture").forGetter(l -> l.textures),
            RenderTypeFunctions.CODEC.optionalFieldOf("render_type", RenderTypeFunctions.SOLID).forGetter(l -> l.renderType),
            ExtraCodecs.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(l -> l.lightEmission),
            PackRenderLayerAnimations.CODEC.optionalFieldOf("animations", PackRenderLayerAnimations.EMPTY).forGetter(l -> l.animations),
            conditionsCodec()
    ).apply(instance, (modelLayers, textures, renderType, lightEmission, animations, conditions) -> {
        return new DefaultPackRenderLayer(modelLayers.orElse(null), textures, renderType, lightEmission, animations, conditions);
    }));

    @Nullable
    private final SkinTypedValue<ModelLayerLocationCodec> modelLayers;
    private SkinTypedValue<Model.Simple> model;
    private final SkinTypedValue<ResourceLocation> textures;
    private final RenderTypeFunction renderType;
    private final int lightEmission;
    private final PackRenderLayerAnimations animations;

    public DefaultPackRenderLayer(@Nullable SkinTypedValue<ModelLayerLocationCodec> modelLayers, SkinTypedValue<ResourceLocation> textures, RenderTypeFunction renderType, int lightEmission, PackRenderLayerAnimations animations, PerspectiveAwareConditions conditions) {
        super(conditions);

        this.modelLayers = modelLayers;
        this.textures = textures;
        this.renderType = renderType;
        this.lightEmission = lightEmission;
        this.animations = animations;
    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return PackRenderLayerSerializers.DEFAULT;
    }

    private void buildModels() {
        if (this.modelLayers != null) {
            var entityModels = Minecraft.getInstance().getEntityModels();
            this.model = new SkinTypedValue<>(
                    new Model.Simple(entityModels.bakeLayer(this.modelLayers.getWide().get()), RenderType::entityTranslucent),
                    new Model.Simple(entityModels.bakeLayer(this.modelLayers.getSlim().get()), RenderType::entityTranslucent)
            );
        }
    }

    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState state, State layerState, int packedLight, float partialTick, float xRot, float yRot) {
        var entity = context.getEntity();
        Model model = parentModel;

        if (this.modelLayers != null) {
            if (this.model == null) {
                this.buildModels();
            }

            model = this.model.get(entity);
            model.allParts().forEach(ModelPart::resetPose);
            mimicModelParts(parentModel.root(), model.root());
        }

        this.animations.animate(model, context);

        model.renderToBuffer(
                poseStack,
                this.renderType.createVertexConsumer(bufferSource, this.textures.get(entity), context.getItem().hasFoil()),
                LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                OverlayTexture.NO_OVERLAY
        );
    }

    @Override
    public void renderArm(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, HumanoidArm arm, ModelPart armPart, PlayerRenderer playerRenderer, State layerState, int packedLight) {
        var entity = context.getEntity();
        Model model = playerRenderer.getModel();

        if (this.modelLayers != null) {
            if (this.model == null) {
                this.buildModels();
            }

            model = this.model.get(entity);
            model.allParts().forEach(ModelPart::resetPose);
            var partName = arm == HumanoidArm.RIGHT ? "rightArm" : "leftArm";

            if (model.root().hasChild(partName)) {
                var foundPart = model.root().getChild(partName);
                foundPart.copyFrom(armPart);
                armPart = foundPart;
            }
        }

        this.animations.animate(model, context);

        armPart.render(
                poseStack,
                this.renderType.createVertexConsumer(bufferSource, this.textures.get(entity), context.getItem().hasFoil()),
                LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                OverlayTexture.NO_OVERLAY
        );
    }

    private static void mimicModelParts(ModelPart source, ModelPart target) {
        target.copyFrom(source);

        source.children.forEach((name, modelPart) -> {
            if (target.hasChild(name)) {
                mimicModelParts(modelPart, target.getChild(name));
            }
        });
    }

    public static class Serializer extends PackRenderLayerSerializer<DefaultPackRenderLayer> {

        @Override
        public MapCodec<DefaultPackRenderLayer> codec() {
            return CODEC;
        }

    }

}
