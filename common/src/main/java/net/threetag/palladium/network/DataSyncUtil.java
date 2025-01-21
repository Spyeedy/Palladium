package net.threetag.palladium.network;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.core.event.PalladiumPlayerEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DataSyncUtil {

    private static final List<EntitySync> ENTITY_SYNC = new ArrayList<>();

    public static void registerEntitySync(EntitySync entitySync) {
        ENTITY_SYNC.add(entitySync);
    }

    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                for (EntitySync entitySync : ENTITY_SYNC) {
                    entitySync.gatherPayloads(serverPlayer, payload -> NetworkManager.sendToPlayer(serverPlayer, payload));
                }
            }
        });

        PalladiumPlayerEvents.START_TRACKING.register((tracker, target) -> {
            if (tracker instanceof ServerPlayer serverPlayer) {
                for (EntitySync entitySync : ENTITY_SYNC) {
                    entitySync.gatherPayloads(target, payload -> NetworkManager.sendToPlayer(serverPlayer, payload));
                }
            }
        });

        PlayerEvent.PLAYER_RESPAWN.register((player, endConquered, removalReason) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                for (EntitySync entitySync : ENTITY_SYNC) {
                    entitySync.gatherPayloads(player, payload -> PalladiumNetwork.sendToTrackingAndSelf(serverPlayer, payload));
                }
            }
        });

        PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                for (EntitySync entitySync : ENTITY_SYNC) {
                    entitySync.gatherPayloads(player, payload -> PalladiumNetwork.sendToTrackingAndSelf(serverPlayer, payload));
                }
            }
        });
    }

    @FunctionalInterface
    public interface EntitySync {

        void gatherPayloads(Entity entity, Consumer<CustomPacketPayload> consumer);

    }

}
