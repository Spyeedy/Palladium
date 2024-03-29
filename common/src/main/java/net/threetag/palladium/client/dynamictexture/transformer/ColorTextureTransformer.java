package net.threetag.palladium.client.dynamictexture.transformer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record ColorTextureTransformer(JsonArray RGBA, boolean ignoreBlank) implements ITextureTransformer {

    @Override
    public NativeImage transform(NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        for (int y = 0; y < texture.getHeight(); ++y) {
            for (int x = 0; x < texture.getWidth(); ++x) {
                blendPixel(texture, x, y, stringConverter);
            }
        }

        return texture;
    }


    public void blendPixel(NativeImage texture, int x, int y, Function<String, String> stringConverter) {
        if (texture.format() != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else if (RGBA.asList().size() != 4) {
            throw new IllegalArgumentException("RGBA array must be of length four");
        } else {
            int i = texture.getPixelRGBA(x, y);
            if (FastColor.ABGR32.alpha(i) == 0 & ignoreBlank) return;

            List<JsonElement> jsonList = RGBA.asList();
            List<Integer> list = new ArrayList<>();
            jsonList.forEach(jsonElement -> {
                if (!jsonElement.isJsonPrimitive()) throw new IllegalArgumentException("Expected RGBA array to contain JSON primitives, got " + jsonElement);
                if (jsonElement.getAsJsonPrimitive().isString()) list.add(Integer.parseInt(stringConverter.apply(jsonElement.getAsString())));
                else list.add(jsonElement.getAsJsonPrimitive().getAsInt());
            });
            int R = list.get(0), G = list.get(1), B = list.get(2), A = list.get(3);

            float f = A / 255.0F;
            float g = B / 255.0F;
            float h = G / 255.0F;
            float j = R / 255.0F;
            // skip base texture alpha
            float l = (float) FastColor.ABGR32.blue(i) / 255.0F;
            float m = (float) FastColor.ABGR32.green(i) / 255.0F;
            float n = (float) FastColor.ABGR32.red(i) / 255.0F;

            float p = 1.0F - f;
            float r = g * f + l * p;
            float s = h * f + m * p;
            float t = j * f + n * p;

            if (r > 1.0F) {
                r = 1.0F;
            }

            if (s > 1.0F) {
                s = 1.0F;
            }

            if (t > 1.0F) {
                t = 1.0F;
            }

            int v = (int)(r * 255.0F);
            int w = (int)(s * 255.0F);
            int z = (int)(t * 255.0F);
            texture.setPixelRGBA(x, y, FastColor.ABGR32.color(FastColor.ABGR32.alpha(i), v, w, z));
        }
    }
}
