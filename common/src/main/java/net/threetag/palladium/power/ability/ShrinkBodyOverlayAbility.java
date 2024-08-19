package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class ShrinkBodyOverlayAbility extends Ability {

    public static final MapCodec<ShrinkBodyOverlayAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, ShrinkBodyOverlayAbility::new));

    public ShrinkBodyOverlayAbility(AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<ShrinkBodyOverlayAbility> getSerializer() {
        return AbilitySerializers.SHRINK_BODY_OVERLAY.get();
    }

    public static class Serializer extends AbilitySerializer<ShrinkBodyOverlayAbility> {

        @Override
        public MapCodec<ShrinkBodyOverlayAbility> codec() {
            return CODEC;
        }
    }
}
