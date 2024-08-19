package net.threetag.palladium.energy.fabric;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.energy.PalladiumEnergyStorage;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class EnergyHelperImpl {

    @SuppressWarnings("unchecked")
    public static <T extends Number> DataComponentType<T> getItemComponent() {
        return (DataComponentType<T>) EnergyStorage.ENERGY_COMPONENT;
    }

    public static long getEnergyStoredInItem(ItemStack stack) {
        var storage = getFromItem(stack);
        return storage != null ? storage.getEnergyAmount() : 0;
    }

    @Nullable
    public static PalladiumEnergyStorage getFromItem(ItemStack stack) {
        var storage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withConstant(stack));
        return storage != null ? new FabricEnergyStorageWrapper(storage) : null;
    }

}
