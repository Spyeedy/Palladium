package net.threetag.palladium.accessory;

import com.google.common.graph.Network;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.network.SyncAccessoriesPacket;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladiumcore.network.NetworkManager;
import net.threetag.palladiumcore.util.Platform;

import org.jetbrains.annotations.Nullable;
import java.util.*;

public class AccessoryPlayerData {

    public Map<AccessorySlot, List<Accessory>> accessories = new HashMap<>();

    public void enable(AccessorySlot slot, Accessory accessory, Player player) {
        if (slot != null && accessory != null && accessory.getPossibleSlots().contains(slot) && canEnable(accessory, player)) {
            if (slot.allowsMultiple()) {
                Collection<Accessory> accessories = this.accessories.computeIfAbsent(slot, accessorySlot -> new ArrayList<>());
                if (!accessories.contains(accessory)) {
                    accessories.add(accessory);
                }
            } else {
                this.accessories.put(slot, Collections.singletonList(accessory));
            }

            if (!player.level().isClientSide)
                NetworkManager.get().sendToPlayersInDimension((ServerLevel) player.level(), new SyncAccessoriesPacket(player.getId(), this.accessories));
        }
    }

    public boolean canEnable(Accessory accessory, Player player) {
        return Platform.isClient() || accessory.isAvailable(player);
    }

    public void disable(AccessorySlot slot, @Nullable Accessory accessory, Player player) {
        if (slot != null && accessory != null) {
            if (slot.allowsMultiple()) {
                Collection<Accessory> accessories = this.accessories.computeIfAbsent(slot, accessorySlot -> new ArrayList<>());
                accessories.remove(accessory);
            } else {
                this.accessories.put(slot, new ArrayList<>());
            }
            if (!player.level().isClientSide)
                NetworkManager.get().sendToPlayersInDimension((ServerLevel) player.level(), new SyncAccessoriesPacket(player.getId(), this.accessories));
        }
    }

    public void validate(Player player) {
        List<Pair<AccessorySlot, Accessory>> disable = new ArrayList<>();

        this.accessories.forEach((slot, accessories) -> {
            for (Accessory accessory : accessories) {
                if (!canEnable(accessory, player)) {
                    disable.add(Pair.of(slot, accessory));
                }
            }
        });
        for (Pair<AccessorySlot, Accessory> pair : disable) {
            this.disable(pair.getFirst(), pair.getSecond(), player);
        }
    }

    public void clear(Player player) {
        this.accessories.clear();
        for (AccessorySlot slot : AccessorySlot.getSlots()) {
            this.accessories.put(slot, new ArrayList<>());
        }
        if (!player.level().isClientSide)
            NetworkManager.get().sendToPlayersInDimension((ServerLevel) player.level(), new SyncAccessoriesPacket(player.getId(), this.accessories));
    }

    public Map<AccessorySlot, List<Accessory>> getSlots() {
        return this.accessories;
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        this.accessories.forEach((slot, list) -> {
            ListTag listNBT = new ListTag();
            for (Accessory accessory : list) {
                listNBT.add(StringTag.valueOf(Objects.requireNonNull(PalladiumRegistries.ACCESSORY.getKey(accessory)).toString()));
            }
            nbt.put(slot.getName().toString(), listNBT);
        });
        return nbt;
    }

    public void fromNBT(CompoundTag nbt) {
        this.accessories = new HashMap<>();
        for (AccessorySlot slot : AccessorySlot.getSlots()) {
            ListTag listNBT = nbt.getList(slot.getName().toString(), 8);
            List<Accessory> accessories = new ArrayList<>();
            for (int i = 0; i < listNBT.size(); i++) {
                Accessory accessory =PalladiumRegistries.ACCESSORY.get(ResourceLocation.parse(listNBT.getString(i)));
                if (accessory != null) {
                    accessories.add(accessory);
                }
            }
            this.accessories.put(slot, accessories);
        }
    }

}
