package net.threetag.palladium.util.property_old;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

public class FluidTagProperty extends TagKeyProperty<Fluid> {

    public FluidTagProperty(String key) {
        super(key, Registries.FLUID);
    }

}
