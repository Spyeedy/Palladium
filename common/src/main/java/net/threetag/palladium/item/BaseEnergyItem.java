package net.threetag.palladium.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BaseEnergyItem extends Item {

    private final int capacity, maxInput, maxOutput;

    public BaseEnergyItem(Properties properties, int capacity, int maxInput, int maxOutput) {
        super(properties);
        this.capacity = capacity;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    public int getEnergyCapacity(ItemStack stack) {
        return this.capacity;
    }

    public int getEnergyMaxInput(ItemStack stack) {
        return this.maxInput;
    }

    public int getEnergyMaxOutput(ItemStack stack) {
        return this.maxOutput;
    }

}
