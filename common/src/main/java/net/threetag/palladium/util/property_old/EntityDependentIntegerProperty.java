package net.threetag.palladium.util.property_old;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.entity.value.EntityDependentNumber;

public class EntityDependentIntegerProperty extends PalladiumProperty<EntityDependentNumber> {

    public EntityDependentIntegerProperty(String key) {
        super(key);
    }

    @Override
    public EntityDependentNumber fromJSON(JsonElement jsonElement) {
        return EntityDependentNumber.fromJson(jsonElement, this.getKey());
    }

    @Override
    public JsonElement toJSON(EntityDependentNumber value) {
        return value.toJson();
    }

    @Override
    public EntityDependentNumber fromNBT(Tag tag, EntityDependentNumber defaultValue) {
        var value = EntityDependentNumber.fromNBT(tag);
        return value != null ? value : defaultValue;
    }

    @Override
    public Tag toNBT(EntityDependentNumber value) {
        return value.toNBT();
    }

    @Override
    public EntityDependentNumber fromBuffer(FriendlyByteBuf buf) {
        return EntityDependentNumber.fromBuffer(buf);
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        ((EntityDependentNumber) value).toBuffer(buf);
    }

    @Override
    public String getString(EntityDependentNumber value) {
        return value == null ? null : value.toJson().toString();
    }
}
