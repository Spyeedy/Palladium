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

public record FloatPropertyCondition(String propertyKey, float min, float max) implements Condition {

    public static final MapCodec<FloatPropertyCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("property").forGetter(FloatPropertyCondition::propertyKey),
                    Codec.FLOAT.optionalFieldOf("min", Float.MIN_VALUE).forGetter(FloatPropertyCondition::min),
                    Codec.FLOAT.optionalFieldOf("max", Float.MAX_VALUE).forGetter(FloatPropertyCondition::max)
            ).apply(instance, FloatPropertyCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FloatPropertyCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, FloatPropertyCondition::propertyKey,
            ByteBufCodecs.FLOAT, FloatPropertyCondition::min,
            ByteBufCodecs.FLOAT, FloatPropertyCondition::max,
            FloatPropertyCondition::new
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
            if (property.getType() == PalladiumPropertyType.FLOAT) {
                float value = (float) handler.get(property);
                result.set(value >= this.min && value <= this.max);
            }
        });

        return result.get();
    }

    @Override
    public ConditionSerializer<FloatPropertyCondition> getSerializer() {
        return ConditionSerializers.FLOAT_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer<FloatPropertyCondition> {

        @Override
        public MapCodec<FloatPropertyCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FloatPropertyCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a float property with a value between the given minimum and maximum.";
        }
    }
}
