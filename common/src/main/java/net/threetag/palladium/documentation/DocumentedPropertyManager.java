package net.threetag.palladium.documentation;

import com.mojang.serialization.JsonOps;
import net.threetag.palladium.util.property.PropertyManager;

public interface DocumentedPropertyManager extends DocumentedConfigurable {

    PropertyManager getPropertyManager();

    @Override
    default void generateDocumentation(JsonDocumentationBuilder builder) {
        this.getPropertyManager().forEach(val -> {
            var prop = builder.addProperty(val.getProperty().getKey(), val.getProperty().getType().getName())
                    .description(val.getProperty().getDescription())
                    .fallbackObject(val.getProperty().getFallback())
                    .exampleJson(val.encode(JsonOps.INSTANCE));

            if (val.getProperty().isRequired()) {
                prop.required();
            }
        });
    }
}
