package net.threetag.palladium.util.property;

import net.minecraft.world.entity.Entity;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PalladiumProperty<T> {

    private final String key;
    private final PalladiumPropertyType<T> type;
    private final String description;
    private final boolean required;
    private final T fallback;
    private final SyncOption syncOption;
    private final boolean persistent;

    protected PalladiumProperty(String key, PalladiumPropertyType<T> type, String description, boolean required, T fallback, SyncOption syncOption, boolean persistent) {
        this.key = key;
        this.type = type;
        this.description = description;
        this.required = required;
        this.fallback = fallback;
        this.syncOption = syncOption;
        this.persistent = persistent;
    }

    public String getKey() {
        return this.key;
    }

    public PalladiumPropertyType<T> getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public T getFallback() {
        return this.fallback;
    }

    public SyncOption getSyncType() {
        return this.syncOption;
    }

    public boolean isConfigurable() {
        return this.description != null;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public void set(Entity entity, T value) {
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> handler.set(this, value));
    }

    public T get(Entity entity) {
        AtomicReference<T> result = new AtomicReference<>();
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> result.set(handler.get(this)));
        return result.get();
    }

    public boolean isRegistered(Entity entity) {
        AtomicBoolean result = new AtomicBoolean(false);
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> result.set(handler.isRegistered(this)));
        return result.get();
    }

    public String getString(T value) {
        return value == null ? null : value.toString();
    }

}
