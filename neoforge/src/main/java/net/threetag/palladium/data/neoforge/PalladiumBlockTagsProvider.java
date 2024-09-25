package net.threetag.palladium.data.neoforge;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.tags.PalladiumBlockTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.threetag.palladium.block.PalladiumBlocks.*;

@SuppressWarnings("unchecked")
public class PalladiumBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {

    public PalladiumBlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.BLOCK, completableFuture, block -> block.builtInRegistryHolder().key(), Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PalladiumBlockTags.PREVENTS_INTANGIBILITY).add(Blocks.BEDROCK);

        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(LEAD_BLOCK.get(), VIBRANIUM_BLOCK.get());

        // Ore Blocks
        this.tag(PalladiumBlockTags.ORES_LEAD).add(LEAD_ORE.get());
        this.tag(PalladiumBlockTags.ORES_TITANIUM).add(TITANIUM_ORE.get());
        this.tag(PalladiumBlockTags.ORES_VIBRANIUM).add(VIBRANIUM_ORE.get());
        this.tag(Tags.Blocks.ORES).addTags(PalladiumBlockTags.ORES_LEAD, PalladiumBlockTags.ORES_TITANIUM, PalladiumBlockTags.ORES_VIBRANIUM);

        // Raw Ore Blocks
        this.tag(PalladiumBlockTags.RAW_LEAD_BLOCKS).add(RAW_LEAD_BLOCK.get());
        this.tag(PalladiumBlockTags.RAW_TITANIUM_BLOCKS).add(RAW_TITANIUM_BLOCK.get());
        this.tag(PalladiumBlockTags.RAW_VIBRANIUM_BLOCKS).add(RAW_VIBRANIUM_BLOCK.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(PalladiumBlockTags.RAW_LEAD_BLOCKS, PalladiumBlockTags.RAW_TITANIUM_BLOCKS, PalladiumBlockTags.RAW_VIBRANIUM_BLOCKS);

        // Storage Blocks
        this.tag(PalladiumBlockTags.STORAGE_BLOCKS_LEAD).add(LEAD_BLOCK.get());
        this.tag(PalladiumBlockTags.STORAGE_BLOCKS_VIBRANIUM).add(VIBRANIUM_BLOCK.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(PalladiumBlockTags.STORAGE_BLOCKS_LEAD, PalladiumBlockTags.STORAGE_BLOCKS_VIBRANIUM);

        // Harvest Tools
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get(), TITANIUM_ORE.get(), VIBRANIUM_ORE.get(), LEAD_BLOCK.get(), VIBRANIUM_BLOCK.get(), RAW_LEAD_BLOCK.get(), RAW_TITANIUM_BLOCK.get(), RAW_VIBRANIUM_BLOCK.get(), REDSTONE_FLUX_CRYSTAL_GEODE.get(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(LEAD_ORE.get(), LEAD_BLOCK.get(), RAW_LEAD_BLOCK.get(), REDSTONE_FLUX_CRYSTAL_GEODE.get(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get());
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(TITANIUM_ORE.get(), RAW_TITANIUM_BLOCK.get());
        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(VIBRANIUM_ORE.get(), VIBRANIUM_BLOCK.get(), RAW_VIBRANIUM_BLOCK.get());
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
