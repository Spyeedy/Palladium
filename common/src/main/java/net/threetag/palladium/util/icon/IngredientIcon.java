package net.threetag.palladium.util.icon;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.GuiUtil;
import net.threetag.palladium.util.context.DataContext;

public record IngredientIcon(Ingredient ingredient) implements Icon {

    public static final MapCodec<IngredientIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(Ingredient.CODEC.fieldOf("ingredient").forGetter(IngredientIcon::ingredient))
            .apply(instance, IngredientIcon::new));

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(x + width / 2D, y + height / 2D, 100);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F, s / 16F);
        }

        int stackIndex = (int) ((System.currentTimeMillis() / 1000) % this.ingredient.getItems().length);
        GuiUtil.drawItem(guiGraphics, this.ingredient.getItems()[stackIndex], 0, true, null);
        stack.popPose();
    }

    @Override
    public IconSerializer<IngredientIcon> getSerializer() {
        return IconSerializers.INGREDIENT.get();
    }

    @Override
    public String toString() {
        return "IngredientIcon{" +
                "ingredient=" + Ingredient.CODEC.encodeStart(JsonOps.COMPRESSED, ingredient).getOrThrow().toString() +
                '}';
    }

    public static class Serializer extends IconSerializer<IngredientIcon> {

        @Override
        public MapCodec<IngredientIcon> codec() {
            return CODEC;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Ingredient Icon");
            builder.setDescription("Circles through the items of an ingredient.");

            builder.addProperty("ingredient", Ingredient.class)
                    .description("Ingredient for the item")
                    .required().exampleJson(Ingredient.CODEC.encodeStart(JsonOps.COMPRESSED, Ingredient.of(ItemTags.ARROWS)).getOrThrow());
        }

    }

}
