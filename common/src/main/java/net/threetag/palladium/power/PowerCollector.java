package net.threetag.palladium.power;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PowerCollector {

    private final LivingEntity entity;
    private final EntityPowerHandler handler;
    private final List<PowerHolder> toRemove;
    private final List<PowerHolder> powerHolders = new ArrayList<>();

    public PowerCollector(LivingEntity entity, EntityPowerHandler handler, List<PowerHolder> toRemove) {
        this.entity = entity;
        this.handler = handler;
        this.toRemove = toRemove;
    }

    public void addPower(Power power, Supplier<PowerValidator> validatorSupplier) {
        if (power == null) {
            return;
        }

        PowerHolder found = null;
        for (PowerHolder holder : this.toRemove) {
            if (holder.getPower() == power) {
                found = holder;
                break;
            }
        }

        if (found != null) {
            found.switchValidator(validatorSupplier.get());
            this.toRemove.remove(found);
            return;
        }

        if (!this.handler.hasPower(this.entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power))) {
            this.powerHolders.add(new PowerHolder(this.entity, power, validatorSupplier.get()));
        }
    }

    public List<PowerHolder> getAdded() {
        return this.powerHolders;
    }
}
