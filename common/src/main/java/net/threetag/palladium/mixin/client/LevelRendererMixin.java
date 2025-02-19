package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.WatcherRenderer;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityClientEventHandler;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V", shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    private void skipSkyRenderingForPhasingBlindness(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean bl, Runnable skyFogSetup, CallbackInfo ci) {
        if (camera.getEntity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, Abilities.INTANGIBILITY.get())) {
                if (AbilityClientEventHandler.getInWallBlockState(living) != null) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void renderWatcher(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean bl, Runnable skyFogSetup, CallbackInfo ci) {
        WatcherRenderer.INSTANCE.render(poseStack, WatcherRenderer.INSTANCE.getVisibility(partialTick));
    }
}
