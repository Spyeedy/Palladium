package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.util.context.DataContext;

public class NightCondition implements Condition {

    public static final NightCondition INSTANCE = new NightCondition();

    public static final MapCodec<NightCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, NightCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean active(DataContext context) {
        var level = context.getLevel();
        return level != null && level.isNight();
    }

    @Override
    public ConditionSerializer<NightCondition> getSerializer() {
        return ConditionSerializers.NIGHT.get();
    }

    public static class Serializer extends ConditionSerializer<NightCondition> {

        @Override
        public MapCodec<NightCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NightCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if it's currently nighttime";
        }
    }
}
