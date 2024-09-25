package net.threetag.palladium.util.icon;

import com.google.gson.JsonArray;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;

public record CompoundIcon(List<Icon> icons) implements Icon {

    public static final MapCodec<CompoundIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(Icon.CODEC.listOf().fieldOf("icons").forGetter(CompoundIcon::icons))
            .apply(instance, CompoundIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompoundIcon> STREAM_CODEC = StreamCodec.composite(
            Icon.STREAM_CODEC.apply(ByteBufCodecs.list()), CompoundIcon::icons, CompoundIcon::new
    );

    @Override
    public void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        for (Icon icon : this.icons) {
            icon.draw(mc, guiGraphics, context, x, y, width, height);
        }
    }

    @Override
    public IconSerializer<CompoundIcon> getSerializer() {
        return IconSerializers.COMPOUND.get();
    }

    public static class Serializer extends IconSerializer<CompoundIcon> {

        @Override
        public MapCodec<CompoundIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CompoundIcon> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Compound Icon");
            builder.setDescription("Let's you merge multiple icons into one.");

            JsonArray jsonArray = new JsonArray();
            jsonArray.add(Icon.CODEC.encodeStart(JsonOps.COMPRESSED, new ItemIcon(Items.APPLE)).getOrThrow().toString());
            jsonArray.add(Icon.CODEC.encodeStart(JsonOps.COMPRESSED, new TexturedIcon(ResourceLocation.parse("example:textures/icons/my_icon.png"))).getOrThrow().toString());
            builder.addProperty("icons", Icon[].class)
                    .description("Array of the icons you want to merge")
                    .required().exampleJson(jsonArray);
        }

    }
}
