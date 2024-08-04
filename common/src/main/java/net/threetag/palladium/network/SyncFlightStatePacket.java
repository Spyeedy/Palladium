package net.threetag.palladium.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record SyncFlightStatePacket(int entityId, boolean flying) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncFlightStatePacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("sync_flight_state"));
    public static final StreamCodec<ByteBuf, SyncFlightStatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncFlightStatePacket::entityId,
            ByteBufCodecs.BOOL, SyncFlightStatePacket::flying,
            SyncFlightStatePacket::new
    );

    public static void handle(SyncFlightStatePacket packet, NetworkManager.Context context) {
        if (context.isClient()) {
            handleClient(packet, context);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncFlightStatePacket packet, NetworkManager.Context context) {
        var entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(packet.entityId);

        if (entity instanceof PalladiumPlayerExtension extension) {
            var flight = extension.palladium$getFlightHandler();
            if (packet.flying) {
                var flightType = FlightHandler.getAvailableFlightType((Player) entity);

                if (flightType.isNotNull()) {
                    flight.setFlightType(flightType);
                }
            } else {
                flight.setFlightType(FlightHandler.FlightType.NONE);
            }

            ((Player) entity).getAbilities().flying = false;
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;

    }
}
