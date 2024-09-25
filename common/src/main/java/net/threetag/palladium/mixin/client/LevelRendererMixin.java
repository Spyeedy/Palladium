package net.threetag.palladium.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityClientEventHandler;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V", shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    private void skipSkyRenderingForPhasingBlindness(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        if (camera.getEntity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (AbilityClientEventHandler.getInWallBlockState(living) != null) {
                    ci.cancel();
                }
            }
        }
    }
}
