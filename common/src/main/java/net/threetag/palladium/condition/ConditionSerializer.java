package net.threetag.palladium.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDefaultDocumentedConfigurable;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class ConditionSerializer<T extends Condition> implements IDefaultDocumentedConfigurable {

    final PropertyManager propertyManager = new PropertyManager();

    public ConditionSerializer<T> withProperty(PalladiumProperty<T> data, T value) {
        this.propertyManager.register(data, value);
        return this;
    }

    public <T> T getProperty(JsonObject json, PalladiumProperty<T> data) {
        if (this.propertyManager.isRegistered(data)) {
            if (json.has(data.getKey())) {
                JsonElement jsonElement = json.get(data.getKey());
                if (jsonElement.isJsonPrimitive() && jsonElement.getAsString().equals("null")) {
                    return null;
                } else {
                    return data.fromJSON(json.get(data.getKey()));
                }
            } else {
                return this.propertyManager.get(data);
            }
        } else {
            throw new RuntimeException("Condition Serializer does not have " + data.getKey() + " data!");
        }
    }


    public abstract MapCodec<T> codec();

    public ConditionEnvironment getContextEnvironment() {
        return ConditionEnvironment.ALL;
    }

    public static boolean checkConditions(Collection<Condition> conditions, DataContext context) {
        for (Condition condition : conditions) {
            if (!condition.active(context)) {
                return false;
            }
        }

        return true;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(Palladium.id("conditions"), "Conditions")
                .add(HTMLBuilder.heading("Conditions"))
                .addDocumentationSettings(PalladiumRegistries.CONDITION_SERIALIZER.stream().sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public ResourceLocation getId() {
        return PalladiumRegistries.CONDITION_SERIALIZER.getKey(this);
    }

    public String getDocumentationDescription() {
        return "";
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        IDefaultDocumentedConfigurable.super.generateDocumentation(builder);
        builder.setTitle(this.getId().getPath());

        var desc = this.getDocumentationDescription();
        if (desc != null && !desc.isEmpty()) {
            builder.setDescription(desc + "<br><br>" + "Applicable for: " + this.getContextEnvironment().toString().toLowerCase(Locale.ROOT));
        } else {
            builder.setDescription("Applicable for: " + this.getContextEnvironment().toString().toLowerCase(Locale.ROOT));
        }
    }
}
