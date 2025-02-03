package net.threetag.palladium.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AimAbility;
import net.threetag.palladium.util.RenderUtil;

public class AimAnimation {

    public static void setupAnim(HumanoidModel<?> model, EntityRenderState state) {
        if (RenderUtil.getCurrentlyRenderedEntity() instanceof LivingEntity living) {
            float[] progress = AimAbility.getTimer(living, RenderUtil.getCurrentlyRenderedPartialTick());
            float left = progress[0];
            float right = progress[1];

            if (left > 0F) {
                model.leftArm.xRot = animateTo(left, model.leftArm.xRot, (float) (model.head.xRot - Math.PI / 2F));
                model.leftArm.yRot = animateTo(left, model.leftArm.yRot, model.head.yRot);
                model.leftArm.zRot = animateTo(left, model.leftArm.zRot, model.head.zRot);
            }

            if (right > 0F) {
                model.rightArm.xRot = animateTo(right, model.rightArm.xRot, (float) (model.head.xRot - Math.PI / 2F));
                model.rightArm.yRot = animateTo(right, model.rightArm.yRot, model.head.yRot);
                model.rightArm.zRot = animateTo(right, model.rightArm.zRot, model.head.zRot);
            }
        }
    }

    private static float animateTo(float progress, float from, float to) {
        return from + (to - from) * progress;
    }

}
