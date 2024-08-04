package net.threetag.palladium.util.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.power.ability.AbilityColor;
import net.threetag.palladium.power.ability.AbilityDescription;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.icon.Icon;

import java.util.HashMap;
import java.util.Map;

public class PalladiumPropertyType<T> {

    private static final Map<String, PalladiumPropertyType<?>> TYPES = new HashMap<>();

    public static final PalladiumPropertyType<String> STRING = register("string", Codec.STRING, ByteBufCodecs.STRING_UTF8);
    public static final PalladiumPropertyType<ResourceLocation> RESOURCE_LOCATION = register("resource_location", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC);
    public static final PalladiumPropertyType<Integer> INTEGER = register("integer", Codec.INT, ByteBufCodecs.VAR_INT);
    public static final PalladiumPropertyType<Float> FLOAT = register("float", Codec.FLOAT, ByteBufCodecs.FLOAT);
    public static final PalladiumPropertyType<Double> DOUBLE = register("double", Codec.DOUBLE, ByteBufCodecs.DOUBLE);
    public static final PalladiumPropertyType<Boolean> BOOLEAN = register("boolean", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final PalladiumPropertyType<Vec2> VEC2 = register("vec2", CodecUtils.VEC2_CODEC, CodecUtils.VEC2_STREAM_CODEC);
    public static final PalladiumPropertyType<Icon> ICON = register("icon", Icon.CODEC, Icon.STREAM_CODEC);
    public static final PalladiumPropertyType<Component> COMPONENT = register("component", ComponentSerialization.CODEC, ComponentSerialization.STREAM_CODEC);
    public static final PalladiumPropertyType<AbilityReference> ABILITY_REFERENCE = register("ability_reference", AbilityReference.CODEC, AbilityReference.STREAM_CODEC);
    public static final PalladiumPropertyType<EnergyBarReference> ENERGY_BAR_REFERENCE = register("energy_bar_reference", EnergyBarReference.CODEC, EnergyBarReference.STREAM_CODEC);
    public static final PalladiumPropertyType<AbilityDescription> ABILITY_DESCRIPTION = register("ability_description", AbilityDescription.CODEC, AbilityDescription.STREAM_CODEC);
    public static final PalladiumPropertyType<AbilityColor> ABILITY_COLOR = register("ability_color", AbilityColor.CODEC, AbilityColor.STREAM_CODEC);

    public static <T, R extends ByteBuf> PalladiumPropertyType<T> register(String name, Codec<T> codec, StreamCodec<R, T> streamCodec) {
        if (TYPES.containsKey(name))
            throw new IllegalArgumentException("Attempted to overwrite palladium property type " + name + ", this is not allowed.");

        var type = new PalladiumPropertyType<>(name, codec, streamCodec);
        TYPES.put(name, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <T> PalladiumPropertyType<T> getType(String name) {
        return (PalladiumPropertyType<T>) TYPES.get(name);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private final String name;
    private final Codec<T> codec;
    private final StreamCodec<? extends ByteBuf, T> streamCodec;

    private PalladiumPropertyType(String name, Codec<T> codec, StreamCodec<? extends ByteBuf, T> streamCodec) {
        this.name = name;
        this.codec = codec;
        this.streamCodec = streamCodec;
    }

    public String getName() {
        return this.name;
    }

    public Codec<T> getCodec() {
        return this.codec;
    }

    public StreamCodec<? extends ByteBuf, T> getStreamCodec() {
        return this.streamCodec;
    }
}
