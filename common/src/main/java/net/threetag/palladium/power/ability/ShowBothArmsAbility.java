package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class ShowBothArmsAbility extends Ability{

    public static final MapCodec<ShowBothArmsAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, ShowBothArmsAbility::new));

    public ShowBothArmsAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<ShowBothArmsAbility> getSerializer() {
        return AbilitySerializers.SHOW_BOTH_ARMS.get();
    }

    public static class Serializer extends AbilitySerializer<ShowBothArmsAbility> {

        @Override
        public MapCodec<ShowBothArmsAbility> codec() {
            return CODEC;
        }
    }
}
