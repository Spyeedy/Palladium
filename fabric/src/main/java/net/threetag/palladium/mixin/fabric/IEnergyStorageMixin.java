package net.threetag.palladium.mixin.fabric;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.threetag.palladium.energy.PalladiumEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import team.reborn.energy.api.EnergyStorage;

@Mixin(PalladiumEnergyStorage.class)
public interface IEnergyStorageMixin extends EnergyStorage {

    default PalladiumEnergyStorage cast() {
        return (PalladiumEnergyStorage) this;
    }

    @Override
    default boolean supportsInsertion() {
        return this.cast().canInsert();
    }

    @Override
    default long insert(long maxAmount, TransactionContext transaction) {
        return this.cast().insertEnergy((int) maxAmount, false);
    }

    @Override
    default boolean supportsExtraction() {
        return this.cast().canWithdraw();
    }

    @Override
    default long extract(long maxAmount, TransactionContext transaction) {
        return this.cast().withdrawEnergy((int) maxAmount, false);
    }

    @Override
    default long getAmount() {
        return this.cast().getEnergyAmount();
    }

    @Override
    default long getCapacity() {
        return this.cast().getEnergyCapacity();
    }
}
