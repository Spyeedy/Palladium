package net.threetag.palladium.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.threetag.palladium.core.registry.SimpleRegister;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.function.Function;

public class ItemTypes {

    public static void init() {
        SimpleRegister.register(PalladiumRegistryKeys.ITEM_TYPE, ResourceLocation.withDefaultNamespace("item"), ITEM_CODEC);
        SimpleRegister.register(PalladiumRegistryKeys.ITEM_TYPE, ResourceLocation.withDefaultNamespace("block_item"), BLOCK_ITEM_CODEC);
    }

    static <B extends Item> RecordCodecBuilder<B, Item.Properties> propertiesCodec() {
        return ItemPropertiesCodec.CODEC.fieldOf("properties").forGetter(item -> ((PalladiumItemExtension) item).palladium$getProperties());
    }

    static <B extends Item> MapCodec<B> simpleCodec(Function<Item.Properties, B> factory) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec()).apply(instance, factory));
    }

    public static final MapCodec<Item> ITEM_CODEC = simpleCodec(Item::new);
    public static final MapCodec<BlockItem> BLOCK_ITEM_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlockItem::getBlock),
            propertiesCodec()
    ).apply(instance, BlockItem::new));

    public static final Codec<Item> CODEC = PalladiumRegistries.ITEM_TYPE.byNameCodec().dispatch(i -> ITEM_CODEC, Function.identity());
}
