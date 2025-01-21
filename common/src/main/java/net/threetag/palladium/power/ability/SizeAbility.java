package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class SizeAbility extends Ability {

    public static final MapCodec<SizeAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(ab -> ab.size),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, SizeAbility::new));

    public final float size;

    public SizeAbility(float size, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.size = size;
    }

    @Override
    public AbilitySerializer<SizeAbility> getSerializer() {
        return AbilitySerializers.SIZE.get();
    }

    public static class Serializer extends AbilitySerializer<SizeAbility> {

        @Override
        public MapCodec<SizeAbility> codec() {
            return CODEC;
        }
    }
}
