package net.threetag.palladium.mixin.fabric;

import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.item.BaseEnergyItem;
import org.spongepowered.asm.mixin.Mixin;
import team.reborn.energy.api.base.SimpleEnergyItem;

@SuppressWarnings("DataFlowIssue")
@Mixin(BaseEnergyItem.class)
public class EnergyItemMixin implements SimpleEnergyItem {

    @Override
    public long getEnergyCapacity(ItemStack stack) {
        return ((BaseEnergyItem) (Object) this).getEnergyCapacity(stack);
    }

    @Override
    public long getEnergyMaxInput(ItemStack stack) {
        return ((BaseEnergyItem) (Object) this).getEnergyMaxInput(stack);
    }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) {
        return ((BaseEnergyItem) (Object) this).getEnergyMaxOutput(stack);
    }
}
