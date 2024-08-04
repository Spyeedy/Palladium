package net.threetag.palladium.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Used for getting material in addonpack block jsons
 */
public class BlockMaterialRegistry {

    private static final Map<ResourceLocation, MapColor> MATERIAL_COLORS = new HashMap<>();
    private static final Map<ResourceLocation, SoundType> SOUND_TYPES = new HashMap<>();

    public static void registerColor(ResourceLocation id, MapColor materialColor) {
        MATERIAL_COLORS.put(id, materialColor);
    }

    @Nullable
    public static MapColor getColor(ResourceLocation id) {
        return MATERIAL_COLORS.get(id);
    }

    public static Set<ResourceLocation> getAllColorIds() {
        return MATERIAL_COLORS.keySet();
    }

    public static Collection<MapColor> getAllColors() {
        return MATERIAL_COLORS.values();
    }

    public static void registerSoundType(ResourceLocation id, SoundType soundType) {
        SOUND_TYPES.put(id, soundType);
    }

    @Nullable
    public static SoundType getSoundType(ResourceLocation id) {
        return SOUND_TYPES.get(id);
    }

    public static Set<ResourceLocation> getAllSoundTypeIds() {
        return SOUND_TYPES.keySet();
    }

    public static Collection<SoundType> getAllSoundTypes() {
        return SOUND_TYPES.values();
    }

    static {
        registerColor(ResourceLocation.withDefaultNamespace("none"), MapColor.NONE);
        registerColor(ResourceLocation.withDefaultNamespace("grass"), MapColor.GRASS);
        registerColor(ResourceLocation.withDefaultNamespace("sand"), MapColor.SAND);
        registerColor(ResourceLocation.withDefaultNamespace("wool"), MapColor.WOOL);
        registerColor(ResourceLocation.withDefaultNamespace("fire"), MapColor.FIRE);
        registerColor(ResourceLocation.withDefaultNamespace("ice"), MapColor.ICE);
        registerColor(ResourceLocation.withDefaultNamespace("metal"), MapColor.METAL);
        registerColor(ResourceLocation.withDefaultNamespace("plant"), MapColor.PLANT);
        registerColor(ResourceLocation.withDefaultNamespace("snow"), MapColor.SNOW);
        registerColor(ResourceLocation.withDefaultNamespace("clay"), MapColor.CLAY);
        registerColor(ResourceLocation.withDefaultNamespace("dirt"), MapColor.DIRT);
        registerColor(ResourceLocation.withDefaultNamespace("stone"), MapColor.STONE);
        registerColor(ResourceLocation.withDefaultNamespace("water"), MapColor.WATER);
        registerColor(ResourceLocation.withDefaultNamespace("wood"), MapColor.WOOD);
        registerColor(ResourceLocation.withDefaultNamespace("quartz"), MapColor.QUARTZ);
        registerColor(ResourceLocation.withDefaultNamespace("color_orange"), MapColor.COLOR_ORANGE);
        registerColor(ResourceLocation.withDefaultNamespace("color_magenta"), MapColor.COLOR_MAGENTA);
        registerColor(ResourceLocation.withDefaultNamespace("color_light_blue"), MapColor.COLOR_LIGHT_BLUE);
        registerColor(ResourceLocation.withDefaultNamespace("color_yellow"), MapColor.COLOR_YELLOW);
        registerColor(ResourceLocation.withDefaultNamespace("color_light_green"), MapColor.COLOR_LIGHT_GREEN);
        registerColor(ResourceLocation.withDefaultNamespace("color_pink"), MapColor.COLOR_PINK);
        registerColor(ResourceLocation.withDefaultNamespace("color_gray"), MapColor.COLOR_GRAY);
        registerColor(ResourceLocation.withDefaultNamespace("color_light_gray"), MapColor.COLOR_LIGHT_GRAY);
        registerColor(ResourceLocation.withDefaultNamespace("color_cyan"), MapColor.COLOR_CYAN);
        registerColor(ResourceLocation.withDefaultNamespace("color_purple"), MapColor.COLOR_PURPLE);
        registerColor(ResourceLocation.withDefaultNamespace("color_blue"), MapColor.COLOR_BLUE);
        registerColor(ResourceLocation.withDefaultNamespace("color_brown"), MapColor.COLOR_BROWN);
        registerColor(ResourceLocation.withDefaultNamespace("color_green"), MapColor.COLOR_GREEN);
        registerColor(ResourceLocation.withDefaultNamespace("color_red"), MapColor.COLOR_RED);
        registerColor(ResourceLocation.withDefaultNamespace("color_black"), MapColor.COLOR_BLACK);
        registerColor(ResourceLocation.withDefaultNamespace("gold"), MapColor.GOLD);
        registerColor(ResourceLocation.withDefaultNamespace("diamond"), MapColor.DIAMOND);
        registerColor(ResourceLocation.withDefaultNamespace("lapis"), MapColor.LAPIS);
        registerColor(ResourceLocation.withDefaultNamespace("emerald"), MapColor.EMERALD);
        registerColor(ResourceLocation.withDefaultNamespace("podzol"), MapColor.PODZOL);
        registerColor(ResourceLocation.withDefaultNamespace("nether"), MapColor.NETHER);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_orange"), MapColor.TERRACOTTA_ORANGE);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_magenta"), MapColor.TERRACOTTA_MAGENTA);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_light_blue"), MapColor.TERRACOTTA_LIGHT_BLUE);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_yellow"), MapColor.TERRACOTTA_YELLOW);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_light_green"), MapColor.TERRACOTTA_LIGHT_GREEN);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_pink"), MapColor.TERRACOTTA_PINK);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_gray"), MapColor.TERRACOTTA_GRAY);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_light_gray"), MapColor.TERRACOTTA_LIGHT_GRAY);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_cyan"), MapColor.TERRACOTTA_CYAN);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_purple"), MapColor.TERRACOTTA_PURPLE);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_blue"), MapColor.TERRACOTTA_BLUE);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_brown"), MapColor.TERRACOTTA_BROWN);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_green"), MapColor.TERRACOTTA_GREEN);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_red"), MapColor.TERRACOTTA_RED);
        registerColor(ResourceLocation.withDefaultNamespace("terracotta_black"), MapColor.TERRACOTTA_BLACK);
        registerColor(ResourceLocation.withDefaultNamespace("crimson_nylium"), MapColor.CRIMSON_NYLIUM);
        registerColor(ResourceLocation.withDefaultNamespace("crimson_stem"), MapColor.CRIMSON_STEM);
        registerColor(ResourceLocation.withDefaultNamespace("crimson_hyphae"), MapColor.CRIMSON_HYPHAE);
        registerColor(ResourceLocation.withDefaultNamespace("warped_nylium"), MapColor.WARPED_NYLIUM);
        registerColor(ResourceLocation.withDefaultNamespace("warped_stem"), MapColor.WARPED_STEM);
        registerColor(ResourceLocation.withDefaultNamespace("warped_hyphae"), MapColor.WARPED_HYPHAE);
        registerColor(ResourceLocation.withDefaultNamespace("warped_wart_block"), MapColor.WARPED_WART_BLOCK);
        registerColor(ResourceLocation.withDefaultNamespace("deepslate"), MapColor.DEEPSLATE);
        registerColor(ResourceLocation.withDefaultNamespace("raw_iron"), MapColor.RAW_IRON);
        registerColor(ResourceLocation.withDefaultNamespace("glow_lichen"), MapColor.GLOW_LICHEN);

        registerSoundType(ResourceLocation.withDefaultNamespace("wood"), SoundType.WOOD);
        registerSoundType(ResourceLocation.withDefaultNamespace("gravel"), SoundType.GRAVEL);
        registerSoundType(ResourceLocation.withDefaultNamespace("grass"), SoundType.GRASS);
        registerSoundType(ResourceLocation.withDefaultNamespace("lily_pad"), SoundType.LILY_PAD);
        registerSoundType(ResourceLocation.withDefaultNamespace("stone"), SoundType.STONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("metal"), SoundType.METAL);
        registerSoundType(ResourceLocation.withDefaultNamespace("glass"), SoundType.GLASS);
        registerSoundType(ResourceLocation.withDefaultNamespace("wool"), SoundType.WOOL);
        registerSoundType(ResourceLocation.withDefaultNamespace("sand"), SoundType.SAND);
        registerSoundType(ResourceLocation.withDefaultNamespace("snow"), SoundType.SNOW);
        registerSoundType(ResourceLocation.withDefaultNamespace("powder_snow"), SoundType.POWDER_SNOW);
        registerSoundType(ResourceLocation.withDefaultNamespace("ladder"), SoundType.LADDER);
        registerSoundType(ResourceLocation.withDefaultNamespace("anvil"), SoundType.ANVIL);
        registerSoundType(ResourceLocation.withDefaultNamespace("slime_block"), SoundType.SLIME_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("honey_block"), SoundType.HONEY_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("wet_grass"), SoundType.WET_GRASS);
        registerSoundType(ResourceLocation.withDefaultNamespace("coral_block"), SoundType.CORAL_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("bamboo"), SoundType.BAMBOO);
        registerSoundType(ResourceLocation.withDefaultNamespace("bamboo_sapling"), SoundType.BAMBOO_SAPLING);
        registerSoundType(ResourceLocation.withDefaultNamespace("scaffolding"), SoundType.SCAFFOLDING);
        registerSoundType(ResourceLocation.withDefaultNamespace("sweet_berry_bush"), SoundType.SWEET_BERRY_BUSH);
        registerSoundType(ResourceLocation.withDefaultNamespace("crop"), SoundType.CROP);
        registerSoundType(ResourceLocation.withDefaultNamespace("hard_crop"), SoundType.HARD_CROP);
        registerSoundType(ResourceLocation.withDefaultNamespace("vine"), SoundType.VINE);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_wart"), SoundType.NETHER_WART);
        registerSoundType(ResourceLocation.withDefaultNamespace("lantern"), SoundType.LANTERN);
        registerSoundType(ResourceLocation.withDefaultNamespace("stem"), SoundType.STEM);
        registerSoundType(ResourceLocation.withDefaultNamespace("nylium"), SoundType.NYLIUM);
        registerSoundType(ResourceLocation.withDefaultNamespace("fungus"), SoundType.FUNGUS);
        registerSoundType(ResourceLocation.withDefaultNamespace("root"), SoundType.ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("shroomlight"), SoundType.SHROOMLIGHT);
        registerSoundType(ResourceLocation.withDefaultNamespace("weeping_vines"), SoundType.WEEPING_VINES);
        registerSoundType(ResourceLocation.withDefaultNamespace("twisting_vines"), SoundType.TWISTING_VINES);
        registerSoundType(ResourceLocation.withDefaultNamespace("soul_sand"), SoundType.SOUL_SAND);
        registerSoundType(ResourceLocation.withDefaultNamespace("soul_soil"), SoundType.SOUL_SOIL);
        registerSoundType(ResourceLocation.withDefaultNamespace("basalt"), SoundType.BASALT);
        registerSoundType(ResourceLocation.withDefaultNamespace("wart_block"), SoundType.WART_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("netherrack"), SoundType.NETHERRACK);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_bricks"), SoundType.NETHER_BRICKS);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_sprouts"), SoundType.NETHER_SPROUTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_ore"), SoundType.NETHER_ORE);
        registerSoundType(ResourceLocation.withDefaultNamespace("bone_block"), SoundType.BONE_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("netherite_block"), SoundType.NETHERITE_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("ancient_debris"), SoundType.ANCIENT_DEBRIS);
        registerSoundType(ResourceLocation.withDefaultNamespace("lodestone"), SoundType.LODESTONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("chain"), SoundType.CHAIN);
        registerSoundType(ResourceLocation.withDefaultNamespace("nether_gold_ore"), SoundType.NETHER_GOLD_ORE);
        registerSoundType(ResourceLocation.withDefaultNamespace("gilded_blackstone"), SoundType.GILDED_BLACKSTONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("candle"), SoundType.CANDLE);
        registerSoundType(ResourceLocation.withDefaultNamespace("amethyst"), SoundType.AMETHYST);
        registerSoundType(ResourceLocation.withDefaultNamespace("amethyst_cluster"), SoundType.AMETHYST_CLUSTER);
        registerSoundType(ResourceLocation.withDefaultNamespace("small_amethyst_bud"), SoundType.SMALL_AMETHYST_BUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("medium_amethyst_bud"), SoundType.MEDIUM_AMETHYST_BUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("large_amethyst_bud"), SoundType.LARGE_AMETHYST_BUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("tuff"), SoundType.TUFF);
        registerSoundType(ResourceLocation.withDefaultNamespace("calcite"), SoundType.CALCITE);
        registerSoundType(ResourceLocation.withDefaultNamespace("dripstone_block"), SoundType.DRIPSTONE_BLOCK);
        registerSoundType(ResourceLocation.withDefaultNamespace("pointed_dripstone"), SoundType.POINTED_DRIPSTONE);
        registerSoundType(ResourceLocation.withDefaultNamespace("copper"), SoundType.COPPER);
        registerSoundType(ResourceLocation.withDefaultNamespace("cave_vines"), SoundType.CAVE_VINES);
        registerSoundType(ResourceLocation.withDefaultNamespace("spore_blossom"), SoundType.SPORE_BLOSSOM);
        registerSoundType(ResourceLocation.withDefaultNamespace("azalea"), SoundType.AZALEA);
        registerSoundType(ResourceLocation.withDefaultNamespace("flowering_azalea"), SoundType.FLOWERING_AZALEA);
        registerSoundType(ResourceLocation.withDefaultNamespace("moss_carpet"), SoundType.MOSS_CARPET);
        registerSoundType(ResourceLocation.withDefaultNamespace("moss"), SoundType.MOSS);
        registerSoundType(ResourceLocation.withDefaultNamespace("big_dripleaf"), SoundType.BIG_DRIPLEAF);
        registerSoundType(ResourceLocation.withDefaultNamespace("small_dripleaf"), SoundType.SMALL_DRIPLEAF);
        registerSoundType(ResourceLocation.withDefaultNamespace("rooted_dirt"), SoundType.ROOTED_DIRT);
        registerSoundType(ResourceLocation.withDefaultNamespace("hanging_roots"), SoundType.HANGING_ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("azelea_leaves"), SoundType.AZALEA_LEAVES);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_sensor"), SoundType.SCULK_SENSOR);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_catalyst"), SoundType.SCULK_CATALYST);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk"), SoundType.SCULK);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_vein"), SoundType.SCULK_VEIN);
        registerSoundType(ResourceLocation.withDefaultNamespace("sculk_shrieker"), SoundType.SCULK_SHRIEKER);
        registerSoundType(ResourceLocation.withDefaultNamespace("glow_lichen"), SoundType.GLOW_LICHEN);
        registerSoundType(ResourceLocation.withDefaultNamespace("deepslate"), SoundType.DEEPSLATE);
        registerSoundType(ResourceLocation.withDefaultNamespace("deepslate_bricks"), SoundType.DEEPSLATE_BRICKS);
        registerSoundType(ResourceLocation.withDefaultNamespace("deepslate_tiles"), SoundType.DEEPSLATE_TILES);
        registerSoundType(ResourceLocation.withDefaultNamespace("polished_deepslate"), SoundType.POLISHED_DEEPSLATE);
        registerSoundType(ResourceLocation.withDefaultNamespace("froglight"), SoundType.FROGLIGHT);
        registerSoundType(ResourceLocation.withDefaultNamespace("frogspawn"), SoundType.FROGSPAWN);
        registerSoundType(ResourceLocation.withDefaultNamespace("mangrove_roots"), SoundType.MANGROVE_ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("muddy_mangrove_roots"), SoundType.MUDDY_MANGROVE_ROOTS);
        registerSoundType(ResourceLocation.withDefaultNamespace("mud"), SoundType.MUD);
        registerSoundType(ResourceLocation.withDefaultNamespace("mud_bricks"), SoundType.MUD_BRICKS);
        registerSoundType(ResourceLocation.withDefaultNamespace("packed_mud"), SoundType.PACKED_MUD);
    }

}
