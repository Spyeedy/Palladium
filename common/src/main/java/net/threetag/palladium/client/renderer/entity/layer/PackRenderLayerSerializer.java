package net.threetag.palladium.client.renderer.entity.layer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public abstract class PackRenderLayerSerializer<T extends PackRenderLayer<? extends PackRenderLayer.State>> {

    private static final BiMap<ResourceLocation, PackRenderLayerSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<PackRenderLayerSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> {
        PackRenderLayerSerializer<?> layerSerializer = TYPES.get(resourceLocation);
        return layerSerializer != null ? DataResult.success(layerSerializer) : DataResult.error(() -> "Unknown type " + resourceLocation);
    }, layerSerializer -> {
        ResourceLocation resourceLocation = TYPES.inverse().get(layerSerializer);
        return layerSerializer != null ? DataResult.success(resourceLocation) : DataResult.error(() -> "Unknown type " + resourceLocation);
    });

    public static <R extends PackRenderLayer.State, T extends PackRenderLayer<R>> PackRenderLayerSerializer<T> register(ResourceLocation id, PackRenderLayerSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for pack render layer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public abstract MapCodec<T> codec();

}
