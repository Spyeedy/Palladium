package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.EnergyItem;

public class EnergyItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public EnergyItem parse(JsonObject json, Item.Properties properties) {
        int capacity = GsonHelper.getAsInt(json, "capacity");
        int maxInput = GsonHelper.getAsInt(json, "max_input");
        int maxOutput = GsonHelper.getAsInt(json, "max_output");

        if (capacity <= 0) {
            throw new JsonParseException("Energy capacity must be greater than 0");
        }

        if (maxInput < 0) {
            throw new JsonParseException("Energy max input can not be negative");
        }

        if (maxOutput < 0) {
            throw new JsonParseException("Energy max output can not be negative");
        }

        return new EnergyItem(properties, capacity, maxInput, maxOutput);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Energy Item");

        builder.addProperty("capacity", Integer.class)
                .description("Max amount of energy the item can hold")
                .required().exampleJson(new JsonPrimitive(500000));

        builder.addProperty("max_input", Integer.class)
                .description("Maximum amount of energy the item can be inserted with during one insertion. Using 0 makes the item not accept any energy")
                .required().exampleJson(new JsonPrimitive(1000));

        builder.addProperty("max_output", Integer.class)
                .description("Maximum amount of energy the item can extract with during one withdrawal. Using 0 makes the item not extract any energy")
                .required().exampleJson(new JsonPrimitive(1000));
    }

    @Override
    public ResourceLocation getId() {
        return Palladium.id("energy");
    }
}
