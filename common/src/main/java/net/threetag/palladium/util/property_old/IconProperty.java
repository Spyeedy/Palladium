package net.threetag.palladium.util.property_old;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.util.icon.Icon;
import net.threetag.palladium.util.icon.IconSerializer;

import java.util.Objects;

public class IconProperty extends PalladiumProperty<Icon> {

    public IconProperty(String key) {
        super(key);
    }

    @Override
    public Icon fromJSON(JsonElement jsonElement) {
        return IconSerializer.parseJSON(jsonElement);
    }

    @Override
    public JsonElement toJSON(Icon value) {
        return IconSerializer.serializeJSON(value);
    }

    @Override
    public Icon fromNBT(Tag tag, Icon defaultValue) {
        if (tag instanceof CompoundTag compoundTag) {
            return IconSerializer.parseNBT(compoundTag);
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Icon value) {
        return IconSerializer.serializeNBT(value);
    }

    @Override
    public Icon fromBuffer(FriendlyByteBuf buf) {
        return IconSerializer.parseNBT(Objects.requireNonNull(buf.readNbt()));
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeNbt(IconSerializer.serializeNBT((Icon) value));
    }

    @Override
    public String getPropertyType() {
        return "icon";
    }
}
