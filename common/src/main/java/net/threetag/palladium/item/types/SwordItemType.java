package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.addonpack.parser.ToolTierParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.Arrays;

public class SwordItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public SwordItem parse(JsonObject json, Item.Properties properties) {
        Tier tier = ToolTierParser.getToolTier(GsonUtil.getAsResourceLocation(json, "tier"));

        if (tier == null) {
            throw new JsonParseException("Unknown tool tier '" + GsonUtil.getAsResourceLocation(json, "tier") + "'");
        }

        return new SwordItem(tier, properties);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Sword");

        builder.addProperty("tier", Tier.class)
                .description("Tool tier, which defines certain characteristics about the tool. Open tool_tiers.html for seeing how to make custom ones. Possible values: " + Arrays.toString(ToolTierParser.getIds().toArray(new ResourceLocation[0])))
                .required().exampleJson(new JsonPrimitive("minecraft:diamond"));
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.withDefaultNamespace("sword");
    }
}
