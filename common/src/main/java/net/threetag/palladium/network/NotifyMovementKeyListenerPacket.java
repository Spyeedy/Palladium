package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public record NotifyMovementKeyListenerPacket(int keyType, boolean active) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<NotifyMovementKeyListenerPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("notify_movement_key_listener"));
    public static final StreamCodec<FriendlyByteBuf, NotifyMovementKeyListenerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, NotifyMovementKeyListenerPacket::keyType,
            ByteBufCodecs.BOOL, NotifyMovementKeyListenerPacket::active,
            NotifyMovementKeyListenerPacket::new
    );

    public static void handle(NotifyMovementKeyListenerPacket packet, NetworkManager.Context context) {
        PalladiumProperty<Boolean> property = null;
        switch (packet.keyType) {
            case 0 -> property = PalladiumProperties.JUMP_KEY_DOWN;
            case 1 -> property = PalladiumProperties.LEFT_KEY_DOWN;
            case 2 -> property = PalladiumProperties.RIGHT_KEY_DOWN;
            case 3 -> property = PalladiumProperties.FORWARD_KEY_DOWN;
            case 4 -> property = PalladiumProperties.BACKWARDS_KEY_DOWN;
        }

        if (property != null) {
            property.set(context.getPlayer(), packet.active);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
