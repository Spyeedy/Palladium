package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class InvisibilityAbility extends Ability {

    public static final MapCodec<InvisibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, InvisibilityAbility::new));

    public InvisibilityAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<InvisibilityAbility> getSerializer() {
        return AbilitySerializers.INVISIBILITY.get();
    }

    public static class Serializer extends AbilitySerializer<InvisibilityAbility> {

        @Override
        public MapCodec<InvisibilityAbility> codec() {
            return CODEC;
        }
    }
}
