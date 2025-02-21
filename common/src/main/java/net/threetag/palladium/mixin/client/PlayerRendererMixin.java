package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.entity.PlayerModelCacheExtension;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Shadow
    public abstract EntityRenderState createRenderState();

    @Shadow
    public abstract void extractRenderState(AbstractClientPlayer arg, PlayerRenderState arg2, float f);

    @Inject(at = @At("RETURN"), method = "renderHand")
    private void renderHand(
            PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, ModelPart armPart, boolean isSleeveVisible, CallbackInfo ci
    ) {
        var player = Minecraft.getInstance().player;
        var l = PalladiumEntityData.get(player, PalladiumEntityDataTypes.RENDER_LAYERS.get());
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;

        if (l instanceof ClientEntityRenderLayers layers) {
            boolean rightArm = armPart == playerRenderer.getModel().rightArm;
            var arm = rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT;

            layers.getLayerStates().forEach((layer, state) -> {
                if (layer.shouldRender(state, PerspectiveAwareConditions.Perspective.FIRST_PERSON)) {
                    layer.renderArm(state.getContext(), poseStack, bufferSource, arm, armPart, playerRenderer, packedLight);
                }
            });
        }

        if (player instanceof PlayerModelCacheExtension ext) {
            ext.palladium$animateInFirstPerson(player, playerRenderer);
        }
    }

}
