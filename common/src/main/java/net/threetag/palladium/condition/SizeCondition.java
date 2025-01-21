package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.SizeUtil;
import net.threetag.palladium.data.DataContext;

public record SizeCondition(float min, float max) implements Condition {

    public static final MapCodec<SizeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("min", 0F).forGetter(SizeCondition::min),
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("max", Float.MAX_VALUE).forGetter(SizeCondition::max)
            ).apply(instance, SizeCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SizeCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SizeCondition::min,
            ByteBufCodecs.FLOAT, SizeCondition::max,
            SizeCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        Entity entity = context.getEntity();

        if (entity == null) {
            return false;
        }

        var averageScale = (SizeUtil.getInstance().getWidthScale(entity) + SizeUtil.getInstance().getHeightScale(entity)) / 2F;
        return averageScale >= this.min && averageScale <= this.max;
    }

    @Override
    public ConditionSerializer<SizeCondition> getSerializer() {
        return ConditionSerializers.SIZE.get();
    }

    public static class Serializer extends ConditionSerializer<SizeCondition> {

        @Override
        public MapCodec<SizeCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SizeCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if an entity is within a certain size (requires Pehkui for real effect). It checks for the \"average \" size, which is the average of the width and height scale. Usually they are the same.";
        }
    }
}
