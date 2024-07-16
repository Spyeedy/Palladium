package net.threetag.palladium.util.icon;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

public record ExperienceIcon(int amount, boolean level) implements Icon {

    public static final MapCodec<ExperienceIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("amount").forGetter(ExperienceIcon::amount),
                    Codec.BOOL.optionalFieldOf("level", true).forGetter(ExperienceIcon::level)
            )
            .apply(instance, ExperienceIcon::new));

    private static final TexturedIcon BACKGROUND_ICON = new TexturedIcon(Palladium.id("textures/icon/experience.png"));

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        BACKGROUND_ICON.draw(mc, guiGraphics, context, x, y, width, height);

        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(x, y, 0);

        if (width != 16 || height != 16) {
            stack.scale(width / 16F, height / 16F, 1);
        }

        String text = this.amount + (this.level ? "L" : "");
        guiGraphics.drawString(mc.font, text, 9, 8, 0, false);
        guiGraphics.drawString(mc.font, text, 7, 8, 0, false);
        guiGraphics.drawString(mc.font, text, 8, 9, 0, false);
        guiGraphics.drawString(mc.font, text, 8, 7, 0, false);
        guiGraphics.drawString(mc.font, text, 8, 8, 8453920, false);

        stack.popPose();
    }

    @Override
    public IconSerializer<ExperienceIcon> getSerializer() {
        return IconSerializers.EXPERIENCE.get();
    }

    @Override
    public String toString() {
        return "ExperienceIcon{" +
                "amount=" + amount +
                ", level=" + level +
                '}';
    }

    public static class Serializer extends IconSerializer<ExperienceIcon> {

        @Override
        public MapCodec<ExperienceIcon> codec() {
            return CODEC;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Experience Icon");
            builder.setDescription("Displays an experience amount as an icon.");

            builder.addProperty("amount", Integer.class)
                    .description("Amount of experience points/level")
                    .required().exampleJson(new JsonPrimitive(3));

            builder.addProperty("level", Boolean.class)
                    .description("Determines if icon should display level or not")
                    .fallback(true).exampleJson(new JsonPrimitive(true));
        }
    }
}
