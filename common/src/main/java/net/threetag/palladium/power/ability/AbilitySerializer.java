package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.documentation.DocumentedCodec;
import net.threetag.palladium.registry.PalladiumRegistries;

public abstract class AbilitySerializer<T extends Ability> implements DocumentedCodec<T> {

    @Override
    public ResourceLocation getId() {
        return PalladiumRegistries.ABILITY_SERIALIZER.getKey(this);
    }

    public abstract MapCodec<T> codec();

//    public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}
