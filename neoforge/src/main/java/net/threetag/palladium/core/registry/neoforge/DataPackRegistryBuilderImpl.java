package net.threetag.palladium.core.registry.neoforge;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.threetag.palladium.neoforge.PalladiumNeoForge;

public class DataPackRegistryBuilderImpl {

    public static <T> void create(ResourceKey<Registry<T>> key, Codec<T> dataCodec, Codec<T> networkCodec) {
        PalladiumNeoForge.whenModBusAvailable(key.location().getNamespace(), bus -> {
            bus.<DataPackRegistryEvent.NewRegistry>addListener(event -> event.dataPackRegistry(key, dataCodec, networkCodec));
        });
    }

}
