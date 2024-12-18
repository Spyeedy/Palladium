package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.entity.ArmSetting;

import java.util.List;

public class AimAbility extends Ability {

    public static final MapCodec<AimAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ArmSetting.CODEC.optionalFieldOf("arm", ArmSetting.MAIN_ARM).forGetter(ab -> ab.arm),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, AimAbility::new));

    public final ArmSetting arm;

    public AimAbility(ArmSetting arm, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.arm = arm;
    }

    public static float getTimer(LivingEntity entity, float partialTicks, boolean right) {
        float f = 0;

        for (AbilityInstance<AimAbility> instance : AbilityUtil.getInstances(entity, AbilitySerializers.AIM.get())) {
            var armType = instance.getAbility().arm;
            var timer = instance.getAnimationTimer();
            var progress = timer != null ? timer.progress(partialTicks) : 1F;

            if (!armType.isNone()) {
                if (armType.isRight(entity) && right) {
                    f = Math.max(f, progress);
                } else if (armType.isLeft(entity) && !right) {
                    f = Math.max(f, progress);
                }
            }
        }

        return f;
    }

    @Override
    public AbilitySerializer<AimAbility> getSerializer() {
        return AbilitySerializers.AIM.get();
    }

    public static class Serializer extends AbilitySerializer<AimAbility> {

        @Override
        public MapCodec<AimAbility> codec() {
            return CODEC;
        }
    }
}
