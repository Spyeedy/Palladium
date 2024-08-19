package net.threetag.palladium.energy.fabric;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.threetag.palladium.energy.PalladiumEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

public class FabricEnergyStorageWrapper implements PalladiumEnergyStorage {

    private final EnergyStorage energyStorage;

    public FabricEnergyStorageWrapper(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        var trans = Transaction.openNested(null);
        var received = this.energyStorage.insert(maxAmount, trans);

        if (simulate) {
            trans.abort();
        } else {
            trans.commit();
        }

        return (int) received;
    }

    @Override
    public long withdrawEnergy(long maxAmount, boolean simulate) {
        var trans = Transaction.openNested(null);
        var extracted = this.energyStorage.extract(maxAmount, trans);

        if (simulate) {
            trans.abort();
        } else {
            trans.commit();
        }

        return (int) extracted;
    }

    @Override
    public long getEnergyAmount() {
        return this.energyStorage.getAmount();
    }

    @Override
    public long getEnergyCapacity() {
        return this.energyStorage.getCapacity();
    }

    @Override
    public boolean canInsert() {
        return this.energyStorage.supportsInsertion();
    }

    @Override
    public boolean canWithdraw() {
        return this.energyStorage.supportsExtraction();
    }
}
