package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.DocumentedCodec;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.context.DataContext;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public abstract class ConditionSerializer<T extends Condition> implements DocumentedCodec<T> {

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

    public ConditionEnvironment getContextEnvironment() {
        return ConditionEnvironment.ALL;
    }

    public static boolean checkConditions(Collection<Condition> conditions, DataContext context) {
        for (Condition condition : conditions) {
            if (!condition.active(context)) {
                return false;
            }
        }

        return true;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(Palladium.id("conditions"), "Conditions")
                .add(HTMLBuilder.heading("Conditions"))
                .addDocumentationSettings(PalladiumRegistries.CONDITION_SERIALIZER.stream().sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    @Override
    public ResourceLocation getId() {
        return PalladiumRegistries.CONDITION_SERIALIZER.getKey(this);
    }

    public String getDocumentationDescription() {
        return "";
    }

}
