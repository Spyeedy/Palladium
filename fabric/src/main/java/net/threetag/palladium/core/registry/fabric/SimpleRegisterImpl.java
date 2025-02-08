package net.threetag.palladium.core.registry.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class SimpleRegisterImpl {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> void register(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) {
        Registry registry = BuiltInRegistries.REGISTRY.getValue(registryResourceKey.location());
        Registry.register(Objects.requireNonNull(registry), id, object);
    }
}
