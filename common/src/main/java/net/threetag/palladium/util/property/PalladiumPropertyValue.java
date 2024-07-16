package net.threetag.palladium.util.property;

import com.mojang.serialization.DynamicOps;

public class PalladiumPropertyValue<T> {

    private final PalladiumProperty<T> property;
    private T value;

    public PalladiumPropertyValue(PalladiumProperty<T> property, T value) {
        this.property = property;
        this.value = value;
    }

    public PalladiumProperty<T> getProperty() {
        return this.property;
    }

    public <R> R encode(DynamicOps<R> dynamicOps) {
        return this.property.getType().getCodec().encodeStart(dynamicOps, this.value).getOrThrow();
    }

    public <R> T decode(DynamicOps<R> dynamicOps, R source) {
        return this.property.getType().getCodec().parse(dynamicOps, source).getOrThrow();
    }

    public <R> void read(DynamicOps<R> dynamicOps, R source) {
        this.value = this.property.getType().getCodec().parse(dynamicOps, source).getOrThrow();
    }

    public T value() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void setNull() {
        this.value = null;
    }

    public boolean isNull() {
        return this.value == null;
    }

    public void setToFallback() {
        this.value = this.property.getFallback();
    }

    public PalladiumPropertyValue<T> copy() {
        return new PalladiumPropertyValue<>(this.property, this.value);
    }
}
