package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public record SyncAbilityStatePacket(int entityId, AbilityReference reference, boolean unlocked, boolean enabled,
                                     int maxCooldown, int cooldown, int maxActivationTimer,
                                     int activationTimer) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncAbilityStatePacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("sync_ability_state_message"));
    public static final StreamCodec<FriendlyByteBuf, SyncAbilityStatePacket> STREAM_CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeInt(packet.entityId);
        packet.reference.toBuffer(buf);
        buf.writeBoolean(packet.unlocked);
        buf.writeBoolean(packet.enabled);
        buf.writeInt(packet.maxCooldown);
        buf.writeInt(packet.cooldown);
        buf.writeInt(packet.maxActivationTimer);
        buf.writeInt(packet.activationTimer);
    }, buf -> {
        var entityId = buf.readInt();
        var reference = AbilityReference.fromBuffer(buf);
        var unlocked = buf.readBoolean();
        var enabled = buf.readBoolean();
        var maxCooldown = buf.readInt();
        var cooldown = buf.readInt();
        var maxActivationTimer = buf.readInt();
        var activationTimer = buf.readInt();
        return new SyncAbilityStatePacket(entityId, reference, unlocked, enabled, maxCooldown, cooldown, maxActivationTimer, activationTimer);
    });

    @Environment(EnvType.CLIENT)
    public static void handle(SyncAbilityStatePacket packet, NetworkManager.Context context) {
        Entity entity = context.getPlayer().level().getEntity(packet.entityId);

        if (entity instanceof LivingEntity livingEntity) {
            AbilityInstance entry = packet.reference.getInstance(livingEntity);

            if (entry != null) {
                entry.setClientState(livingEntity, entry.getHolder(), packet.unlocked, packet.enabled, packet.maxCooldown, packet.cooldown, packet.maxActivationTimer, packet.activationTimer);

                if (Minecraft.getInstance().screen instanceof PowersScreen powers && powers.selectedTab != null) {
                    powers.selectedTab.populate();
                }
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
