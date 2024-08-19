package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public record BuyAbilityUnlockPacket(AbilityReference reference) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BuyAbilityUnlockPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("buy_ability_unlock"));
    public static final StreamCodec<FriendlyByteBuf, BuyAbilityUnlockPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, BuyAbilityUnlockPacket::reference,
            BuyAbilityUnlockPacket::new
    );

    public static void handle(BuyAbilityUnlockPacket packet, NetworkManager.Context context) {
        AbilityInstance<?> instance = packet.reference.getInstance(context.getPlayer());

        if (instance != null) {
            var buyableCondition = instance.getAbility().getConditions().findBuyCondition();

            if (buyableCondition != null && !instance.getOrDefault(PalladiumDataComponents.Abilities.BOUGHT.get(), false) && buyableCondition.isAvailable(context.getPlayer())) {
                buyableCondition.buy(context.getPlayer(), instance);
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
