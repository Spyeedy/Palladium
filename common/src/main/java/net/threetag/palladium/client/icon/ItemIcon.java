package net.threetag.palladium.client.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.threetag.palladium.data.DataContext;

public class ItemIcon implements Icon {

    public static final MapCodec<ItemIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(ItemStack.CODEC.fieldOf("item").forGetter(ItemIcon::getItem))
            .apply(instance, ItemIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemIcon> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ItemIcon::getItem, ItemIcon::new
    );

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

        mc.getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, 240, OverlayTexture.NO_OVERLAY, guiGraphics.pose(), mc.renderBuffers().bufferSource(), mc.level, 0);
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
        public StreamCodec<RegistryFriendlyByteBuf, ItemIcon> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
