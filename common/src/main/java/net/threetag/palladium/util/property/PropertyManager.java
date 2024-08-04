package net.threetag.palladium.util.property;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PropertyManager implements Iterable<PalladiumPropertyValue<?>> {

    public static final Codec<PropertyManager> CODEC = CompoundTag.CODEC.xmap(compoundTag -> {
        var p = new PropertyManager();
        p.fromNBT(compoundTag);
        return p;
    }, propertyManager -> propertyManager.toNBT(true));

    public static final StreamCodec<ByteBuf, PropertyManager> STREAM_CODEC = ByteBufCodecs.fromCodec(CompoundTag.CODEC.xmap(
            compoundTag -> Utils.tap(new PropertyManager(), manager -> manager.fromNBT(compoundTag)),
            propertyManager -> propertyManager.toNBT(false)
    ));

    final Map<PalladiumProperty<?>, PalladiumPropertyValue<?>> values = new LinkedHashMap<>();
    Listener listener;

    public PropertyManager setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public <T> PropertyManager register(PalladiumProperty<T> property) {
        return this.register(property, property.getFallback());
    }

    public <T> PropertyManager register(PalladiumProperty<T> property, T value) {
        this.values.put(property, new PalladiumPropertyValue<>(property, value));
        return this;
    }

    public boolean isRegistered(PalladiumProperty<?> property) {
        return this.values.containsKey(property);
    }

    public <T> PropertyManager set(PalladiumProperty<T> property, @Nullable T value) {
        if (!this.values.containsKey(property)) {
            throw new RuntimeException("Property " + property.getKey() + " was not registered!");
        }
        PalladiumPropertyValue<T> holder = this.getHolder(property);
        T oldValue = holder.value();
        holder.set(value);
        if (this.listener != null) {
            this.listener.onChanged(property, holder, oldValue, value);
        }
        return this;
    }

    @Nullable
    public <T> T get(PalladiumProperty<T> property) {
        return this.getHolder(property).value();
    }

    @SuppressWarnings("unchecked")
    public <T> PalladiumPropertyValue<T> getHolder(PalladiumProperty<T> property) {
        return (PalladiumPropertyValue<T>) this.values.get(property);
    }

    public Collection<PalladiumPropertyValue<?>> getHolders() {
        return this.values.values();
    }

    public <T> Optional<T> optional(PalladiumProperty<T> property) {
        return Optional.ofNullable(this.get(property));
    }

    @SuppressWarnings("unchecked")
    public <T> PalladiumProperty<T> getPropertyByName(String name) {
        for (PalladiumProperty<?> property : this.values.keySet()) {
            if (property.getKey().equals(name)) {
                return (PalladiumProperty<T>) property;
            }
        }
        return null;
    }

    public void fromNBT(CompoundTag nbt) {
        for (Map.Entry<PalladiumProperty<?>, PalladiumPropertyValue<?>> e : this.values.entrySet()) {
            var property = e.getKey();
            var holder = e.getValue();

            if (nbt.contains(property.getKey())) {
                Tag tag = nbt.get(property.getKey());
                holder.read(NbtOps.INSTANCE, tag);
            } else {
                holder.setToFallback();
            }
        }
    }

    public CompoundTag toNBT(boolean toDisk) {
        CompoundTag nbt = new CompoundTag();

        for (Map.Entry<PalladiumProperty<?>, PalladiumPropertyValue<?>> e : this.values.entrySet()) {
            var property = e.getKey();
            var holder = e.getValue();

            if (!toDisk || property.isPersistent()) {
                nbt.put(property.getKey(), holder.encode(NbtOps.INSTANCE));
            }
        }

        return nbt;
    }

    public void fromJSON(CompoundTag nbt) {
        for (Map.Entry<PalladiumProperty<?>, PalladiumPropertyValue<?>> e : this.values.entrySet()) {
            var property = e.getKey();
            var holder = e.getValue();

            if (property.isConfigurable()) {
                if (nbt.contains(property.getKey())) {
                    Tag tag = nbt.get(property.getKey());
                    holder.read(NbtOps.INSTANCE, tag);
                } else {
                    if (property.isRequired()) {
                        throw new JsonParseException("Property " + property.getKey() + " is required!");
                    } else {
                        holder.setToFallback();
                    }
                }
            } else {
                holder.setToFallback();
            }
        }
    }

    public void fromJSON(JsonObject json) {
        for (Map.Entry<PalladiumProperty<?>, PalladiumPropertyValue<?>> e : this.values.entrySet()) {
            var property = e.getKey();
            var holder = e.getValue();

            if (property.isConfigurable()) {
                if (GsonHelper.isValidNode(json, property.getKey())) {
                    holder.read(JsonOps.INSTANCE, json.get(property.getKey()));
                } else {
                    if (property.isRequired()) {
                        throw new JsonParseException("Property " + property.getKey() + " is required!");
                    } else {
                        holder.setToFallback();
                    }
                }
            } else {
                holder.setToFallback();
            }
        }
    }

    public PropertyManager copy() {
        var manager = new PropertyManager();
        for (PalladiumPropertyValue<?> value : this.values.values()) {
            manager.values.put(value.getProperty(), value.copy());
        }
        return manager;
    }

    @NotNull
    @Override
    public Iterator<PalladiumPropertyValue<?>> iterator() {
        return this.values.values().iterator();
    }

    public interface Listener {

        <T> void onChanged(PalladiumProperty<T> property, PalladiumPropertyValue<T> valueHolder, T oldValue, T newValue);

    }

}
