package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public class IsUnderWaterCondition implements Condition {

    public static final IsUnderWaterCondition INSTANCE = new IsUnderWaterCondition();

    public static final MapCodec<IsUnderWaterCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsUnderWaterCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isUnderWater();
    }

    @Override
    public ConditionSerializer<IsUnderWaterCondition> getSerializer() {
        return ConditionSerializers.IS_UNDER_WATER.get();
    }

    public static class Serializer extends ConditionSerializer<IsUnderWaterCondition> {

        @Override
        public MapCodec<IsUnderWaterCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsUnderWaterCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is under water.";
        }
    }
}
