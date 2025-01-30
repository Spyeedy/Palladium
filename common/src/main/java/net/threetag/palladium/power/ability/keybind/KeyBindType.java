package net.threetag.palladium.power.ability.keybind;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.client.gui.component.UiComponent;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public abstract class KeyBindType {

    public static final Codec<KeyBindType> CODEC = PalladiumRegistries.KEY_BIND_TYPE_SERIALIZER.byNameCodec().dispatch(KeyBindType::getSerializer, KeyBindTypeSerializer::codec);

    public static final StreamCodec<RegistryFriendlyByteBuf, KeyBindType> STREAM_CODEC = ByteBufCodecs.registry(PalladiumRegistryKeys.KEY_BIND_TYPE_SERIALIZER).dispatch(KeyBindType::getSerializer, KeyBindTypeSerializer::streamCodec);

    public abstract KeyBindTypeSerializer<?> getSerializer();

    @Environment(EnvType.CLIENT)
    public abstract UiComponent getDisplayedKey(AbilityInstance<?> abilityInstance, int index, boolean inside);
}
