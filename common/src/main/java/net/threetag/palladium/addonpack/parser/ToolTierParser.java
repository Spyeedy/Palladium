package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.CodecUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ToolTierParser extends SimpleJsonResourceReloadListener {

    public static final Codec<SimpleToolTier> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    TagKey.codec(Registries.BLOCK).fieldOf("incorrect_block_for_drops").forGetter(Tier::getIncorrectBlocksForDrops),
                    ExtraCodecs.POSITIVE_INT.fieldOf("uses").forGetter(Tier::getUses),
                    Codec.floatRange(0, Float.MAX_VALUE).fieldOf("speed").forGetter(Tier::getSpeed),
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("attack_damage_bonus", 0F).forGetter(Tier::getAttackDamageBonus),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("enchantment_value", 0).forGetter(Tier::getEnchantmentValue),
                    CodecUtils.SIMPLE_INGREDIENT_SUPPLIER.fieldOf("repair_ingredient").forGetter(SimpleToolTier::getSimpleRepairIngredient)
            ).apply(instance, SimpleToolTier::new)
    );

    private static final Map<ResourceLocation, Tier> TIERS = new HashMap<>();

    public ToolTierParser() {
        super(AddonParser.GSON, "tool_tiers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        registerToolTier(ResourceLocation.withDefaultNamespace("wood"), Tiers.WOOD);
        registerToolTier(ResourceLocation.withDefaultNamespace("stone"), Tiers.STONE);
        registerToolTier(ResourceLocation.withDefaultNamespace("iron"), Tiers.IRON);
        registerToolTier(ResourceLocation.withDefaultNamespace("gold"), Tiers.GOLD);
        registerToolTier(ResourceLocation.withDefaultNamespace("diamond"), Tiers.DIAMOND);
        registerToolTier(ResourceLocation.withDefaultNamespace("netherite"), Tiers.NETHERITE);

        AtomicInteger i = new AtomicInteger();
        object.forEach((id, jsonElement) -> {
            try {
                JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
                SimpleToolTier toolTier = CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
                registerToolTier(id, toolTier);
                i.getAndIncrement();
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Error while parsing addonpack tool tier " + " '" + id + "'");

                CrashReportCategory reportCategory = crashReport.addCategory("Addon Tool Tier", 1);
                reportCategory.setDetail("Resource name", id);

                throw new ReportedException(crashReport);
            }
        });

        AddonPackLog.info("Registered " + i.get() + " addonpack tool tiers");
    }

    public static void registerToolTier(ResourceLocation id, Tier tier) {
        TIERS.put(id, tier);
    }

    public static Tier getToolTier(ResourceLocation id) {
        return TIERS.get(id);
    }

    public static Set<ResourceLocation> getIds() {
        return TIERS.keySet();
    }

    public static HTMLBuilder documentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder()
                .setDescription("Each tool type goes into a seperate file into the 'addon/[namespace]/tool_tiers' folder, which can then be used for custom tools (swords, pickaxes, etc.).");

        builder.addProperty("level", Integer.class)
                .description("Determines the mining level and what blocks can be harvested. For reference: iron has 2, diamond has 3. So obsidian can only be mined with tools with the level 3 or above, thats why you need a diamond pickaxe for it")
                .required().exampleJson(new JsonPrimitive(2));

        builder.addProperty("uses", Integer.class)
                .description("Determines the durability for tool. For reference: iron has 250, diamond has 1561")
                .required().exampleJson(new JsonPrimitive(420));

        builder.addProperty("speed", Float.class)
                .description("Determines the mining speed. For reference: iron has 6.0, diamond has 8.0")
                .required().exampleJson(new JsonPrimitive(6.9F));

        builder.addProperty("attack_damage_bonus", Float.class)
                .description("Determines the additional attack damage. For reference: iron has 2.0, diamond has 3.0")
                .required().exampleJson(new JsonPrimitive(2.5F));

        builder.addProperty("enchantment_value", Integer.class)
                .description("Determines the enchantibility of the item. For reference: iron has 9, diamond 10, gold 25.")
                .required().exampleJson(new JsonPrimitive(12));

        builder.addProperty("repair_ingredient", Ingredient.class)
                .description("Ingredient definition for repairing the item in an anvil. Can be defined like in recipes.")
                .fallback(Ingredient.EMPTY, "empty ingredient").exampleJson(CodecUtils.SIMPLE_INGREDIENT_SUPPLIER.encodeStart(JsonOps.INSTANCE, new CodecUtils.SimpleIngredientSupplierItem(Items.DIRT)).getOrThrow());

        return new HTMLBuilder(Palladium.id("tool_tiers"), "Tool Tiers").add(HTMLBuilder.heading("Tool Tiers")).addDocumentation(builder);
    }

    public static class SimpleToolTier implements Tier {

        private final TagKey<Block> incorrectBlocksForDrops;
        private final int uses;
        private final float speed;
        private final float attackDamageBonus;
        private final int enchantmentValue;
        @NotNull
        private final CodecUtils.SimpleIngredientSupplier repairIngredient;

        public SimpleToolTier(TagKey<Block> incorrectBlocksForDrops, int uses, float speed, float attackDamageBonus, int enchantmentValue, @NotNull CodecUtils.SimpleIngredientSupplier repairIngredient) {
            this.incorrectBlocksForDrops = incorrectBlocksForDrops;
            this.uses = uses;
            this.speed = speed;
            this.attackDamageBonus = attackDamageBonus;
            this.enchantmentValue = enchantmentValue;
            this.repairIngredient = repairIngredient;
        }

        @Override
        public int getUses() {
            return this.uses;
        }

        @Override
        public float getSpeed() {
            return this.speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return this.attackDamageBonus;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return this.incorrectBlocksForDrops;
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }

        public CodecUtils.SimpleIngredientSupplier getSimpleRepairIngredient() {
            return this.repairIngredient;
        }
    }

}
