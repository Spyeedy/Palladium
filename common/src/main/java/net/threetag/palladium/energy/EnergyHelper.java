package net.threetag.palladium.energy;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnergyHelper {

    @ExpectPlatform
    public static <T extends Number> DataComponentType<T> getItemComponent() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static long getEnergyStoredInItem(ItemStack stack) {
        throw new AssertionError();
    }

    @Nullable
    @ExpectPlatform
    public static PalladiumEnergyStorage getFromItem(ItemStack stack) {
        throw new AssertionError();
    }

}
