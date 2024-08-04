package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.SyncEntityPowersPacket;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityPowerHandler {

    private final Map<ResourceLocation, PowerHolder> powers = new HashMap<>();
    private final LivingEntity entity;
    private CompoundTag powerData = new CompoundTag();
    private boolean invalid = false;

    public EntityPowerHandler(LivingEntity entity) {
        this.entity = entity;
    }

    public Map<ResourceLocation, PowerHolder> getPowerHolders() {
        return ImmutableMap.copyOf(this.powers);
    }

    public void invalidate() {
        this.invalid = true;
    }

    public void tick() {
        if (!this.entity.level().isClientSide) {
            List<PowerHolder> toRemove = new ArrayList<>();
            PowerCollector collector = new PowerCollector(this.entity, this, toRemove);

            // Find invalid
            if (this.invalid) {
                toRemove.addAll(this.powers.values());
                this.invalid = false;
            }

            // Get new ones
            for (PowerProvider provider : PalladiumRegistries.POWER_PROVIDER) {
                provider.providePowers(this.entity, this, collector);
            }

            // Remove old ones
            for (PowerHolder holder : toRemove) {
                this.removePowerHolder(holder.getPowerId());
            }

            // Add new ones
            for (PowerHolder holder : collector.getAdded()) {
                this.setPowerHolder(holder);
            }

            // Sync
            if (!toRemove.isEmpty() || !collector.getAdded().isEmpty()) {
                var msg = new SyncEntityPowersPacket(this.entity, toRemove.stream().map(p -> p.getPower().getId()).toList(), collector.getAdded().stream().map(p -> p.getPower().getId()).toList());
                if (this.entity instanceof ServerPlayer serverPlayer) {
                    msg.sendToTrackingAndSelf(serverPlayer);
                } else {
                    msg.sendToTracking(this.entity);
                }
            }
        }

        // Tick
        for (PowerHolder holder : this.powers.values()) {
            holder.tick();
        }
    }

    public void setPowerHolder(PowerHolder holder) {
        if (this.hasPower(holder.getPowerId())) {
            this.powers.put(holder.getPowerId(), holder);
        } else {
            this.removePowerHolder(holder.getPowerId());
            this.powers.put(holder.getPowerId(), holder);
            holder.fromNBT(this.powerData.getCompound(holder.getPowerId().toString()));
            holder.firstTick();
        }
    }

    public void removePowerHolder(ResourceLocation powerId) {
        if (this.powers.containsKey(powerId)) {
            var holder = this.powers.get(powerId);
            boolean isStillValid = !holder.getPower().isInvalid();
            boolean hasPersistentData = holder.getPower().hasPersistentData();
            holder.lastTick();

            if (hasPersistentData) {
                this.savePowerNbt(holder);
            }

            this.powers.remove(powerId);

            if (isStillValid && !hasPersistentData) {
                this.powerData.remove(powerId.toString());
            }
        }
    }

    public PowerHolder getPowerHolder(ResourceLocation powerId) {
        return this.powers.get(powerId);
    }

    public boolean hasPower(ResourceLocation powerId) {
        return this.powers.containsKey(powerId);
    }

    public void removeAndAddPowers(List<ResourceLocation> toRemove, List<ResourceLocation> toAdd) {
        if (this.entity.level().isClientSide) {
            for (ResourceLocation powerId : toRemove) {
                this.removePowerHolder(powerId);
            }

            for (ResourceLocation power : toAdd) {
                this.setPowerHolder(new PowerHolder(this.entity, power, PowerValidator.ALWAYS_ACTIVE));
            }
        }
    }

    public void fromNBT(CompoundTag nbt) {
        this.powerData = nbt;
    }

    public CompoundTag toNBT() {
        for (PowerHolder holder : this.powers.values()) {
            this.savePowerNbt(holder);
        }
        this.cleanPowerData();
        return this.powerData;
    }

    public void savePowerNbt(PowerHolder holder) {
        this.powerData.put(holder.getPowerId().toString(), holder.toNBT(true));
    }

    public void cleanPowerData() {
        List<String> toRemove = new ArrayList<>();
        for (String key : this.powerData.getAllKeys()) {
            if (!this.entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).containsKey(ResourceLocation.parse(key))) {
                toRemove.add(key);
            }
        }
        for (String key : toRemove) {
            this.powerData.remove(key);
        }
    }

}
