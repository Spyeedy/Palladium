package net.threetag.palladium.documentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EquipmentSlot;

public interface Documented<T, R extends T> {

    JsonElement TYPE_STRING = new JsonPrimitive("string");
    JsonElement TYPE_STRING_ARRAY = new JsonPrimitive("string[]");
    JsonElement TYPE_RESOURCE_LOCATION = new JsonPrimitive("ResourceLocation");
    JsonElement TYPE_BOOLEAN = new JsonPrimitive("boolean");
    JsonElement TYPE_INT = new JsonPrimitive("integer");
    JsonElement TYPE_FLOAT = new JsonPrimitive("float");
    JsonElement TYPE_DOUBLE = new JsonPrimitive("double");
    JsonElement TYPE_VECTOR3 = new JsonPrimitive("Vector 3D");
    JsonElement TYPE_TEXTURE_REFERENCE = new JsonPrimitive("Texture Reference");
    JsonElement TYPE_TEXT_COMPONENT = new JsonPrimitive("Text Component");
    JsonElement TYPE_NBT = new JsonPrimitive("NBT");

    JsonElement TYPE_ATTRIBUTE = new JsonPrimitive("Attribute ID");
    JsonElement TYPE_DAMAGE_TYPE = new JsonPrimitive("Damage Type ID");
    JsonElement TYPE_PARTICLE_TYPE = new JsonPrimitive("Particle Type ID");

    CodecDocumentationBuilder<T, R> getDocumentation(HolderLookup.Provider provider);

    static JsonElement typeEnum(Enum<?>[] values) {
        var json = new JsonObject();
        json.addProperty("type", "enum");

        var array = new JsonArray();
        for (Enum<?> value : values) {
            array.add(new JsonPrimitive(value.name()));
        }
        json.add("values", array);

        return json;
    }

    // TODO Accessories
    static JsonElement typePlayerSlots() {
        var json = new JsonObject();
        json.addProperty("type", "enum");

        var array = new JsonArray();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            array.add(new JsonPrimitive(slot.name()));
        }
        json.add("values", array);

        return json;
    }

    static JsonElement typeCombined(JsonElement... elements) {
        var json = new JsonObject();
        json.addProperty("type", "combined");

        var array = new JsonArray();
        for (JsonElement element : elements) {
            array.add(element);
        }
        json.add("options", array);

        return json;
    }

    static JsonElement typeListOrPrimitive(JsonElement type) {
        return typeCombined(type, new JsonPrimitive(type.getAsString() + "[]"));
    }

}
