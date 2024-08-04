package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public record ToggleAccessoryPacket(AccessorySlot slot, Accessory accessory) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleAccessoryPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("toggle_accessory"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleAccessoryPacket> STREAM_CODEC = StreamCodec.composite(
            AccessorySlot.STREAM_CODEC, ToggleAccessoryPacket::slot,
            ByteBufCodecs.registry(PalladiumRegistryKeys.ACCESSORY), ToggleAccessoryPacket::accessory,
            ToggleAccessoryPacket::new
    );

    public static void handle(ToggleAccessoryPacket packet, NetworkManager.Context context) {
        Player player = context.getPlayer();
        if (player != null) {
            Accessory.getPlayerData(player).ifPresent(data -> {
                if (packet.slot != null && packet.accessory != null) {
                    Collection<Accessory> accessories = data.getSlots().get(packet.slot);
                    if (accessories == null || !accessories.contains(packet.accessory)) {
                        data.enable(packet.slot, packet.accessory, player);
                    } else {
                        data.disable(packet.slot, packet.accessory, player);
                    }
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
