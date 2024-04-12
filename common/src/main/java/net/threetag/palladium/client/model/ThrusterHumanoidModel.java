package net.threetag.palladium.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ThrusterHumanoidModel<T extends LivingEntity> extends ImprovedHumanoidModel<T> implements ExtraAnimatedModel<T> {

    public final ModelPart rightArmThruster;
    public final ModelPart leftArmThruster;
    public final ModelPart rightLegThruster;
    public final ModelPart leftLegThruster;
    private final List<ModelPart> thrusters = new ArrayList<>();

    public ThrusterHumanoidModel(ModelPart root) {
        super(root);
        this.rightArmThruster = this.rightArm.getChild("right_arm_thruster");
        this.leftArmThruster = this.leftArm.getChild("left_arm_thruster");
        this.rightLegThruster = this.rightLeg.getChild("right_leg_thruster");
        this.leftLegThruster = this.leftLeg.getChild("left_leg_thruster");
        this.thrusters.add(this.rightArmThruster);
        this.thrusters.add(this.leftArmThruster);
        this.thrusters.add(this.rightLegThruster);
        this.thrusters.add(this.leftLegThruster);
    }

    public ThrusterHumanoidModel(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
        super(root, renderType);
        this.rightArmThruster = this.rightArm.getChild("right_arm_thruster");
        this.leftArmThruster = this.leftArm.getChild("left_arm_thruster");
        this.rightLegThruster = this.rightLeg.getChild("right_leg_thruster");
        this.leftLegThruster = this.leftLeg.getChild("left_leg_thruster");
        this.thrusters.add(this.rightArmThruster);
        this.thrusters.add(this.leftArmThruster);
        this.thrusters.add(this.rightLegThruster);
        this.thrusters.add(this.leftLegThruster);
    }

    @Override
    public void extraAnimations(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks) {
        float f = Mth.sin((entity.tickCount + partialTicks) * 3F) / 10F;
        float v = 1F;

        if (entity instanceof PalladiumPlayerExtension extension) {
            v = Math.max(extension.palladium$getFlightHandler().getFlightAnimation(partialTicks) - 1F, 0F) / 2F;
        }

        for (ModelPart thruster : this.thrusters) {
            thruster.resetPose();
            thruster.offsetScale(new Vector3f(f, f + v, f));
        }
    }
}
