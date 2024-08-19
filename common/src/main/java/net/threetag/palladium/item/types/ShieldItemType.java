package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.CodecUtils;

public class ShieldItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public ShieldItem parse(JsonObject json, Item.Properties properties) {
        int useDuration = GsonHelper.getAsInt(json, "use_duration", 72000);
        CodecUtils.SimpleIngredientSupplier repairIngredient = CodecUtils.SIMPLE_INGREDIENT_SUPPLIER.parse(JsonOps.INSTANCE, json.get("repair_ingredient")).getOrThrow();
        return new ExtShieldItem(useDuration, repairIngredient, properties);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Shield");

        builder.addProperty("use_duration", Integer.class)
                .description("Amount of ticks the shield can be actively held for")
                .fallback(72000)
                .exampleJson(new JsonPrimitive(72000));

        builder.addProperty("repair_ingredient", Ingredient.class)
                .description("The ingredient needed to repair the shield in an anvil. Can be null for making it non-repairable")
                .required()
                .exampleJson(CodecUtils.SIMPLE_INGREDIENT_SUPPLIER.encodeStart(JsonOps.INSTANCE, new CodecUtils.SimpleIngredientSupplierTag(ItemTags.PLANKS)).getOrThrow());
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.withDefaultNamespace("shield");
    }

    public static class ExtShieldItem extends ShieldItem {

        public final int useDuration;
        public final CodecUtils.SimpleIngredientSupplier repairIngredient;

        public ExtShieldItem(int useDuration, CodecUtils.SimpleIngredientSupplier repairIngredient, Properties properties) {
            super(properties);
            this.useDuration = useDuration;
            this.repairIngredient = repairIngredient;
        }

        @Override
        public int getUseDuration(ItemStack stack, LivingEntity entity) {
            return this.useDuration;
        }

        @Override
        public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
            return this.repairIngredient.get().test(repairCandidate);
        }

    }
}
