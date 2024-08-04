package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.context.DataContext;

public record AbilityTypeUnlockedCondition(Ability ability) implements Condition {

    public static final MapCodec<AbilityTypeUnlockedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(PalladiumRegistries.ABILITY.byNameCodec().fieldOf("ability_type").forGetter(AbilityTypeUnlockedCondition::ability)
            ).apply(instance, AbilityTypeUnlockedCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityTypeUnlockedCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(PalladiumRegistryKeys.ABILITY), AbilityTypeUnlockedCondition::ability, AbilityTypeUnlockedCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        return AbilityUtil.isTypeUnlocked(entity, this.ability);
    }

    @Override
    public ConditionSerializer<AbilityTypeUnlockedCondition> getSerializer() {
        return ConditionSerializers.ABILITY_TYPE_UNLOCKED.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityTypeUnlockedCondition> {

        @Override
        public MapCodec<AbilityTypeUnlockedCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityTypeUnlockedCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if an ability of a certain type is unlocked.";
        }
    }
}