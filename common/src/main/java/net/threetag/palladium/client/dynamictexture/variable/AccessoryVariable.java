package net.threetag.palladium.client.dynamictexture.variable;

import com.google.common.collect.Ordering;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.Collection;
import java.util.Optional;

public class AccessoryVariable implements ITextureVariable {

    private final AccessorySlot accessorySlot;
    private final String fallbackValue, splitValue;
    private final boolean path;

    public AccessoryVariable(AccessorySlot accessorySlot, String fallbackValue, String splitValue, boolean path) {
        this.accessorySlot = accessorySlot;
        this.fallbackValue = fallbackValue;
        this.splitValue = splitValue;
        this.path = path;
    }

    @Override
    public Object get(DataContext context) {
        Optional<AccessoryPlayerData> dataOptional = Accessory.getPlayerData(context.getPlayer());

        if (dataOptional.isEmpty()) {
            return this.fallbackValue;
        }

        AccessoryPlayerData data = dataOptional.get();
        Collection<Accessory> accessories = data.accessories.get(this.accessorySlot);

        if (accessories == null || accessories.isEmpty()) {
            return this.fallbackValue;
        }

        StringBuilder result = new StringBuilder();
        for (Object o : accessories.stream().sorted(Ordering.usingToString()).toArray()) {
            result.append(o.toString().split(":")[this.path ? 1 : 0]);
            result.append(splitValue);
        }

        result.deleteCharAt(result.length() - splitValue.length());
        return result;
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new AccessoryVariable(
                    GsonUtil.getAsAccessorySlot(json, "accessory_slot"),
                    GsonHelper.getAsString(json, "fallback_value", ""),
                    GsonHelper.getAsString(json, "split_value", "_"),
                    GsonHelper.getAsBoolean(json, "use_path", true)
            );
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Accessory");

            builder.addProperty("accessory_slot", String.class)
                    .description("The ID of the accessory slot to read from")
                    .required()
                    .exampleJson(new JsonPrimitive("palladium:head"));

            builder.addProperty("fallback_value", String.class)
                    .description("If an accessory isn't chosen for the specified accessory slot, this value will be returned instead")
                    .fallback("")
                    .exampleJson(new JsonPrimitive("_not_set"));

            builder.addProperty("split_value", String.class)
                    .description("If multiple accessories are selected, they'll be returned in a list in alphabetical order separated by this string")
                    .fallback("_")
                    .exampleJson(new JsonPrimitive("_"));

            builder.addProperty("use_path", Boolean.class)
                    .description("Determines if the namespace or the path will be returned from the accessory ID. Example: the accessory ID is 'test:stone_hat'. If 'use_path' is set to true, you'll get 'stone_hat'. If it's set to false, you'll get 'test'.")
                    .fallback(true).exampleJson(new JsonPrimitive(true));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the ID of the selected accessory in the specified slot.";
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("accessory");
        }
    }
}