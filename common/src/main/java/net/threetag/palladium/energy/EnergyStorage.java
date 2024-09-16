package net.threetag.palladium.energy;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;

public class EnergyStorage implements PalladiumEnergyStorage {

    protected long energy;
    protected long capacity;
    protected long maxReceive;
    protected long maxExtract;

    public EnergyStorage(long capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public EnergyStorage(long capacity, long maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public EnergyStorage(long capacity, long maxReceive, long maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public EnergyStorage(long capacity, long maxReceive, long maxExtract, long energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    @Override
    public boolean canInsert() {
        return this.maxReceive > 0;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        if (!canInsert())
            return 0;

        long energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public boolean canWithdraw() {
        return this.maxExtract > 0;
    }

    @Override
    public long withdrawEnergy(long maxAmount, boolean simulate) {
        if (!canWithdraw())
            return 0;

        long energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public long getEnergyAmount() {
        return this.energy;
    }

    @Override
    public long getEnergyCapacity() {
        return this.capacity;
    }

    public void modifyEnergy(long energy) {
        this.energy = Mth.clamp(this.energy + energy, 0, this.getEnergyCapacity());
    }

    public Tag serializeNBT() {
        return LongTag.valueOf(this.getEnergyAmount());
    }

    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = intNbt.getAsInt();
    }

}
