package net.threetag.palladium.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.Collections;
import java.util.Optional;

public class CreativeModeTabCodec {

    public static final Codec<CreativeModeTab> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("name").forGetter(CreativeModeTab::getDisplayName),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(tab -> BuiltInRegistries.ITEM.getKey(tab.getIconItem().getItem())),
            ResourceLocation.CODEC.optionalFieldOf("background_texture").forGetter(tab -> Optional.of(tab.getBackgroundTexture())),
            ResourceLocation.CODEC.listOf().optionalFieldOf("items", Collections.emptyList()).forGetter(tab -> tab.getDisplayItems().stream().map(item -> BuiltInRegistries.ITEM.getKey(item.getItem())).toList())
    ).apply(instance, (name, icon, bg, items) -> CreativeTabRegistry.create(builder -> {
        builder.title(name);
        builder.icon(() -> BuiltInRegistries.ITEM.getValue(icon).getDefaultInstance());
        bg.ifPresent(builder::backgroundTexture);
        builder.displayItems((itemDisplayParameters, output) -> {
            for (ResourceLocation item : items) {
                output.accept(BuiltInRegistries.ITEM.getValue(item).getDefaultInstance());
            }
        });
    })));

}
