package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public record SetFlyingStatePacket(boolean flying) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SetFlyingStatePacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("set_flying_state"));
    public static final StreamCodec<FriendlyByteBuf, SetFlyingStatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SetFlyingStatePacket::flying,
            SetFlyingStatePacket::new
    );

    public static void handle(SetFlyingStatePacket packet, NetworkManager.Context context) {
        if (context.getPlayer() instanceof PalladiumPlayerExtension extension) {
            var flight = extension.palladium$getFlightHandler();
            if (packet.flying) {
                var flightType = FlightHandler.getAvailableFlightType(context.getPlayer());

                if (flightType.isNotNull()) {
                    flight.setFlightType(flightType);
                }
            } else {
                flight.setFlightType(FlightHandler.FlightType.NONE);
            }

            context.getPlayer().getAbilities().flying = false;
            NetworkManager.get().sendToPlayersTrackingEntity(context.getPlayer(), new SyncFlightStatePacket(context.getPlayer().getId(), packet.flying));
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
