package net.threetag.palladium.client.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record ModelLayerLocationCodec(ResourceLocation model, String layer) {

    public static final Codec<ModelLayerLocationCodec> CODEC = Codec.STRING.comapFlatMap(ModelLayerLocationCodec::read, ModelLayerLocationCodec::toString).stable();

    public static ModelLayerLocationCodec parse(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new ModelLayerLocationCodec(ResourceLocation.parse(s[0]), "main");
        } else {
            return new ModelLayerLocationCodec(ResourceLocation.parse(s[0]), s[1]);
        }
    }

    public static DataResult<ModelLayerLocationCodec> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid model layer location: " + path + " " + e.getMessage());
        }
    }

    @Environment(EnvType.CLIENT)
    public ModelLayerLocation get() {
        return new ModelLayerLocation(this.model, this.layer);
    }

    @Override
    public String toString() {
        return this.model.toString() + "#" + this.layer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ModelLayerLocationCodec(ResourceLocation model1, String layer1))) return false;
        return Objects.equals(layer, layer1) && Objects.equals(model, model1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, layer);
    }
}
