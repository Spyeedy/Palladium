package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class FireAspectAbility extends Ability {

    public static final MapCodec<FireAspectAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(ab -> ab.time),
                    Codec.BOOL.optionalFieldOf("should_stack_time", false).forGetter(ab -> ab.shouldStackTime),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_time", 60).forGetter(ab -> ab.maxTime),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, FireAspectAbility::new));

    public final int time;
    public final boolean shouldStackTime;
    public final int maxTime;

    public FireAspectAbility(int time, boolean shouldStackTime, int maxTime, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.time = time;
        this.shouldStackTime = shouldStackTime;
        this.maxTime = maxTime;
    }

    @Override
    public AbilitySerializer<FireAspectAbility> getSerializer() {
        return AbilitySerializers.FIRE_ASPECT.get();
    }

    public static class Serializer extends AbilitySerializer<FireAspectAbility> {

        @Override
        public MapCodec<FireAspectAbility> codec() {
            return CODEC;
        }
    }

}
