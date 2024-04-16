package net.threetag.palladium.client.dynamictexture.transformer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;

import java.awt.*;
import java.io.IOException;
import java.util.function.Function;

public record ColorTextureTransformer(Object rawColor, boolean ignoreBlank) implements ITextureTransformer {

    @Override
    public NativeImage transform(NativeImage texture, ResourceManager manager, Function<String, String> stringConverter) throws IOException {
        var color = Color.WHITE;
        if (this.rawColor instanceof String string) {
            color = Color.decode(stringConverter.apply(string));
        } else if (this.rawColor instanceof Integer[] integers && integers.length == 3) {
            color = new Color(
                    Integer.getInteger(stringConverter.apply(integers[0].toString())),
                    Integer.getInteger(stringConverter.apply(integers[1].toString())),
                    Integer.getInteger(stringConverter.apply(integers[2].toString()))
            );
        }

        for (int y = 0; y < texture.getHeight(); ++y) {
            for (int x = 0; x < texture.getWidth(); ++x) {
                blendPixel(texture, x, y, color);
            }
        }

        return texture;
    }


    public void blendPixel(NativeImage texture, int x, int y, Color color) {
        if (texture.format() != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else {
            int i = texture.getPixelRGBA(x, y);
            if (FastColor.ABGR32.alpha(i) == 0 & this.ignoreBlank) return;

            float f = color.getAlpha() / 255.0F;
            float g = color.getBlue() / 255.0F;
            float h = color.getGreen() / 255.0F;
            float j = color.getRed() / 255.0F;
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

            int v = (int) (r * 255.0F);
            int w = (int) (s * 255.0F);
            int z = (int) (t * 255.0F);
            texture.setPixelRGBA(x, y, FastColor.ABGR32.color(FastColor.ABGR32.alpha(i), v, w, z));
        }
    }
}
