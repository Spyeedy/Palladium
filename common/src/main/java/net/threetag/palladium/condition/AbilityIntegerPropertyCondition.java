package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyType;

public record AbilityIntegerPropertyCondition(AbilityReference ability, String propertyKey, int min,
                                              int max) implements Condition {

    public static final MapCodec<AbilityIntegerPropertyCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityIntegerPropertyCondition::ability),
                    Codec.STRING.fieldOf("property").forGetter(AbilityIntegerPropertyCondition::propertyKey),
                    Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(AbilityIntegerPropertyCondition::min),
                    Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(AbilityIntegerPropertyCondition::max)
            ).apply(instance, AbilityIntegerPropertyCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityIntegerPropertyCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityIntegerPropertyCondition::ability,
            ByteBufCodecs.STRING_UTF8, AbilityIntegerPropertyCondition::propertyKey,
            ByteBufCodecs.VAR_INT, AbilityIntegerPropertyCondition::min,
            ByteBufCodecs.VAR_INT, AbilityIntegerPropertyCondition::max,
            AbilityIntegerPropertyCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityInstance dependency = this.ability.getInstance(entity, holder);

        if (dependency == null) {
            return false;
        }

        PalladiumProperty<?> property = dependency.getEitherPropertyByKey(this.propertyKey);

        if (property.getType() == PalladiumPropertyType.INTEGER) {
            int value = (int) dependency.getProperty(property);
            return value >= this.min && value <= this.max;
        }

        return false;
    }

    @Override
    public ConditionSerializer<AbilityIntegerPropertyCondition> getSerializer() {
        return ConditionSerializers.ABILITY_INTEGER_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityIntegerPropertyCondition> {

        @Override
        public MapCodec<AbilityIntegerPropertyCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityIntegerPropertyCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the given ability has a certain integer property value.";
        }
    }
}
