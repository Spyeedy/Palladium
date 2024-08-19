package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;

public class NormalItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public Item parse(JsonObject json, Item.Properties properties) {
        return new Item(properties);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Item");
        builder.setDescription("Default Item Type, you don't need to specify that you want this one, leaving 'type' out of the json will make it fall back to this one.");
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.withDefaultNamespace("item");
    }
}
