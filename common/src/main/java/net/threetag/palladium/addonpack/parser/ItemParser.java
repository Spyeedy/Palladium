package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.ItemBuilder;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.DocumentedConfigurable;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.*;
import net.threetag.palladium.item.types.*;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;

import java.util.*;

public class ItemParser extends AddonParser<Item> {

    public static final ResourceLocation FALLBACK_SERIALIZER = Palladium.id("default");
    private static final Map<ResourceLocation, ItemTypeSerializer> TYPE_SERIALIZERS = new LinkedHashMap<>();
    public final Map<ResourceLocation, List<PlacedTabPlacement>> autoRegisteredBlockItems = new HashMap<>();

    public ItemParser() {
        super(GSON, "items", Registries.ITEM);
    }

    @Override
    public void injectJsons(Map<ResourceLocation, JsonElement> map) {
        for (ResourceLocation id : autoRegisteredBlockItems.keySet()) {
            var json = new JsonObject();
            json.addProperty("type", "palladium:block_item");
            json.addProperty("block", id.toString());
            JsonArray jsonArray = new JsonArray();
            if (this.autoRegisteredBlockItems.get(id) != null) {
                for (PlacedTabPlacement tabPlacement : this.autoRegisteredBlockItems.get(id)) {
                    jsonArray.add(tabPlacement.toJson());
                }
            }
            json.add("creative_mode_tab", jsonArray);
            map.put(id, json);
        }
    }

    @Override
    public AddonBuilder<Item> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        var dataComponents = json.has("components") ? DataComponentMap.CODEC.parse(JsonOps.INSTANCE, json.get("components")).getOrThrow() : DataComponentMap.EMPTY;

        ItemBuilder builder = new ItemBuilder(id, dataComponents, json);

        builder.type(GsonUtil.getAsResourceLocation(json, "type", null));

        GsonUtil.ifHasKey(json, "creative_mode_tab", je -> {
            for (PlacedTabPlacement placedTabPlacement : GsonUtil.fromListOrPrimitive(je, PlacedTabPlacement::fromJson)) {
                builder.creativeModeTab(placedTabPlacement);
            }
        });

        return builder;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(Palladium.id("items"), "Items")
                .add(HTMLBuilder.heading("Items"))
                .add(HTMLBuilder.subHeading("Global Settings"))
                .addDocumentation(getDefaultDocumentationBuilder())
                .addDocumentationSettings(new ArrayList<>(TYPE_SERIALIZERS.values()));
    }

    static {
        registerTypeSerializer(new NormalItemType());
        registerTypeSerializer(new BlockItemType());
        registerTypeSerializer(new ArmorItemType());
        registerTypeSerializer(new SwordItemType());
        registerTypeSerializer(new PickaxeItemType());
        registerTypeSerializer(new AxeItemType());
        registerTypeSerializer(new ShovelItemType());
        registerTypeSerializer(new HoeItemType());
        registerTypeSerializer(new ShieldItemType());
        registerTypeSerializer(new BowItemType());
        registerTypeSerializer(new CrossbowItemType());
        registerTypeSerializer(new EnergyItemType());
    }

    public static void registerTypeSerializer(ItemTypeSerializer serializer) {
        TYPE_SERIALIZERS.put(serializer.getId(), serializer);
    }

    public static ItemTypeSerializer getTypeSerializer(ResourceLocation id) {
        return TYPE_SERIALIZERS.get(id);
    }

    public static JsonDocumentationBuilder getDefaultDocumentationBuilder() {
        JsonDocumentationBuilder builder = new JsonDocumentationBuilder();

        builder.setDescription("These settings apply to ALL item types. Keep in mind that if fields are not required, you do NOT need to write them into your json.");

        builder.addProperty("type", ResourceLocation.class)
                .description("Item Type, each come with new different settings. Listed below on this page.")
                .fallback(Palladium.id("default"));
        builder.addProperty("creative_mode_tab", ResourceLocation.class)
                .description("ID of the creative mode tab the item is supposed to appear in. Fore more precise placements, check the \"Custom Items\" page on the wiki. Possible values: " + Arrays.toString(BuiltInRegistries.CREATIVE_MODE_TAB.keySet().stream().sorted(Comparator.comparing(ResourceLocation::toString)).toArray()))
                .fallback(null)
                .exampleJson(new JsonPrimitive("minecraft:decorations"));
        builder.addProperty("components", DataComponentMap.class)
                .description("Map of data components for this item. Check Minecraft Wiki")
                .fallback(null);

        return builder;
    }

    public interface ItemTypeSerializer extends DocumentedConfigurable {

        Item parse(JsonObject json, Item.Properties properties);
    }

    public static class PlacedTabPlacement {

        // 0 = add, 1 = addAfter, 2 = addBefore
        private final int type;
        private final ResourceLocation referencedItem;
        private final ResourceLocation tab;

        private PlacedTabPlacement(int type, ResourceLocation referencedItem, ResourceLocation tab) {
            this.type = type;
            this.referencedItem = referencedItem;
            this.tab = tab;
        }

        public ResourceLocation getTab() {
            return this.tab;
        }

        public static PlacedTabPlacement add(ResourceLocation tab) {
            return new PlacedTabPlacement(0, null, tab);
        }

        public static PlacedTabPlacement addAfter(ResourceLocation afterItem, ResourceLocation tab) {
            return new PlacedTabPlacement(1, afterItem, tab);
        }

        public static PlacedTabPlacement addBefore(ResourceLocation beforeItem, ResourceLocation tab) {
            return new PlacedTabPlacement(2, beforeItem, tab);
        }

        public void addToTab(CreativeModeTabRegistry.ItemGroupEntries entries, Item item) {
            if (this.type == 0) {
                entries.add(item);
            } else if (type == 1) {
                var addAfter = BuiltInRegistries.ITEM.get(this.referencedItem);

                if (addAfter == Items.AIR) {
                    AddonPackLog.warning("Tried to add '" + BuiltInRegistries.ITEM.getKey(item) + "' after unknown item '" + this.referencedItem + "' in creative mode tab");
                    entries.add(item);
                } else {
                    entries.addAfter(addAfter, item);
                }
            } else if (type == 2) {
                var addBefore = BuiltInRegistries.ITEM.get(this.referencedItem);

                if (addBefore == Items.AIR) {
                    AddonPackLog.warning("Tried to add '" + BuiltInRegistries.ITEM.getKey(item) + "' before unknown item '" + this.referencedItem + "' in creative mode tab");
                    entries.add(item);
                } else {
                    entries.addBefore(addBefore, item);
                }
            }
        }

        public static PlacedTabPlacement fromJson(JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                return add(GsonUtil.convertToResourceLocation(jsonElement, "creative_mode_tab"));
            } else {
                var json = GsonHelper.convertToJsonObject(jsonElement, "creative_mode_tab");

                if (GsonHelper.isValidNode(json, "after")) {
                    return addAfter(GsonUtil.convertToResourceLocation(json.get("after"), "creative_mode_tab.after"), GsonUtil.getAsResourceLocation(json, "tab"));
                } else if (GsonHelper.isValidNode(json, "before")) {
                    return addBefore(GsonUtil.convertToResourceLocation(json.get("before"), "creative_mode_tab.after"), GsonUtil.getAsResourceLocation(json, "tab"));
                } else {
                    return add(GsonUtil.getAsResourceLocation(json, "tab"));
                }
            }
        }

        public JsonElement toJson() {
            if (this.type == 0) {
                return new JsonPrimitive(this.tab.toString());
            } else {
                var json = new JsonObject();
                json.addProperty(this.type == 1 ? "after" : "before", this.referencedItem.toString());
                json.addProperty("tab", this.tab.toString());
                return json;
            }
        }

    }
}
