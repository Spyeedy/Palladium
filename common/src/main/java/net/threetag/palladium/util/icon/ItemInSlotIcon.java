package net.threetag.palladium.util.icon;

import com.google.gson.JsonPrimitive;
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
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.context.DataContext;

public record ItemInSlotIcon(PlayerSlot slot) implements Icon {

    public static final MapCodec<ItemInSlotIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(PlayerSlot.CODEC.fieldOf("slot").forGetter(ItemInSlotIcon::slot))
            .apply(instance, ItemInSlotIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInSlotIcon> STREAM_CODEC = StreamCodec.composite(
            PlayerSlot.STREAM_CODEC, ItemInSlotIcon::slot, ItemInSlotIcon::new
    );

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        var stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(x + width / 2D, y + height / 2D, 100);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F, s / 16F);
        }

        var item = new ItemStack(Items.BARRIER);
        var items = this.slot.getItems(context.getLivingEntity());

        if (!items.isEmpty()) {
            var found = items.get(0);

            if (!found.isEmpty()) {
                item = found;
            }
        }

        mc.getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, 240, OverlayTexture.NO_OVERLAY, guiGraphics.pose(), mc.renderBuffers().bufferSource(), mc.level, 0);
        stack.popPose();
    }

    @Override
    public IconSerializer<ItemInSlotIcon> getSerializer() {
        return IconSerializers.ITEM_IN_SLOT.get();
    }

    @Override
    public String toString() {
        return "ItemIcon{" + "slot=" + this.slot.toString() + '}';
    }

    public static class Serializer extends IconSerializer<ItemInSlotIcon> {

        @Override
        public MapCodec<ItemInSlotIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemInSlotIcon> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Item in Slot Icon");
            builder.setDescription("Uses the item that's in the specified slot.");

            builder.addProperty("slot", String.class)
                    .description("Name of the slot.")
                    .required().exampleJson(new JsonPrimitive("chest"));
        }
    }

}
