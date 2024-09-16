package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.client.dynamictexture.DynamicTextureManager;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

public class SkinOverlayPackRenderLayer extends AbstractPackRenderLayer {

    private final SkinTypedValue<DynamicTexture> texture;
    private final RenderTypeFunction renderType;

    public SkinOverlayPackRenderLayer(SkinTypedValue<DynamicTexture> texture, RenderTypeFunction renderType) {
        this.texture = texture;
        this.renderType = renderType;
    }

    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var entity = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions)) {
            VertexConsumer vertexConsumer = this.renderType.createVertexConsumer(bufferSource, this.texture.get(entity).getTexture(context), context.getItem().hasFoil());
            parentModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
    }

    @Override
    public void renderArm(DataContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        var player = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(player, this.conditions, this.firstPersonConditions)) {
            PlayerModel<AbstractClientPlayer> entityModel = playerRenderer.getModel();
            VertexConsumer vertexConsumer = this.renderType.createVertexConsumer(bufferSource, this.texture.get(player).getTexture(context), context.getItem().hasFoil());

            entityModel.attackTime = 0.0F;
            entityModel.crouching = false;
            entityModel.swimAmount = 0.0F;

            if (arm == HumanoidArm.RIGHT) {
                entityModel.rightArm.xRot = 0.0F;
                entityModel.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            } else {
                entityModel.leftArm.xRot = 0.0F;
                entityModel.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    public static SkinOverlayPackRenderLayer parse(JsonObject json) {
        var renderType = PackRenderLayerManager.getRenderType(GsonUtil.getAsResourceLocation(json, "render_type", ResourceLocation.withDefaultNamespace("solid")));

        if (renderType == null) {
            throw new JsonParseException("Unknown render type '" + GsonUtil.getAsResourceLocation(json, "render_type", ResourceLocation.withDefaultNamespace("solid")) + "'");
        }

        return new SkinOverlayPackRenderLayer(SkinTypedValue.fromJSON(json.get("texture"), DynamicTextureManager::fromJson), renderType);
    }
}
