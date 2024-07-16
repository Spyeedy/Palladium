package net.threetag.palladium.util.property;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.power.ability.AbilityColor;
import net.threetag.palladium.power.ability.AbilityDescription;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.icon.Icon;

import java.util.HashMap;
import java.util.Map;

public class PalladiumPropertyType<T> {

    private static final Map<String, PalladiumPropertyType<?>> TYPES = new HashMap<>();

    public static final PalladiumPropertyType<String> STRING = register("string", Codec.STRING);
    public static final PalladiumPropertyType<Integer> INTEGER = register("integer", Codec.INT);
    public static final PalladiumPropertyType<Float> FLOAT = register("float", Codec.FLOAT);
    public static final PalladiumPropertyType<Double> DOUBLE = register("double", Codec.DOUBLE);
    public static final PalladiumPropertyType<Boolean> BOOLEAN = register("boolean", Codec.BOOL);
    public static final PalladiumPropertyType<Vec2> VEC2 = register("vec2", CodecUtils.VEC2_CODEC);
    public static final PalladiumPropertyType<Icon> ICON = register("icon", Icon.CODEC);
    public static final PalladiumPropertyType<Component> COMPONENT = register("component", ComponentSerialization.CODEC);
    public static final PalladiumPropertyType<AbilityReference> ABILITY_REFERENCE = register("ability_reference", AbilityReference.CODEC);
    public static final PalladiumPropertyType<AbilityDescription> ABILITY_DESCRIPTION = register("ability_description", AbilityDescription.CODEC);
    public static final PalladiumPropertyType<AbilityColor> ABILITY_COLOR = register("ability_color", AbilityColor.CODEC);

    public static <T> PalladiumPropertyType<T> register(String name, Codec<T> codec) {
        if (TYPES.containsKey(name))
            throw new IllegalArgumentException("Attempted to overwrite palladium property type " + name + ", this is not allowed.");

        var type = new PalladiumPropertyType<>(name, codec);
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

    private PalladiumPropertyType(String name, Codec<T> codec) {
        this.name = name;
        this.codec = codec;
    }

    public String getName() {
        return this.name;
    }

    public Codec<T> getCodec() {
        return this.codec;
    }
}
