package net.threetag.palladium.energy;

public interface PalladiumEnergyStorage {

    default boolean canInsert() {
        return true;
    }

    long insertEnergy(long maxAmount, boolean simulate);

    default boolean canWithdraw() {
        return true;
    }

    long withdrawEnergy(long maxAmount, boolean simulate);

    long getEnergyAmount();

    long getEnergyCapacity();

}
