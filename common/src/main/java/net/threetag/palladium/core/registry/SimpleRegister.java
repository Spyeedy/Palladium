package net.threetag.palladium.core.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SimpleRegister {

    @ExpectPlatform
    public static <T> void register(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) {
        throw new AssertionError();
    }

}
