package net.threetag.palladium.client.model.animation;

import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.Easing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladiumcore.event.ViewportEvents;

import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
public class FlightAnimation extends PalladiumAnimation implements ViewportEvents.ComputeCameraAngles {

    public FlightAnimation(int priority) {
        super(priority);

        ViewportEvents.COMPUTE_CAMERA_ANGLES.register(this);
    }

    @Override
    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
        boolean active = !firstPersonContext.firstPerson();

        if (active && player instanceof PalladiumPlayerExtension extension) {
            float anim = extension.palladium_getFlightAnimation(partialTicks);

            if (anim <= 1F) {
                return;
            }

            anim = (anim - 1F) / 2F;

            var vec1 = to2D(extension.palladium_getFlightVector(partialTicks));
            var vec2 = to2D(extension.palladium_getLookAngle(partialTicks));
            var tilt = Mth.clamp(angleBetweenVector(vec1, vec2), -0.5F, 0.5F) * 90F * extension.palladium_getHorizontalSpeed(partialTicks);

            builder.get(PlayerModelPart.BODY)
                    .setZRotDegrees((float) -tilt)
                    .setXRotDegrees(-90.0F - player.getXRot())
                    .setY(0.2F * 16F)
                    .setY2(-1.8F / 2F * 16F)
                    .animate(Ease.INBACK, anim);

            builder.get(PlayerModelPart.HEAD)
                    .setXRotDegrees(-90.0F)
                    .setYRotDegrees((float) tilt)
                    .animate(Ease.INBACK, anim);

            builder.get(PlayerModelPart.CHEST)
                    .setXRotDegrees(-5F)
                    .setYRotDegrees(0F)
                    .setZRotDegrees(0F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.RIGHT_ARM)
                    .setXRotDegrees(0F)
                    .setYRotDegrees(17.5F)
                    .setZRotDegrees(5F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.LEFT_ARM)
                    .setXRotDegrees(0F)
                    .setYRotDegrees(-17.5F)
                    .setZRotDegrees(-5F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.RIGHT_LEG)
                    .setZ(-1F)
                    .setXRotDegrees(5F)
                    .setYRotDegrees(2.5F)
                    .setZRotDegrees(2.5F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.LEFT_LEG)
                    .setZ(-1F)
                    .setXRotDegrees(5F)
                    .setYRotDegrees(-2.5F)
                    .setZRotDegrees(-2.5F)
                    .animate(Ease.INOUTCUBIC, anim);
        }
    }

    public static Vec2 to2D(Vec3 vec) {
        return new Vec2((float) vec.x, (float) vec.z);
    }

    public static double angleBetweenVector(Vec2 vec1, Vec2 vec2) {
        return Math.atan2(vec1.x * vec2.y - vec1.y * vec2.x, vec1.x * vec2.x + vec1.y * vec2.y);
    }

    @Override
    public void computeCameraAngles(GameRenderer gameRenderer, Camera camera, double partialTick, AtomicReference<Float> yaw, AtomicReference<Float> pitch, AtomicReference<Float> roll) {
        if (Minecraft.getInstance().player instanceof PalladiumPlayerExtension extension) {
            float anim = extension.palladium_getFlightAnimation((float) partialTick);

            if (anim <= 1F) {
                return;
            }

            anim = (anim - 1F) / 2F;

            var vec1 = FlightAnimation.to2D(extension.palladium_getFlightVector((float) partialTick));
            var vec2 = FlightAnimation.to2D(extension.palladium_getLookAngle((float) partialTick));
            var tilt = Mth.clamp(FlightAnimation.angleBetweenVector(vec1, vec2), -0.5F, 0.5F) * 30F * extension.palladium_getHorizontalSpeed((float) partialTick);

            roll.set((float) tilt * Easing.easingFromEnum(Ease.INOUTCUBIC, anim));
        }
    }
}
