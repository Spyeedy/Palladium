package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.condition.BuyableCondition;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public record RequestAbilityBuyScreenPacket(AbilityReference reference) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestAbilityBuyScreenPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("request_ability_buy_screen"));
    public static final StreamCodec<FriendlyByteBuf, RequestAbilityBuyScreenPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, RequestAbilityBuyScreenPacket::reference,
            RequestAbilityBuyScreenPacket::new
    );

    public static void handle(RequestAbilityBuyScreenPacket packet, NetworkManager.Context context) {
        AbilityInstance entry = packet.reference.getInstance(context.getPlayer());

        if (entry != null) {
            var buyableCondition = entry.getConfiguration().getConditions().findBuyCondition();

            if (buyableCondition != null && !entry.getProperty(BuyableCondition.BOUGHT)) {
                for (AbilityInstance parentEntry : AbilityUtil.findParentsWithinHolder(entry.getConfiguration(), entry.getHolder())) {
                    if (!parentEntry.isUnlocked()) {
                        return;
                    }
                }

                NetworkManager.get().sendToPlayer((ServerPlayer) context.getPlayer(), new OpenAbilityBuyScreenPacket(packet.reference, buyableCondition.createData(), buyableCondition.isAvailable(context.getPlayer())));
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
