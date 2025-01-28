package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilityReference;
import org.jetbrains.annotations.NotNull;

public record AbilityClickedPacket(AbilityReference reference) implements CustomPacketPayload {

    public static final Type<AbilityClickedPacket> TYPE = new Type<>(Palladium.id("ability_clicked"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityClickedPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityClickedPacket::reference,
            AbilityClickedPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AbilityClickedPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> packet.reference.optional(context.getPlayer(), null).ifPresent(ability -> {
            ability.getAbility().getStateManager().getUnlockingHandler().onClicked(context.getPlayer(), ability);
        }));
    }

}
