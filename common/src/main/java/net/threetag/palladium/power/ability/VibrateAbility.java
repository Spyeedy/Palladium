package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class VibrateAbility extends Ability {

    public static final MapCodec<VibrateAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, VibrateAbility::new));

    public VibrateAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<VibrateAbility> getSerializer() {
        return AbilitySerializers.VIBRATE.get();
    }

    public static class Serializer extends AbilitySerializer<VibrateAbility> {

        @Override
        public MapCodec<VibrateAbility> codec() {
            return CODEC;
        }
    }
}
