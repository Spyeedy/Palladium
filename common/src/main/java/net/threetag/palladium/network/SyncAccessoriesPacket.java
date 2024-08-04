package net.threetag.palladium.network;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.screen.AccessoryScreen;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record SyncAccessoriesPacket(int entityId,
                                    Map<AccessorySlot, List<Accessory>> accessories) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncAccessoriesPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("sync_accessories"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAccessoriesPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncAccessoriesPacket::entityId,
            ByteBufCodecs.map(Object2ObjectOpenHashMap::new, AccessorySlot.STREAM_CODEC, ByteBufCodecs.registry(PalladiumRegistryKeys.ACCESSORY).apply(ByteBufCodecs.list())), SyncAccessoriesPacket::accessories,
            SyncAccessoriesPacket::new
    );

    public static void handle(SyncAccessoriesPacket packet, NetworkManager.Context context) {
        if (context.isClient()) {
            handleClient(packet, context);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncAccessoriesPacket packet, NetworkManager.Context context) {
        Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(packet.entityId);

        if (entity instanceof Player player) {
            Accessory.getPlayerData(player).ifPresent(data -> {
                data.clear(player);
                packet.accessories.forEach((slot, accessories) -> {
                    for (Accessory accessory : accessories) {
                        data.enable(slot, accessory, player);
                    }
                });
                if (Minecraft.getInstance().screen instanceof AccessoryScreen) {
                    ((AccessoryScreen) Minecraft.getInstance().screen).accessoryList.refreshList();
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
