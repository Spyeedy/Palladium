package net.threetag.palladium.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class RegistryHolder<T> implements Holder<T>, Supplier<T> {

    public abstract ResourceLocation getId();

    @Override
    public T get() {
        return this.value();
    }
}
