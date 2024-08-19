package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class SlowfallAbility extends Ability {

    public static final MapCodec<SlowfallAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, SlowfallAbility::new));

    public SlowfallAbility(AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<SlowfallAbility> getSerializer() {
        return AbilitySerializers.SLOWFALL.get();
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, PowerHolder holder, boolean enabled) {
        if (enabled && !entity.onGround() && entity.getDeltaMovement().y() < 0D) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y * 0.6D, entity.getDeltaMovement().z);
            entity.fallDistance = 0F;
        }
    }

    public static class Serializer extends AbilitySerializer<SlowfallAbility> {

        @Override
        public MapCodec<SlowfallAbility> codec() {
            return CODEC;
        }
    }
}
