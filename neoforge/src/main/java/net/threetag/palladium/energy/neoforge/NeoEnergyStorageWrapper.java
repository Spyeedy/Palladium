package net.threetag.palladium.energy.neoforge;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.threetag.palladium.energy.PalladiumEnergyStorage;

public class NeoEnergyStorageWrapper implements PalladiumEnergyStorage {

    private final IEnergyStorage energyStorage;

    public NeoEnergyStorageWrapper(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return this.energyStorage.receiveEnergy((int) maxAmount, simulate);
    }

    @Override
    public long withdrawEnergy(long maxAmount, boolean simulate) {
        return this.energyStorage.extractEnergy((int) maxAmount, simulate);
    }

    @Override
    public long getEnergyAmount() {
        return this.energyStorage.getEnergyStored();
    }

    @Override
    public long getEnergyCapacity() {
        return this.energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canWithdraw() {
        return this.energyStorage.canExtract();
    }

    @Override
    public boolean canInsert() {
        return this.energyStorage.canReceive();
    }
}
