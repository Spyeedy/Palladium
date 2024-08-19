package net.threetag.palladium.util.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.power.ability.AbilityColor;
import net.threetag.palladium.power.ability.AbilityDescription;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladium.util.ArmSetting;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.ParsedCommands;
import net.threetag.palladium.util.Utils;
import net.threetag.palladium.util.icon.Icon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    public static final PalladiumPropertyType<Vec3> VEC3 = register("vec3", Vec3.CODEC, ByteBufCodecs.collection(Utils::newList, ByteBufCodecs.DOUBLE, 3).map(doubles -> new Vec3(doubles.getFirst(), doubles.get(1), doubles.get(2)), vec3 -> Arrays.asList(vec3.x, vec3.y, vec3.z)));
    public static final PalladiumPropertyType<Icon> ICON = register("icon", Icon.CODEC, Icon.STREAM_CODEC);
    public static final PalladiumPropertyType<Component> COMPONENT = register("component", ComponentSerialization.CODEC, ComponentSerialization.STREAM_CODEC);
    public static final PalladiumPropertyType<AbilityReference> ABILITY_REFERENCE = register("ability_reference", AbilityReference.CODEC, AbilityReference.STREAM_CODEC);
    public static final PalladiumPropertyType<EnergyBarReference> ENERGY_BAR_REFERENCE = register("energy_bar_reference", EnergyBarReference.CODEC, EnergyBarReference.STREAM_CODEC);
    public static final PalladiumPropertyType<AbilityDescription> ABILITY_DESCRIPTION = register("ability_description", AbilityDescription.CODEC, AbilityDescription.STREAM_CODEC);
    public static final PalladiumPropertyType<AbilityColor> ABILITY_COLOR = register("ability_color", AbilityColor.CODEC, AbilityColor.STREAM_CODEC);
    public static final PalladiumPropertyType<ArmSetting> ARM_SETTING = register("arm_setting", ArmSetting.CODEC, ArmSetting.STREAM_CODEC);
    public static final PalladiumPropertyType<AttributeModifier.Operation> ATTRIBUTE_MODIFIER_OPERATION = register("attribute_modifier_operation", AttributeModifier.Operation.CODEC, AttributeModifier.Operation.STREAM_CODEC);
    public static final PalladiumPropertyType<ParsedCommands> PARSED_COMMANDS = register("parsed_commands", ParsedCommands.CODEC, ParsedCommands.STREAM_CODEC);

    public static final PalladiumPropertyType<Holder<Attribute>> ATTRIBUTE = register("attribute", Attribute.CODEC, Attribute.STREAM_CODEC);
    public static final PalladiumPropertyType<List<Holder<DamageType>>> DAMAGE_TYPES = register("damage_types", CodecUtils.listOrPrimitive(DamageType.CODEC), ByteBufCodecs.collection(Utils::newList, DamageType.STREAM_CODEC));

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
