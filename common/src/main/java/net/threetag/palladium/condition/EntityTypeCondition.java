package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public record EntityTypeCondition(EntityType<?> entityType) implements Condition {

    public static final MapCodec<EntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityTypeCondition::entityType)
            ).apply(instance, EntityTypeCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), EntityTypeCondition::entityType, EntityTypeCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.getType() == this.entityType;
    }

    @Override
    public ConditionSerializer<EntityTypeCondition> getSerializer() {
        return ConditionSerializers.ENTITY_TYPE.get();
    }

    public static class Serializer extends ConditionSerializer<EntityTypeCondition> {

        @Override
        public MapCodec<EntityTypeCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EntityTypeCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is of a specific entity type.";
        }
    }
}
