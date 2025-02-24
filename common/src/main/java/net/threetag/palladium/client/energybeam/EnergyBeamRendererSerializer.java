package net.threetag.palladium.client.energybeam;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public abstract class EnergyBeamRendererSerializer<T extends EnergyBeamRenderer> {

    private static final BiMap<ResourceLocation, EnergyBeamRendererSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<EnergyBeamRendererSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> {
        EnergyBeamRendererSerializer<?> serializer = TYPES.get(resourceLocation);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + resourceLocation);
    }, serializer -> {
        ResourceLocation resourceLocation = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(resourceLocation) : DataResult.error(() -> "Unknown type " + resourceLocation);
    });

    public static <T extends EnergyBeamRenderer> EnergyBeamRendererSerializer<T> register(ResourceLocation id, EnergyBeamRendererSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for energy beam renderer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public abstract MapCodec<T> codec();

}
