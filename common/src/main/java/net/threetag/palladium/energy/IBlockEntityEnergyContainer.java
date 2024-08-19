package net.threetag.palladium.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IBlockEntityEnergyContainer {

    PalladiumEnergyStorage getEnergyStorage(@Nullable Direction side);

}
