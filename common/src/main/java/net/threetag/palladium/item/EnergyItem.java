package net.threetag.palladium.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.threetag.palladium.energy.EnergyHelper;
import net.threetag.palladium.util.Utils;

import java.util.List;

public class EnergyItem extends BaseEnergyItem {

    private static final int BAR_COLOR = Mth.color(0.9F, 0.1F, 0F);

    public EnergyItem(Properties properties, int capacity, int maxInput, int maxOutput) {
        super(properties, capacity, maxInput, maxOutput);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        var stored = EnergyHelper.getEnergyStoredInItem(stack);
        tooltipComponents.add(Component.translatable("item.palladium.flux_capacitor.desc",
                Component.literal(Utils.getFormattedNumber(stored)).withStyle(ChatFormatting.GOLD),
                Component.literal(Utils.getFormattedNumber(this.getEnergyCapacity(stack))).withStyle(ChatFormatting.GOLD)
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var storage = EnergyHelper.getFromItem(stack);
        return storage.getEnergyAmount() < storage.getEnergyCapacity();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var storage = EnergyHelper.getFromItem(stack);
        return Math.round(13F * storage.getEnergyAmount() / (float) storage.getEnergyCapacity());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    public ItemStack getFullyChargedInstance() {
        var filled = this.getDefaultInstance();
        filled.set(EnergyHelper.getItemComponent(), this.getEnergyCapacity(filled));
        return filled;
    }

}
