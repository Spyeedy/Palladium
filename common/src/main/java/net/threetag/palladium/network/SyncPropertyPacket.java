package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyType;
import net.threetag.palladium.util.property.PalladiumPropertyValue;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class SyncPropertyPacket<T> implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncPropertyPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("sync_property"));
    public static final StreamCodec<FriendlyByteBuf, SyncPropertyPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.entityId);
                buf.writeUtf(packet.propertyKey);
                buf.writeUtf(packet.property.getType().getName());
                packet.property.getType().getStreamCodec().encode(buf, packet.value);
            }, buf -> {
                var entityId = buf.readInt();
                var propertyKey = buf.readUtf();
                PalladiumPropertyType propType = PalladiumPropertyType.getType(buf.readUtf());
                var val = propType.getStreamCodec().decode(buf);

                return new SyncPropertyPacket(entityId, propertyKey, val);
            }
    );

    private final int entityId;
    private final String propertyKey;
    private final T value;

    private PalladiumProperty<T> property;

    // Instantiate for server-side
    public SyncPropertyPacket(int entityId, PalladiumPropertyValue<T> valueHolder) {
        this.entityId = entityId;
        this.property = valueHolder.getProperty();
        this.propertyKey = this.property.getKey();
        this.value = valueHolder.value();
    }

    // Instantiate for client-side
    public SyncPropertyPacket(int entityId, String propertyKey, T value) {
        this.entityId = entityId;
        this.propertyKey = propertyKey;
        this.value = value;
    }

    public static <T> void handle(SyncPropertyPacket<?> packet, NetworkManager.Context context) {
        Entity entity = Objects.requireNonNull(context.getPlayer().level()).getEntity(packet.entityId);
        if (entity != null) {
            EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
                PalladiumProperty property = handler.getPropertyByName(packet.propertyKey);
                handler.set(property, packet.value);
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
