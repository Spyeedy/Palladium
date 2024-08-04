package net.threetag.palladium.util.property_old;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.power.ability.AbilityReference;

public class AbilityReferenceProperty extends PalladiumProperty<AbilityReference> {

    public AbilityReferenceProperty(String key) {
        super(key);
    }

    @Override
    public AbilityReference fromJSON(JsonElement jsonElement) {
        return AbilityReference.parse(jsonElement.getAsString());
    }

    @Override
    public JsonElement toJSON(AbilityReference value) {
        return new JsonPrimitive(value.toString());
    }

    @Override
    public AbilityReference fromNBT(Tag tag, AbilityReference defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return AbilityReference.parse(stringTag.getAsString());
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(AbilityReference value) {
        return StringTag.valueOf(value.toString());
    }

    @Override
    public AbilityReference fromBuffer(FriendlyByteBuf buf) {
        return new AbilityReference(buf.readResourceLocation(), buf.readUtf());
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        var ref = (AbilityReference) value;
        buf.writeResourceLocation(ref.powerId());
        buf.writeUtf(ref.abilityKey());
    }

    @Override
    public String getPropertyType() {
        return "ability_reference";
    }
}
