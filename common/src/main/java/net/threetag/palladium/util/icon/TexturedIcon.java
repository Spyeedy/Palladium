package net.threetag.palladium.util.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.context.DataContext;

import java.awt.*;

public class TexturedIcon implements Icon {

    public static final MapCodec<TexturedIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    TextureReference.CODEC.fieldOf("texture").forGetter(TexturedIcon::getTexture),
                    CodecUtils.COLOR_CODEC.optionalFieldOf("texture", null).forGetter(TexturedIcon::getTint)
            )
            .apply(instance, TexturedIcon::new));

    public static final ResourceLocation LOCK = Palladium.id("textures/icons/lock.png");

    public final TextureReference texture;
    public final Color tint;

    public TexturedIcon(TextureReference texture) {
        this(texture, null);
    }

    public TexturedIcon(TextureReference texture, Color tint) {
        this.texture = texture;
        this.tint = tint;
    }

    public TexturedIcon(ResourceLocation texture) {
        this(texture, null);
    }

    public TexturedIcon(ResourceLocation texture, Color tint) {
        this(TextureReference.normal(texture), tint);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int w, int h) {
        var stack = guiGraphics.pose();
        RenderSystem.setShaderTexture(0, this.texture.getTexture(context));
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        var tesselator = Tesselator.getInstance();
        var buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        var m = stack.last().pose();
        var color = this.tint != null ? this.tint : Color.WHITE;
        var r = color.getRed();
        var g = color.getGreen();
        var b = color.getBlue();
        var a = color.getAlpha();
        buffer.vertex(m, x, y + h, 0).color(r, g, b, a).uv(0, 1).endVertex();
        buffer.vertex(m, x + w, y + h, 0).color(r, g, b, a).uv(1, 1).endVertex();
        buffer.vertex(m, x + w, y, 0).color(r, g, b, a).uv(1, 0).endVertex();
        buffer.vertex(m, x, y, 0).color(r, g, b, a).uv(0, 0).endVertex();
        tesselator.end();
    }

    public TextureReference getTexture() {
        return texture;
    }

    public Color getTint() {
        return tint;
    }

    @Override
    public IconSerializer<TexturedIcon> getSerializer() {
        return IconSerializers.TEXTURE.get();
    }

    @Override
    public String toString() {
        return "TexturedIcon{" +
                "texture=" + texture +
                ", tint=" + tint +
                '}';
    }

    public static class Serializer extends IconSerializer<TexturedIcon> {

        @Override
        public MapCodec<TexturedIcon> codec() {
            return CODEC;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Textured Icon");
            builder.setDescription("Uses a texture to render as an icon.");

            builder.addProperty("texture", TextureReference.class)
                    .description("Path to the texture file or dynamic texture json file.")
                    .required().exampleJson(new JsonPrimitive("example:textures/icons/my_icon.png"));

            JsonArray tint = new JsonArray();
            tint.add(123);
            tint.add(32);
            tint.add(212);
            builder.addProperty("tint", Integer[].class)
                    .description("Adds an additional tint to the texture.")
                    .fallback(new Integer[]{255, 255, 255}, "/").exampleJson(tint);
        }
    }
}
