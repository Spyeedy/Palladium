package net.threetag.palladium.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladiumcore.network.NetworkManager;
import net.threetag.palladiumcore.util.DataSyncUtil;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PalladiumNetwork {

    public static void init() {
        var net = NetworkManager.get();

        net.registerC2S(AbilityKeyPressedPacket.TYPE, AbilityKeyPressedPacket.STREAM_CODEC, AbilityKeyPressedPacket::handle);
        net.registerC2S(BuyAbilityUnlockPacket.TYPE, BuyAbilityUnlockPacket.STREAM_CODEC, BuyAbilityUnlockPacket::handle);
        net.registerC2S(SetFlyingStatePacket.TYPE, SetFlyingStatePacket.STREAM_CODEC, SetFlyingStatePacket::handle);
        net.registerC2S(RequestAbilityBuyScreenPacket.TYPE, RequestAbilityBuyScreenPacket.STREAM_CODEC, RequestAbilityBuyScreenPacket::handle);
        net.registerC2S(ToggleAccessoryPacket.TYPE, ToggleAccessoryPacket.STREAM_CODEC, ToggleAccessoryPacket::handle);
        net.registerC2S(ToggleOpenableEquipmentPacket.TYPE, ToggleOpenableEquipmentPacket.STREAM_CODEC, ToggleOpenableEquipmentPacket::handle);

        net.registerS2C(OpenAbilityBuyScreenPacket.TYPE, OpenAbilityBuyScreenPacket.STREAM_CODEC, OpenAbilityBuyScreenPacket::handle);
        net.registerS2C(SyncEnergyBarPacket.TYPE, SyncEnergyBarPacket.STREAM_CODEC, SyncEnergyBarPacket::handle);
        net.registerS2C(SyncAccessoriesPacket.TYPE, SyncAccessoriesPacket.STREAM_CODEC, SyncAccessoriesPacket::handle);
        net.registerS2C(SyncFlightStatePacket.TYPE, SyncFlightStatePacket.STREAM_CODEC, SyncFlightStatePacket::handle);
        net.registerS2C(SyncEntityPowersPacket.TYPE, SyncEntityPowersPacket.STREAM_CODEC, SyncEntityPowersPacket::handle);
        net.registerS2C(SyncPropertyPacket.TYPE, SyncPropertyPacket.STREAM_CODEC, SyncPropertyPacket::handle);

        // Powers
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            if (entity instanceof LivingEntity livingEntity) {
                var opt = PowerUtil.getPowerHandler(livingEntity);

                if (opt.isPresent()) {
                    var handler = opt.get();

                    List<Triple<EnergyBarReference, Integer, Integer>> energyBars = new ArrayList<>();
                    for (PowerHolder holder : opt.get().getPowerHolders().values()) {
                        for (EnergyBar energyBar : holder.getEnergyBars().values()) {
                            energyBars.add(Triple.of(new EnergyBarReference(holder.getPowerId(), energyBar.getConfiguration().getKey()), energyBar.get(), energyBar.getMax()));
                        }
                    }

                    consumer.accept(new SyncEntityPowersPacket(livingEntity.getId(), Collections.emptyList(), handler.getPowerHolders().keySet().stream().toList(), energyBars));

//                    for (AbilityInstance<?> entry : AbilityUtil.getInstances(livingEntity)) {
//                        consumer.accept(entry.getSyncStateMessage(livingEntity));
//                    }
                }
            }
        });

        // Accessories
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                var opt = Accessory.getPlayerData(serverPlayer);
                opt.ifPresent(accessoryPlayerData -> consumer.accept(new SyncAccessoriesPacket(serverPlayer.getId(), accessoryPlayerData.accessories)));
            }
        });

        // Properties
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            EntityPropertyHandler.getHandler(entity).ifPresent(properties -> {
                properties.getHolders().forEach((holder) -> consumer.accept(new SyncPropertyPacket<>(entity.getId(), holder)));
            });
        });
    }
}
