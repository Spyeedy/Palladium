package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyType;
import net.threetag.palladium.util.property.PalladiumPropertyValue;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class SyncAbilityInstancePropertyPacket<T> implements CustomPacketPayload {

    public static final Type<SyncAbilityInstancePropertyPacket> TYPE = new Type<>(Palladium.id("sync_ability_instance_property"));
    public static final StreamCodec<FriendlyByteBuf, SyncAbilityInstancePropertyPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.entityId);
                AbilityReference.STREAM_CODEC.encode(buf, packet.reference);
                buf.writeUtf(packet.propertyKey);
                buf.writeUtf(packet.property.getType().getName());
                packet.property.getType().getStreamCodec().encode(buf, packet.value);
            }, buf -> {
                var entityId = buf.readInt();
                var ref = AbilityReference.STREAM_CODEC.decode(buf);
                var propertyKey = buf.readUtf();
                PalladiumPropertyType propType = PalladiumPropertyType.getType(buf.readUtf());
                var val = propType.getStreamCodec().decode(buf);

                return new SyncAbilityInstancePropertyPacket(entityId, ref, propertyKey, val);
            }
    );

    private final int entityId;
    private final AbilityReference reference;
    private final String propertyKey;
    private final T value;

    private PalladiumProperty<T> property;

    // Instantiate for server-side
    public SyncAbilityInstancePropertyPacket(int entityId, AbilityReference reference, PalladiumPropertyValue<T> valueHolder) {
        this.entityId = entityId;
        this.reference = reference;
        this.property = valueHolder.getProperty();
        this.propertyKey = this.property.getKey();
        this.value = valueHolder.value();
    }

    // Instantiate for client-side
    public SyncAbilityInstancePropertyPacket(int entityId, AbilityReference reference, String propertyKey, T value) {
        this.entityId = entityId;
        this.reference = reference;
        this.propertyKey = propertyKey;
        this.value = value;
    }

    public static void handle(SyncAbilityInstancePropertyPacket packet, NetworkManager.Context context) {
        if (context.isClient()) {
            handleClient(packet, context);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncAbilityInstancePropertyPacket packet, NetworkManager.Context context) {
        var level = Objects.requireNonNull(Minecraft.getInstance().level);
        Entity entity = level.getEntity(packet.entityId);
        if (entity instanceof LivingEntity livingEntity) {
            AbilityInstance instance = packet.reference.getInstance(livingEntity);

            if (instance != null) {
                var handler = instance.getPropertyManager();
                PalladiumProperty property = handler.getPropertyByName(packet.propertyKey);
                handler.set(property, packet.value);
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
