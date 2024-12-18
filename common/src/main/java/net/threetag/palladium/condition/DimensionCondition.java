package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public record DimensionCondition(ResourceKey<Level> dimension) implements Condition {

    public static final MapCodec<DimensionCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(DimensionCondition::dimension)
            ).apply(instance, DimensionCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DimensionCondition> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), DimensionCondition::dimension, DimensionCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        var level = context.get(DataContextType.LEVEL);
        return level != null && level.dimension().equals(this.dimension);
    }

    @Override
    public ConditionSerializer<DimensionCondition> getSerializer() {
        return ConditionSerializers.DIMENSION.get();
    }

    public static class Serializer extends ConditionSerializer<DimensionCondition> {

        @Override
        public MapCodec<DimensionCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DimensionCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the player is in a specific dimension.";
        }
    }
}
