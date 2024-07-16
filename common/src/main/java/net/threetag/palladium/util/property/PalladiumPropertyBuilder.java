package net.threetag.palladium.util.property;

import org.jetbrains.annotations.Nullable;

public class PalladiumPropertyBuilder<T> {

    private final String key;
    private final PalladiumPropertyType<T> type;
    @Nullable
    private String description = null;
    private boolean required = false;
    @Nullable
    private T fallback = null;
    private SyncOption syncOption = SyncOption.EVERYONE;
    private boolean persistent = true;

    private PalladiumPropertyBuilder(String key, PalladiumPropertyType<T> type) {
        this.key = key;
        this.type = type;
    }

    public static <T> PalladiumPropertyBuilder<T> create(String name, PalladiumPropertyType<T> type) {
        return new PalladiumPropertyBuilder<>(name, type);
    }

    public PalladiumPropertyBuilder<T> configurable(String description) {
        return this.configurable(description, false, null);
    }

    public PalladiumPropertyBuilder<T> configurable(String description, boolean required) {
        return this.configurable(description, required, null);
    }

    public PalladiumPropertyBuilder<T> configurable(String description, boolean required, @Nullable T fallback) {
        this.description = description;
        this.required = required;
        this.fallback = fallback;
        return this;
    }

    public PalladiumPropertyBuilder<T> sync(SyncOption syncOption) {
        this.syncOption = syncOption;
        return this;
    }

    public PalladiumPropertyBuilder<T> disablePersistence() {
        this.persistent = false;
        return this;
    }

    public PalladiumProperty<T> build() {
        return new PalladiumProperty<T>(this.key, this.type, this.description, this.required, this.fallback, this.syncOption, this.persistent);
    }
}
