package net.threetag.palladium.util.icon;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.GuiUtil;
import net.threetag.palladium.util.context.DataContext;

public class ItemIcon implements Icon {

    public static final MapCodec<ItemIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(ItemStack.CODEC.fieldOf("item").forGetter(ItemIcon::getItem))
            .apply(instance, ItemIcon::new));

    public final ItemStack stack;

    public ItemIcon(ItemStack stack) {
        this.stack = stack;
    }

    public ItemIcon(ItemLike itemLike) {
        this.stack = new ItemStack(itemLike);
    }

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + width / 2D, y + height / 2D, 100);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            guiGraphics.pose().scale(s / 16F, s / 16F, s / 16F);
        }

        var item = this.stack;

        if (item.isEmpty()) {
            var contextItem = context.getItem();

            if (!contextItem.isEmpty()) {
                item = contextItem;
            } else {
                item = new ItemStack(Items.BARRIER);
            }
        }

        GuiUtil.drawItem(guiGraphics, item, 0, true, null);
        guiGraphics.pose().popPose();
    }

    @Override
    public IconSerializer<ItemIcon> getSerializer() {
        return IconSerializers.ITEM.get();
    }

    public ItemStack getItem() {
        return this.stack;
    }

    @Override
    public String toString() {
        return "ItemIcon{" + "stack=" + stack + '}';
    }

    public static class Serializer extends IconSerializer<ItemIcon> {

        @Override
        public MapCodec<ItemIcon> codec() {
            return CODEC;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Item Icon");
            builder.setDescription("Uses an item as an icon.");

            builder.addProperty("item", ResourceLocation.class)
                    .description("ID of the item that's supposed to be displayed. If you leave it out, it will display the item from the current context (if given).")
                    .fallback(ResourceLocation.withDefaultNamespace("air")).exampleJson(new JsonPrimitive("minecraft:apple"));
        }
    }

}
