package net.threetag.palladium.util.icon;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.DocumentedConfigurable;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.Comparator;
import java.util.stream.Collectors;

public abstract class IconSerializer<T extends Icon> implements DocumentedConfigurable {

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(Palladium.id("icons"), "Icons")
                .add(HTMLBuilder.heading("Icons"))
                .addDocumentationSettings(PalladiumRegistries.ICON_SERIALIZER.stream().sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    @Override
    public ResourceLocation getId() {
        return PalladiumRegistries.ICON_SERIALIZER.getKey(this);
    }
}
