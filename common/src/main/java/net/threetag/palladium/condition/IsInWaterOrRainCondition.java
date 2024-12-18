package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public class IsInWaterOrRainCondition implements Condition {

    public static final IsInWaterOrRainCondition INSTANCE = new IsInWaterOrRainCondition();

    public static final MapCodec<IsInWaterOrRainCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsInWaterOrRainCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWaterOrRain();
    }

    @Override
    public ConditionSerializer<IsInWaterOrRainCondition> getSerializer() {
        return ConditionSerializers.IS_IN_WATER_OR_RAIN.get();
    }

    public static class Serializer extends ConditionSerializer<IsInWaterOrRainCondition> {

        @Override
        public MapCodec<IsInWaterOrRainCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsInWaterOrRainCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in water or rain.";
        }
    }
}
