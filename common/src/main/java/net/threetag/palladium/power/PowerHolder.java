package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.energybar.EnergyBarConfiguration;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PowerHolder {

    public final LivingEntity entity;
    private final Power power;
    private final ResourceLocation powerId;
    private final Map<String, AbilityInstance> entryMap = new HashMap<>();
    private final Map<String, EnergyBar> energyBars = new LinkedHashMap<>();
    private PowerValidator validator;

    public PowerHolder(LivingEntity entity, Power power, PowerValidator validator) {
        this.entity = entity;
        this.power = power;
        this.powerId = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power);
        this.validator = validator;

        for (Map.Entry<String, AbilityConfiguration> e : this.getPower().getAbilities().entrySet()) {
            AbilityInstance entry = new AbilityInstance(e.getValue(), this, new AbilityReference(entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power), e.getKey()));
            this.entryMap.put(e.getKey(), entry);
        }
        for (Map.Entry<String, EnergyBarConfiguration> e : this.getPower().getEnergyBars().entrySet()) {
            this.energyBars.put(e.getKey(), new EnergyBar(e.getValue(), this, new EnergyBarReference(entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power), e.getKey())));
        }
    }

    public Power getPower() {
        return this.power;
    }

    public ResourceLocation getPowerId() {
        return this.powerId;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public void fromNBT(CompoundTag tag) {
        for (Map.Entry<String, AbilityInstance> entry : this.entryMap.entrySet()) {
            if (tag.contains(entry.getKey())) {
                CompoundTag abData = tag.getCompound(entry.getKey());
                entry.getValue().fromNBT(abData);
            }
        }

        if (tag.contains("_EnergyBars", 10)) {
            var energies = tag.getCompound("_EnergyBars");
            for (String key : energies.getAllKeys()) {
                if (this.energyBars.containsKey(key)) {
                    this.energyBars.get(key).fromNBT(energies.getCompound(key));
                }
            }
        }
    }

    public CompoundTag toNBT(boolean toDisk) {
        CompoundTag tag = new CompoundTag();

        for (Map.Entry<String, AbilityInstance> entry : this.entryMap.entrySet()) {
            CompoundTag abData = entry.getValue().toNBT(toDisk);
            tag.put(entry.getKey(), abData);
        }

        CompoundTag energies = new CompoundTag();
        for (Map.Entry<String, EnergyBar> entry : this.energyBars.entrySet()) {
            energies.put(entry.getKey(), entry.getValue().toNBT());
        }
        tag.put("_EnergyBars", energies);

        return tag;
    }

    public Map<String, AbilityInstance> getAbilities() {
        return ImmutableMap.copyOf(this.entryMap);
    }

    public Map<String, EnergyBar> getEnergyBars() {
        return ImmutableMap.copyOf(this.energyBars);
    }

    public void tick() {
        this.entryMap.forEach((id, entry) -> entry.tick(entity, this));

        if (!this.getEntity().level().isClientSide) {
            this.energyBars.forEach((id, bar) -> bar.tick(entity));
        }
    }

    public void firstTick() {
        this.entryMap.forEach((id, entry) -> entry.getConfiguration().getAbility().firstTick(entity, entry, this, entry.isEnabled()));
    }

    public void lastTick() {
        this.entryMap.forEach((id, entry) -> entry.getConfiguration().getAbility().lastTick(entity, entry, this, entry.isEnabled()));
    }

    public boolean isInvalid() {
        return this.power.isInvalid() || !this.validator.stillValid(this.entity, this.power);
    }

    public void switchValidator(PowerValidator validator) {
        this.validator = validator;
    }
}
