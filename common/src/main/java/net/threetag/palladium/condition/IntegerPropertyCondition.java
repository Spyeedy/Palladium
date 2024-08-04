package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyType;

import java.util.concurrent.atomic.AtomicBoolean;

public record IntegerPropertyCondition(String propertyKey, int min, int max) implements Condition {

    public static final MapCodec<IntegerPropertyCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("property").forGetter(IntegerPropertyCondition::propertyKey),
                    Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(IntegerPropertyCondition::min),
                    Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(IntegerPropertyCondition::max)
            ).apply(instance, IntegerPropertyCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, IntegerPropertyCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, IntegerPropertyCondition::propertyKey,
            ByteBufCodecs.VAR_INT, IntegerPropertyCondition::min,
            ByteBufCodecs.VAR_INT, IntegerPropertyCondition::max,
            IntegerPropertyCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        AtomicBoolean result = new AtomicBoolean(false);

        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);
            if (property.getType() == PalladiumPropertyType.INTEGER) {
                int value = (int) handler.get(property);
                result.set(value >= this.min && value <= this.max);
            }
        });

        return result.get();
    }

    @Override
    public ConditionSerializer<IntegerPropertyCondition> getSerializer() {
        return ConditionSerializers.INTEGER_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer<IntegerPropertyCondition> {

        @Override
        public MapCodec<IntegerPropertyCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IntegerPropertyCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a certain amount of a certain integer property.";
        }
    }
}
