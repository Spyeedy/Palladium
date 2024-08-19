package net.threetag.palladium.block;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.PalladiumBlockUtil;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistryHolder;

public class PalladiumBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Palladium.MOD_ID, Registries.BLOCK);

    public static final RegistryHolder<Block, Block> LEAD_ORE = BLOCKS.register("lead_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryHolder<Block, Block> DEEPSLATE_LEAD_ORE = BLOCKS.register("deepslate_lead_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.ofFullCopy(LEAD_ORE.value()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistryHolder<Block, Block> TITANIUM_ORE = BLOCKS.register("titanium_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).requiresCorrectToolForDrops()));
    public static final RegistryHolder<Block, Block> VIBRANIUM_ORE = BLOCKS.register("vibranium_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).requiresCorrectToolForDrops().lightLevel(value -> 4)));

    public static final RegistryHolder<Block, Block> REDSTONE_FLUX_CRYSTAL_GEODE = BLOCKS.register("redstone_flux_crystal_geode", () -> new FluxCrystalGeodeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE).lightLevel(value -> 9)));
    public static final RegistryHolder<Block, Block> DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE = BLOCKS.register("deepslate_redstone_flux_crystal_geode", () -> new FluxCrystalGeodeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_REDSTONE_ORE).lightLevel(value -> 9)));
    public static final RegistryHolder<Block, Block> REDSTONE_FLUX_CRYSTAL_CLUSTER = BLOCKS.register("redstone_flux_crystal_cluster", () -> new RedstoneFluxCrystalClusterBlock(7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).mapColor(MapColor.COLOR_RED)));
    public static final RegistryHolder<Block, Block> LARGE_REDSTONE_FLUX_CRYSTAL_BUD = BLOCKS.register("large_redstone_flux_crystal_bud", () -> new RedstoneFluxCrystalClusterBlock(5, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD).mapColor(MapColor.COLOR_RED)));
    public static final RegistryHolder<Block, Block> MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD = BLOCKS.register("medium_redstone_flux_crystal_bud", () -> new RedstoneFluxCrystalClusterBlock(4, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD).mapColor(MapColor.COLOR_RED)));
    public static final RegistryHolder<Block, Block> SMALL_REDSTONE_FLUX_CRYSTAL_BUD = BLOCKS.register("small_redstone_flux_crystal_bud", () -> new RedstoneFluxCrystalClusterBlock(3, 4, BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD).mapColor(MapColor.COLOR_RED)));

    public static final RegistryHolder<Block, Block> LEAD_BLOCK = BLOCKS.register("lead_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(4.0F, 12.0F)));
    public static final RegistryHolder<Block, Block> VIBRANIUM_BLOCK = BLOCKS.register("vibranium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(15.0F, 18.0F)));

    public static final RegistryHolder<Block, Block> RAW_LEAD_BLOCK = BLOCKS.register("raw_lead_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));
    public static final RegistryHolder<Block, Block> RAW_TITANIUM_BLOCK = BLOCKS.register("raw_titanium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));
    public static final RegistryHolder<Block, Block> RAW_VIBRANIUM_BLOCK = BLOCKS.register("raw_vibranium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));

    public static final RegistryHolder<Block, FlowerBlock> HEART_SHAPED_HERB = BLOCKS.register("heart_shaped_herb", () -> new FlowerBlock(MobEffects.DAMAGE_RESISTANCE, 4, BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.GRASS)));
    public static final RegistryHolder<Block, Block> POTTED_HEART_SHAPED_HERB = BLOCKS.register("potted_heart_shaped_herb", () -> PalladiumBlockUtil.createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HEART_SHAPED_HERB, BlockBehaviour.Properties.of().instabreak().noOcclusion()));

}
