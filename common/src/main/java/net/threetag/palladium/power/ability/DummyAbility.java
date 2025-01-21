package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class DummyAbility extends Ability {

    public static final MapCodec<DummyAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, DummyAbility::new));

    public DummyAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<?> getSerializer() {
        return AbilitySerializers.DUMMY.get();
    }

    public static class Serializer extends AbilitySerializer<DummyAbility> {

        @Override
        public MapCodec<DummyAbility> codec() {
            return CODEC;
        }
    }
}
