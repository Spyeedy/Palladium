package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.context.DataContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record NotCondition(List<Condition> conditions) implements Condition {

    public static final MapCodec<NotCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(CodecUtils.listOrPrimitive(Condition.CODEC).fieldOf("conditions").forGetter(NotCondition::conditions)
            ).apply(instance, NotCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, NotCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, Condition.STREAM_CODEC), NotCondition::conditions, NotCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        for (Condition condition : this.conditions) {
            if (condition.active(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer<NotCondition> getSerializer() {
        return ConditionSerializers.NOT.get();
    }

    public static class Serializer extends ConditionSerializer<NotCondition> {

        @Override
        public MapCodec<NotCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NotCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns true if all conditions are disabled.";
        }
    }
}
