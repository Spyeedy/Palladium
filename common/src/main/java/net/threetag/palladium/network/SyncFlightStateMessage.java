package net.threetag.palladium.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SyncFlightStateMessage extends MessageS2C {

    private final int entityId;
    private final boolean flying;

    public SyncFlightStateMessage(int entityId, boolean flying) {
        this.entityId = entityId;
        this.flying = flying;
    }

    public SyncFlightStateMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.flying = buf.readBoolean();
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.SYNC_FLYING_STATE;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.flying);
    }

    @Override
    public void handle(MessageContext context) {
        var entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(this.entityId);

        if (entity instanceof PalladiumPlayerExtension extension) {
            var flight = extension.palladium$getFlightHandler();
            if (this.flying) {
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
}
