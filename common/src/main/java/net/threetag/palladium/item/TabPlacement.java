package net.threetag.palladium.item;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record TabPlacement(Type type, ResourceLocation tab, @Nullable ResourceLocation referencedItem) {

    private static final Codec<TabPlacement> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Type.CODEC.optionalFieldOf("type", Type.ADD).forGetter(TabPlacement::type),
            ResourceLocation.CODEC.fieldOf("tab").forGetter(TabPlacement::tab),
            ResourceLocation.CODEC.optionalFieldOf("target").forGetter(p -> Optional.ofNullable(p.referencedItem))
    ).apply(instance, (type, tab, item) -> new TabPlacement(type, tab, item.orElse(null))));

    public static final Codec<TabPlacement> CODEC = Codec.either(DIRECT_CODEC, ResourceLocation.CODEC).xmap(
            either -> either.map(
                    tabPlacement -> tabPlacement,
                    id -> new TabPlacement(Type.ADD, id, null)
            ),
            tabPlacement -> tabPlacement.type() != Type.ADD ? Either.left(tabPlacement) : Either.right(tabPlacement.tab())
    );

    @SuppressWarnings("UnstableApiUsage")
    public void add(Item item) {
        CreativeTabRegistry.modify(CreativeTabRegistry.defer(this.tab), (flags, output, canUseGameMasterBlocks) -> {
            if (this.type == Type.ADD) {
                output.accept(item.getDefaultInstance());
            } else {
                var refItem = BuiltInRegistries.ITEM.getOptional(this.referencedItem);

                if (refItem.isEmpty() || refItem.get() == Items.AIR) {
                    AddonPackLog.warning("Could not find item with id " + this.referencedItem);
                    return;
                }

                if (this.type == Type.ADD_AFTER) {
                    output.acceptAfter(refItem.get(), item);
                } else {
                    output.acceptBefore(refItem.get(), item);
                }
            }
        });
    }

    public static void loadTabs() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (((PalladiumItemExtension) item).palladium$getProperties() instanceof ItemPropertiesCodec.ExtendedProperties properties) {
                for (TabPlacement tab : properties.getTabs()) {
                    tab.add(item);
                }
            }
        }
    }

    public enum Type implements StringRepresentable {

        ADD("add"),
        ADD_AFTER("add_after"),
        ADD_BEFORE("add_before");

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

}
