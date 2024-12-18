package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public class InWaterCondition implements Condition {

    public static final InWaterCondition INSTANCE = new InWaterCondition();

    public static final MapCodec<InWaterCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, InWaterCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWater();
    }

    @Override
    public ConditionSerializer<InWaterCondition> getSerializer() {
        return ConditionSerializers.IN_WATER.get();
    }

    public static class Serializer extends ConditionSerializer<InWaterCondition> {

        @Override
        public MapCodec<InWaterCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InWaterCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in water.";
        }
    }
}
