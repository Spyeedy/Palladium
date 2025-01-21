package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public class IsInWaterOrBubbleCondition implements Condition {

    public static final IsInWaterOrBubbleCondition INSTANCE = new IsInWaterOrBubbleCondition();

    public static final MapCodec<IsInWaterOrBubbleCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsInWaterOrBubbleCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWaterOrBubble();
    }

    @Override
    public ConditionSerializer<IsInWaterOrBubbleCondition> getSerializer() {
        return ConditionSerializers.IS_IN_WATER_OR_BUBBLE.get();
    }

    public static class Serializer extends ConditionSerializer<IsInWaterOrBubbleCondition> {

        @Override
        public MapCodec<IsInWaterOrBubbleCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsInWaterOrBubbleCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in water or a bubble column.";
        }
    }
}
