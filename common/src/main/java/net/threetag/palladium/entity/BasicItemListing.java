package net.threetag.palladium.entity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BasicItemListing implements VillagerTrades.ItemListing {

    protected final ItemCost price;
    protected final ItemCost price2;
    protected final ItemStack forSale;
    protected final int maxTrades;
    protected final int xp;
    protected final float priceMult;

    public BasicItemListing(ItemCost price, ItemCost price2, ItemStack forSale, int maxTrades, int xp, float priceMult) {
        this.price = price;
        this.price2 = price2;
        this.forSale = forSale;
        this.maxTrades = maxTrades;
        this.xp = xp;
        this.priceMult = priceMult;
    }

    public BasicItemListing(ItemCost price, ItemStack forSale, int maxTrades, int xp, float priceMult) {
        this(price, null, forSale, maxTrades, xp, priceMult);
    }

    public BasicItemListing(int emeralds, ItemStack forSale, int maxTrades, int xp, float mult) {
        this(new ItemCost(Items.EMERALD, emeralds), forSale, maxTrades, xp, mult);
    }

    public BasicItemListing(int emeralds, ItemStack forSale, int maxTrades, int xp) {
        this(new ItemCost(Items.EMERALD, emeralds), forSale, maxTrades, xp, 1);
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        return new MerchantOffer(this.price, Optional.ofNullable(this.price2), this.forSale, this.maxTrades, this.xp, this.priceMult);
    }
}
