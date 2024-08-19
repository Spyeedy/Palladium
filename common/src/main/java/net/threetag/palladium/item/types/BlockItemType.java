package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;

public class BlockItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public BlockItem parse(JsonObject json, Item.Properties properties) {
        var blockId = GsonUtil.getAsResourceLocation(json, "block");

        if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
            throw new JsonParseException("Unknown block '" + blockId + "'");
        }

        return new BlockItem(BuiltInRegistries.BLOCK.get(blockId), properties);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Block Item");
        builder.setDescription("Item for a block, duh");

        builder.addProperty("block", ResourceLocation.class)
                .description("ID of the block that this item is for")
                .required().exampleJson(new JsonPrimitive("test:test_block"));
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.withDefaultNamespace("block_item");
    }
}
