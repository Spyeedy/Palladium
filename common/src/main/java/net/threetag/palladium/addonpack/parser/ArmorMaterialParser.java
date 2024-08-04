package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.ArmorMaterialBuilder;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.Objects;

public class ArmorMaterialParser extends AddonParser<ArmorMaterial> {

    public static final Codec<ArmorItem.Type> ARMOR_TYPE_CODEC = StringRepresentable.fromEnum(ArmorItem.Type::values);

//    public static final Codec<ArmorMaterialBuilder> BUILDER_CODEC = RecordCodecBuilder.create(instance ->
//            instance.group(
//                    Codec.unboundedMap(ARMOR_TYPE_CODEC, Codec.INT).fieldOf("defense").forGetter(ArmorMaterialBuilder::getDefense),
//                    Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("enchantment_value", 0).forGetter(ArmorMaterialBuilder::getEnchantmentValue),
//                    BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("equip_sound").forGetter(ArmorMaterialBuilder::getEquipSound),
//                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("toughness", 0F).forGetter(ArmorMaterialBuilder::getToughness),
//                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("knockback_resistance", 0F).forGetter(ArmorMaterialBuilder::getKnockbackResistance)
//            ).apply(instance, ArmorMaterialBuilder::new)
//            );

    public ArmorMaterialParser() {
        super(GSON, "armor_materials", Registries.ARMOR_MATERIAL);
    }

    @Override
    public AddonBuilder<ArmorMaterial> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");

        var builder = new ArmorMaterialBuilder(id)
                .setDefense(Codec.unboundedMap(ARMOR_TYPE_CODEC, Codec.INT).parse(JsonOps.INSTANCE, json.get("defense")).getOrThrow())
                .setEnchantmentValue(GsonUtil.getAsIntMin(json, "enchantment_value", 0))
                .setEquipSound(BuiltInRegistries.SOUND_EVENT.holderByNameCodec().parse(JsonOps.INSTANCE, json.get("equip_sound")).getOrThrow())
                .setToughness(GsonHelper.getAsFloat(json, "toughness", 0))
                .setKnockbackResistance(GsonHelper.getAsFloat(json, "knockback_resistance", 0));

        if (json.has("repair_ingredient")) {
            builder.setRepairIngredient(CodecUtils.SIMPLE_INGREDIENT_SUPPLIER.parse(JsonOps.INSTANCE, json.get("repair_ingredient")).getOrThrow());
        }

        return builder;
    }

    public static HTMLBuilder documentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder()
                .setDescription("Each armor material goes into a seperate file into the 'addon/[namespace]/armor_materials' folder, which can then be used for custom armor items/suit sets.");

        JsonObject slotProtections = new JsonObject();
        slotProtections.addProperty(ArmorItem.Type.HELMET.getName(), 3);
        slotProtections.addProperty(ArmorItem.Type.CHESTPLATE.getName(), 8);
        slotProtections.addProperty(ArmorItem.Type.LEGGINGS.getName(), 6);
        slotProtections.addProperty(ArmorItem.Type.BOOTS.getName(), 3);
        builder.addProperty("defense", JsonObject.class)
                .description("Protection values for the armor pieces, determines the defense value of each slot. For reference (Order: feet, legs, chest, head), iron has [2, 5, 6, 2], diamond is in the example.")
                .required().exampleJson(slotProtections);

        builder.addProperty("enchantment_value", Integer.class)
                .description("Determines the enchantibility of the item. For reference: iron has 9, diamond 10, gold 25.")
                .required().exampleJson(new JsonPrimitive(12));

        builder.addProperty("equip_sound", ResourceLocation.class)
                .description("Sound that is played when equipping the item into the slot.")
                .required().exampleJson(new JsonPrimitive(Objects.requireNonNull(BuiltInRegistries.SOUND_EVENT.getKey(SoundEvents.ARMOR_EQUIP_IRON.value())).toString()));

        builder.addProperty("toughness", Float.class)
                .description("Adds additional armor toughness. For reference: diamond has 2.0, netherite has 3.0, rest has 0.")
                .fallback(0F).exampleJson(new JsonPrimitive(1.5F));

        builder.addProperty("knockback_resistance", Float.class)
                .description("Adds knockback resistance. For reference: netherite has 0.1, rest has 0.")
                .fallback(0F).exampleJson(new JsonPrimitive(0.1F));

        builder.addProperty("repair_ingredient", Ingredient.class)
                .description("Ingredient definition for repairing the item in an anvil. Can be defined like in recipes.")
                .fallback(Ingredient.EMPTY, "empty ingredient").exampleJson(Ingredient.CODEC.encodeStart(JsonOps.COMPRESSED, Ingredient.of(ItemTags.DIRT)).getOrThrow());

        return new HTMLBuilder(Palladium.id("armor_materials"), "Armor Materials").add(HTMLBuilder.heading("Armor Materials")).addDocumentation(builder);
    }

}
