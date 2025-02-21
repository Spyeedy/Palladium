package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(at = @At("RETURN"), method = "renderHand")
    private void renderHand(
            PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, ModelPart armPart, boolean isSleeveVisible, CallbackInfo ci
    ) {
        var l = PalladiumEntityData.get(Minecraft.getInstance().player, PalladiumEntityDataTypes.RENDER_LAYERS.get());

        if (l instanceof ClientEntityRenderLayers layers) {
            PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
            boolean rightArm = armPart == playerRenderer.getModel().rightArm;
            var arm = rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT;

            layers.getLayerStates().forEach((layer, state) -> {
                if (layer.shouldRender(state, PerspectiveAwareConditions.Perspective.FIRST_PERSON)) {
                    layer.renderArm(state.getContext(), poseStack, bufferSource, arm, armPart, playerRenderer, packedLight);
                }
            });
        }
    }

}
