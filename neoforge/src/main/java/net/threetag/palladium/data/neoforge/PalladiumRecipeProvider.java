package net.threetag.palladium.data.neoforge;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tags.PalladiumItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("NullableProblems")
public class PalladiumRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final ImmutableList<ItemLike> LEAD_SMELTABLES = ImmutableList.of(PalladiumItems.RAW_LEAD.get(), PalladiumItems.LEAD_ORE.get(), PalladiumItems.DEEPSLATE_LEAD_ORE.get());
    private static final ImmutableList<ItemLike> VIBRANIUM_SMELTABLES = ImmutableList.of(PalladiumItems.RAW_VIBRANIUM.get(), PalladiumItems.VIBRANIUM_ORE.get());

    public PalladiumRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PalladiumItems.SUIT_STAND.get()).pattern(" B ").pattern("SBS").pattern("SXS").define('B', PalladiumItemTags.QUARTZ).define('S', Ingredient.of(Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB)).define('X', Blocks.SMOOTH_STONE_SLAB).unlockedBy(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND)).save(output);

        oreSmelting(output, LEAD_SMELTABLES, RecipeCategory.MISC, PalladiumItems.LEAD_INGOT.get(), 0.7F, 200, "lead_ingot");
        oreSmelting(output, VIBRANIUM_SMELTABLES, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT.get(), 1F, 600, "vibranium_ingot");

        oreBlasting(output, LEAD_SMELTABLES, RecipeCategory.MISC, PalladiumItems.LEAD_INGOT.get(), 0.7F, 100, "lead_ingot");
        oreBlasting(output, VIBRANIUM_SMELTABLES, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT.get(), 1F, 300, "vibranium_ingot");

        nineBlockStorageRecipesRecipesWithCustomUnpacking(output, RecipeCategory.MISC, PalladiumItems.LEAD_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.LEAD_BLOCK.get(), "lead_ingot_from_lead_block", "lead_ingot");
        nineBlockStorageRecipesRecipesWithCustomUnpacking(output, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.VIBRANIUM_BLOCK.get(), "vibranium_ingot_from_vibranium_block", "vibranium_ingot");

        nineBlockStorageRecipes(output, RecipeCategory.MISC, PalladiumItems.RAW_LEAD.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.RAW_LEAD_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, PalladiumItems.RAW_TITANIUM.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.RAW_TITANIUM_BLOCK.get());
        nineBlockStorageRecipes(output, RecipeCategory.MISC, PalladiumItems.RAW_VIBRANIUM.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.RAW_VIBRANIUM_BLOCK.get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PURPLE_DYE).requires(PalladiumBlocks.HEART_SHAPED_HERB.get()).group("purple_dye").unlockedBy("has_flower", has(PalladiumBlocks.HEART_SHAPED_HERB.get())).save(output, Palladium.id("purple_dye_from_heart_shaped_herb"));

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.LEATHER_BOOTS), Ingredient.of(PalladiumItemTags.VIBRANIUM_INGOTS), RecipeCategory.COMBAT, PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get()).unlocks(getHasName(PalladiumItems.VIBRANIUM_INGOT.get()), has(PalladiumItemTags.VIBRANIUM_INGOTS)).save(output, Palladium.id("vibranium_weave_boots_smithing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.LEAD_CIRCUIT.get()).pattern("II").pattern("LL").pattern("GG").define('I', PalladiumItemTags.IRON_INGOTS).define('L', PalladiumItemTags.LEAD_INGOTS).define('G', PalladiumItemTags.GOLD_INGOTS).unlockedBy(getHasName(PalladiumItems.LEAD_INGOT.get()), has(PalladiumItemTags.LEAD_INGOTS)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.QUARTZ_CIRCUIT.get()).pattern("II").pattern("QQ").pattern("CC").define('I', PalladiumItemTags.IRON_INGOTS).define('Q', PalladiumItemTags.QUARTZ).define('C', PalladiumItemTags.COPPER_INGOTS).unlockedBy(getHasName(Items.QUARTZ), has(PalladiumItemTags.QUARTZ)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.VIBRANIUM_CIRCUIT.get()).pattern("II").pattern("VV").pattern("DD").define('I', PalladiumItemTags.IRON_INGOTS).define('V', PalladiumItemTags.VIBRANIUM_INGOTS).define('D', PalladiumItemTags.DIAMONDS).unlockedBy(getHasName(PalladiumItems.VIBRANIUM_INGOT.get()), has(PalladiumItemTags.VIBRANIUM_INGOTS)).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.LEAD_FLUX_CAPACITOR.get()).pattern("RLR").pattern("GCG").pattern("LRL").define('R', PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).define('L', PalladiumItemTags.LEAD_INGOTS).define('G', PalladiumItemTags.GOLD_INGOTS).define('C', PalladiumItems.LEAD_CIRCUIT.get()).unlockedBy(getHasName(PalladiumItems.LEAD_INGOT.get()), has(PalladiumItemTags.LEAD_INGOTS)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.QUARTZ_FLUX_CAPACITOR.get()).pattern("RQR").pattern("CFC").pattern("QRQ").define('R', PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).define('Q', PalladiumItemTags.QUARTZ).define('C', PalladiumItemTags.COPPER_INGOTS).define('F', PalladiumItems.LEAD_FLUX_CAPACITOR.get()).unlockedBy(getHasName(Items.QUARTZ), has(PalladiumItemTags.QUARTZ)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.VIBRANIUM_FLUX_CAPACITOR.get()).pattern("RVR").pattern("DFD").pattern("VRV").define('R', PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).define('V', PalladiumItemTags.VIBRANIUM_INGOTS).define('D', PalladiumItemTags.DIAMONDS).define('F', PalladiumItems.QUARTZ_FLUX_CAPACITOR.get()).unlockedBy(getHasName(PalladiumItems.VIBRANIUM_INGOT.get()), has(PalladiumItemTags.VIBRANIUM_INGOTS)).save(output);
    }

    protected static void oreSmelting(RecipeOutput output, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTIme, String group) {
        oreCooking(output, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredients, category, result, experience, cookingTIme, group, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput output, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(output, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredients, category, result, experience, cookingTime, group, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput output, RecipeSerializer<T> cookingSerializer, AbstractCookingRecipe.Factory<T> factory, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String recipeName) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, experience, cookingTime, cookingSerializer, factory).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(output, Palladium.id(getItemName(result) + recipeName + "_" + getItemName(itemlike)));
        }
    }

    protected static void nineBlockStorageRecipes(RecipeOutput output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed) {
        nineBlockStorageRecipes(output, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), null, getSimpleRecipeName(unpacked), null);
    }

    protected static void nineBlockStorageRecipesWithCustomPacking(RecipeOutput output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String packedName, String packedGroup) {
        nineBlockStorageRecipes(output, unpackedCategory, unpacked, packedCategory, packed, packedName, packedGroup, getSimpleRecipeName(unpacked), null);
    }

    protected static void nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeOutput output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String unpackedName, String unpackedGroup) {
        nineBlockStorageRecipes(output, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), null, unpackedName, unpackedGroup);
    }

    protected static void nineBlockStorageRecipes(RecipeOutput output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String packedName, @javax.annotation.Nullable String packedGroup, String unpackedName, @javax.annotation.Nullable String unpackedGroup) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9).requires(packed).group(unpackedGroup).unlockedBy(getHasName(packed), has(packed)).save(output, Palladium.id(unpackedName));
        ShapedRecipeBuilder.shaped(packedCategory, packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").group(packedGroup).unlockedBy(getHasName(unpacked), has(unpacked)).save(output, Palladium.id(packedName));
    }
}
