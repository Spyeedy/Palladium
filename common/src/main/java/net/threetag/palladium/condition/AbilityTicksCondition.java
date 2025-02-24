package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.util.CodecExtras;

public record AbilityTicksCondition(AbilityReference ability, String propertyKey, int min,
                                    int max) implements Condition {

    public static final MapCodec<AbilityTicksCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityTicksCondition::ability),
                    Codec.STRING.fieldOf("property").forGetter(AbilityTicksCondition::propertyKey),
                    CodecExtras.TIME.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(AbilityTicksCondition::min),
                    CodecExtras.TIME.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(AbilityTicksCondition::max)
            ).apply(instance, AbilityTicksCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityTicksCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityTicksCondition::ability,
            ByteBufCodecs.STRING_UTF8, AbilityTicksCondition::propertyKey,
            ByteBufCodecs.VAR_INT, AbilityTicksCondition::min,
            ByteBufCodecs.VAR_INT, AbilityTicksCondition::max,
            AbilityTicksCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityInstance dependency = this.ability.getInstance(entity, holder);

        if (dependency == null) {
            return false;
        } else {
            return this.min <= dependency.getEnabledTicks() && dependency.getEnabledTicks() <= this.max;
        }
    }

    @Override
    public ConditionSerializer<AbilityTicksCondition> getSerializer() {
        return ConditionSerializers.ABILITY_TICKS.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityTicksCondition> {

        @Override
        public MapCodec<AbilityTicksCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityTicksCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability has been enabled for a certain amount of ticks.";
        }
    }
}
