package net.threetag.palladium.addonpack.builder;

import com.google.gson.JsonObject;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder extends AddonBuilder<Item> {

    private final JsonObject json;
    private final DataComponentMap components;
    private ResourceLocation typeSerializerId = null;
    private final List<ItemParser.PlacedTabPlacement> creativeModeTabs = new ArrayList<>();

    public ItemBuilder(ResourceLocation id, DataComponentMap components, JsonObject json) {
        super(id);
        this.json = json;
        this.components = components;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected Item create() {
        if (this.typeSerializerId == null) {
            this.typeSerializerId = ItemParser.FALLBACK_SERIALIZER;
        }

        ItemParser.ItemTypeSerializer serializer = ItemParser.getTypeSerializer(this.typeSerializerId);

        if (serializer == null) {
            AddonPackLog.warning("Unknown item type '" + this.typeSerializerId + "', falling back to '" + ItemParser.FALLBACK_SERIALIZER + "'");
        }

        var properties = new Item.Properties();
        for (TypedDataComponent component : this.components) {
            properties.component(component.type(), component.value());
        }

        Item item = serializer != null ? serializer.parse(this.json, properties) : new Item(properties);

        for (ItemParser.PlacedTabPlacement creativeModeTab : this.creativeModeTabs) {
            ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, creativeModeTab.getTab());
            CreativeModeTabRegistry.addToTab(tabKey, entries -> creativeModeTab.addToTab(entries, (Item) item));
        }

        return (Item) item;
    }

    public ItemBuilder type(ResourceLocation serializerId) {
        this.typeSerializerId = serializerId;
        return this;
    }

    public ItemBuilder creativeModeTab(ItemParser.PlacedTabPlacement tabPlacement) {
        this.creativeModeTabs.add(tabPlacement);
        return this;
    }

}
