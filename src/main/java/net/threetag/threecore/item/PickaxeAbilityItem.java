package net.threetag.threecore.item;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.ability.AbilityGenerator;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.ability.IAbilityProvider;
import net.threetag.threecore.capability.ItemAbilityContainerProvider;

import javax.annotation.Nullable;
import java.util.List;

public class PickaxeAbilityItem extends PickaxeItem implements IAbilityProvider {

    private List<AbilityGenerator> abilityGenerators;
    private List<ITextComponent> description;

    public PickaxeAbilityItem(IItemTier itemTier, int attackDamage, float attackSpeed, Properties properties) {
        super(itemTier, attackDamage, attackSpeed, properties);
    }

    public PickaxeAbilityItem setAbilities(List<AbilityGenerator> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public PickaxeAbilityItem addAbility(AbilityGenerator abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    public PickaxeAbilityItem setDescription(List<ITextComponent> description) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> this.description = description);
        return this;
    }

    public PickaxeAbilityItem addDescriptionLine(ITextComponent line) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            if (this.description == null)
                this.description = Lists.newArrayList();
            this.description.add(line);
        });
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.description != null)
            tooltip.addAll(this.description);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemAbilityContainerProvider(stack);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }

}
