package net.threetag.palladium.power.ability.unlocking;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public abstract class UnlockingHandler {

    public static final Codec<UnlockingHandler> DIRECT_CODEC = PalladiumRegistries.ABILITY_UNLOCKING_HANDLER_SERIALIZER.byNameCodec().dispatch(UnlockingHandler::getSerializer, UnlockingHandlerSerializer::codec);

    public static final Codec<UnlockingHandler> CODEC = Codec.either(
            DIRECT_CODEC,
            Condition.LIST_CODEC
    ).xmap(
            either -> either.map(
                    left -> left,
                    ConditionalUnlockingHandler::new
            ),
            handler -> handler instanceof ConditionalUnlockingHandler cond ? Either.right(cond.conditions) : Either.left(handler)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockingHandler> STREAM_CODEC = ByteBufCodecs.registry(PalladiumRegistryKeys.ABILITY_UNLOCKING_HANDLER_SERIALIZER).dispatch(UnlockingHandler::getSerializer, UnlockingHandlerSerializer::streamCodec);

    public abstract boolean check(LivingEntity entity, PowerHolder powerHolder, AbilityInstance<?> abilityInstance);

    public abstract UnlockingHandlerSerializer<?> getSerializer();

}
