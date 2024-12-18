package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public class IsInWaterRainOrBubbleCondition implements Condition {

    public static final IsInWaterRainOrBubbleCondition INSTANCE = new IsInWaterRainOrBubbleCondition();

    public static final MapCodec<IsInWaterRainOrBubbleCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsInWaterRainOrBubbleCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWaterRainOrBubble();
    }

    @Override
    public ConditionSerializer<IsInWaterRainOrBubbleCondition> getSerializer() {
        return ConditionSerializers.IS_IN_WATER_RAIN_OR_BUBBLE.get();
    }

    public static class Serializer extends ConditionSerializer<IsInWaterRainOrBubbleCondition> {

        @Override
        public MapCodec<IsInWaterRainOrBubbleCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsInWaterRainOrBubbleCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in water, rain or a bubble column.";
        }
    }
}
