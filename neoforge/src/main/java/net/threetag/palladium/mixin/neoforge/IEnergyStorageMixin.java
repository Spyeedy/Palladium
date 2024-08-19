package net.threetag.palladium.mixin.neoforge;

import net.threetag.palladium.energy.PalladiumEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PalladiumEnergyStorage.class)
public interface IEnergyStorageMixin extends net.minecraftforge.energy.IEnergyStorage {

    default PalladiumEnergyStorage cast() {
        return (PalladiumEnergyStorage) this;
    }

    @Override
    default int receiveEnergy(int maxReceive, boolean simulate) {
        return this.cast().insertEnergy(maxReceive, simulate);
    }

    @Override
    default int extractEnergy(int maxExtract, boolean simulate) {
        return this.cast().withdrawEnergy(maxExtract, simulate);
    }

    @Override
    default int getEnergyStored() {
        return this.cast().getEnergyAmount();
    }

    @Override
    default int getMaxEnergyStored() {
        return this.cast().getEnergyCapacity();
    }

    @Override
    default boolean canExtract() {
        return this.cast().canWithdraw();
    }

    @Override
    default boolean canReceive() {
        return this.cast().canInsert();
    }
}
