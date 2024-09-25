package net.threetag.palladium.data.neoforge;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tags.PalladiumItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.threetag.palladium.item.PalladiumItems.*;

@SuppressWarnings("unchecked")
public class PalladiumItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {

    public PalladiumItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.ITEM, completableFuture, item -> item.builtInRegistryHolder().key(), Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PalladiumItemTags.VIBRATION_ABSORPTION_BOOTS).add(PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get());

        // Ore Blocks
        this.tag(PalladiumItemTags.ORES_LEAD).add(LEAD_ORE.get());
        this.tag(PalladiumItemTags.ORES_TITANIUM).add(TITANIUM_ORE.get());
        this.tag(PalladiumItemTags.ORES_VIBRANIUM).add(VIBRANIUM_ORE.get());
        this.tag(Tags.Items.ORES).addTags(PalladiumItemTags.ORES_LEAD, PalladiumItemTags.ORES_TITANIUM, PalladiumItemTags.ORES_VIBRANIUM);

        // Raw Ore Blocks
        this.tag(PalladiumItemTags.RAW_LEAD_BLOCKS).add(RAW_LEAD_BLOCK.get());
        this.tag(PalladiumItemTags.RAW_TITANIUM_BLOCKS).add(RAW_TITANIUM_BLOCK.get());
        this.tag(PalladiumItemTags.RAW_VIBRANIUM_BLOCKS).add(RAW_VIBRANIUM_BLOCK.get());
        this.tag(Tags.Items.STORAGE_BLOCKS).addTags(PalladiumItemTags.RAW_LEAD_BLOCKS, PalladiumItemTags.RAW_TITANIUM_BLOCKS, PalladiumItemTags.RAW_VIBRANIUM_BLOCKS);

        // Storage Blocks
        this.tag(PalladiumItemTags.STORAGE_BLOCKS_LEAD).add(LEAD_BLOCK.get());
        this.tag(PalladiumItemTags.STORAGE_BLOCKS_VIBRANIUM).add(VIBRANIUM_BLOCK.get());
        this.tag(Tags.Items.STORAGE_BLOCKS).addTags(PalladiumItemTags.STORAGE_BLOCKS_LEAD, PalladiumItemTags.STORAGE_BLOCKS_VIBRANIUM);

        // Ingots
        this.tag(PalladiumItemTags.LEAD_INGOTS).add(LEAD_INGOT.get());
        this.tag(PalladiumItemTags.VIBRANIUM_INGOTS).add(VIBRANIUM_INGOT.get());
        this.tag(Tags.Items.INGOTS).addTags(PalladiumItemTags.LEAD_INGOTS, PalladiumItemTags.VIBRANIUM_INGOTS);
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
