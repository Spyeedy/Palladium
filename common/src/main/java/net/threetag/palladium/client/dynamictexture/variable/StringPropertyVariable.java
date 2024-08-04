package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyType;

import java.util.concurrent.atomic.AtomicReference;

public class StringPropertyVariable implements ITextureVariable {
    private final String propertyKey;

    public StringPropertyVariable(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    @Override
    public Object get(DataContext context) {
        AtomicReference<String> result = new AtomicReference<>("");
        EntityPropertyHandler.getHandler(context.getEntity()).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);
            if (property.getType() == PalladiumPropertyType.STRING) {
                result.set((String) handler.get(property));
            }
        });
        return result.get();
    }

    public static class Serializer implements TextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new StringPropertyVariable(GsonHelper.getAsString(json, "property"));
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("String Property");

            builder.addProperty("property", String.class)
                    .description("Name of the property you want the value from.")
                    .required().exampleJson(new JsonPrimitive("example_property"));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the value of a String property within the player.";
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("string_property");
        }
    }
}
