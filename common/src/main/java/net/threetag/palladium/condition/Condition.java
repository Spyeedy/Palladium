package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityConditions;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.context.DataContext;

import java.util.Collections;
import java.util.List;

public interface Condition {

    Codec<Condition> CODEC = PalladiumRegistries.CONDITION_SERIALIZER.byNameCodec().dispatch(Condition::getSerializer, ConditionSerializer::codec);
    Codec<List<Condition>> LIST_CODEC = CodecUtils.listOrPrimitive(PalladiumRegistries.CONDITION_SERIALIZER.byNameCodec().dispatch(Condition::getSerializer, ConditionSerializer::codec));
    StreamCodec<RegistryFriendlyByteBuf, Condition> STREAM_CODEC = ByteBufCodecs.registry(PalladiumRegistryKeys.CONDITION_SERIALIZER).dispatch(Condition::getSerializer, ConditionSerializer::streamCodec);

    boolean active(DataContext context);

    default boolean needsKey() {
        return false;
    }

    default AbilityConditions.KeyType getKeyType() {
        return AbilityConditions.KeyType.KEY_BIND;
    }

    default AbilityConditions.KeyPressType getKeyPressType() {
        return AbilityConditions.KeyPressType.ACTION;
    }

    default boolean handlesCooldown() {
        return false;
    }

    default CooldownType getCooldownType() {
        return CooldownType.STATIC;
    }

    default void init(LivingEntity entity, AbilityInstance<?> abilityInstance) {

    }

    default void registerDataComponents(DataComponentMap.Builder components) {

    }

    default void onKeyPressed(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder) {

    }

    default void onKeyReleased(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder) {

    }

    ConditionSerializer<?> getSerializer();

    default List<String> getDependentAbilities() {
        return Collections.emptyList();
    }
}
