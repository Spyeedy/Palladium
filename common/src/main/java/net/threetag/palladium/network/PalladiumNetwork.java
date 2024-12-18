package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkSource;

import java.util.*;
import java.util.stream.Collectors;

public class PalladiumNetwork {

    public static void init() {
        // Server -> Client
        registerS2C(SyncEntityPowersPacket.TYPE, SyncEntityPowersPacket.STREAM_CODEC, SyncEntityPowersPacket::handle);

        // Client -> Server

        // TODO
        // Powers
//        DataSyncUtil.registerEntitySync((entity, consumer) -> {
//            if (entity instanceof LivingEntity livingEntity) {
//                consumer.accept(SyncEntityPowersPacket.create(livingEntity));
//
////                    for (AbilityInstance<?> entry : AbilityUtil.getInstances(livingEntity)) {
////                        consumer.accept(entry.getSyncStateMessage(livingEntity));
////                    }
//            }
//        });

        // Accessories
//        DataSyncUtil.registerEntitySync((entity, consumer) -> {
//            if (entity instanceof ServerPlayer serverPlayer) {
//                var opt = Accessory.getPlayerData(serverPlayer);
//                opt.ifPresent(accessoryPlayerData -> consumer.accept(new SyncAccessoriesPacket(serverPlayer.getId(), accessoryPlayerData.accessories)));
//            }
//        });
//
//        // Properties
//        DataSyncUtil.registerEntitySync((entity, consumer) -> {
//            EntityPropertyHandler.getHandler(entity).ifPresent(properties -> {
//                properties.getHolders().forEach((holder) -> consumer.accept(new SyncPropertyPacket<>(entity.getId(), holder)));
//            });
//        });
    }

    public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        if (Platform.getEnvironment() == Env.SERVER) {
            NetworkManager.registerS2CPayloadType(type, codec);
        } else {
            NetworkManager.registerReceiver(NetworkManager.s2c(), type, codec, receiver);
        }
    }

    public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), type, codec, receiver);
    }

    public static void sendToTrackingAndSelf(Entity entity, CustomPacketPayload packet) {
        if (entity.level().isClientSide()) {
            throw new IllegalStateException("Cannot send clientbound payloads on the client");
        } else {
            if (entity instanceof ServerPlayer player) {
                NetworkManager.sendToPlayer(player, packet);
            }
            for (ServerPlayer player : tracking(entity)) {
                NetworkManager.sendToPlayer(player, packet);
            }
        }
    }

    public static Collection<ServerPlayer> tracking(Entity entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ChunkSource manager = entity.level().getChunkSource();
        if (manager instanceof ServerChunkCache) {
            ChunkMap chunkLoadingManager = ((ServerChunkCache) manager).chunkMap;
            ChunkMap.TrackedEntity tracker = chunkLoadingManager.entityMap.get(entity.getId());
            return (tracker != null ? tracker.seenBy.stream().map(ServerPlayerConnection::getPlayer).collect(Collectors.toUnmodifiableSet()) : Collections.emptySet());
        } else {
            throw new IllegalArgumentException("Only supported on server worlds!");
        }
    }
}
