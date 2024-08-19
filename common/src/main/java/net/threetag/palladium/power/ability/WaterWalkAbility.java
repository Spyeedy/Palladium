package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class WaterWalkAbility extends Ability {

    public static final MapCodec<WaterWalkAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, WaterWalkAbility::new));

    public WaterWalkAbility(AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<WaterWalkAbility> getSerializer() {
        return AbilitySerializers.WATER_WALK.get();
    }

    public static class Serializer extends AbilitySerializer<WaterWalkAbility> {

        @Override
        public MapCodec<WaterWalkAbility> codec() {
            return CODEC;
        }
    }
}
