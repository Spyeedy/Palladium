package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.context.DataContext;

import java.util.ArrayList;
import java.util.List;

public record AndCondition(List<Condition> conditions) implements Condition {

    public static final MapCodec<AndCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(CodecUtils.listOrPrimitive(Condition.CODEC).fieldOf("conditions").forGetter(AndCondition::conditions)
            ).apply(instance, AndCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AndCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, Condition.STREAM_CODEC), AndCondition::conditions, AndCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        for (Condition condition : this.conditions) {
            if (!condition.active(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer<AndCondition> getSerializer() {
        return ConditionSerializers.AND.get();
    }

    public static class Serializer extends ConditionSerializer<AndCondition> {

        @Override
        public MapCodec<AndCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AndCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "A condition that is active if all of the conditions in the array are active.";
        }
    }
}
