package net.threetag.palladium.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class CodecExtras {

    public static final Codec<Float> NON_NEGATIVE_FLOAT = Codec.FLOAT.validate(f -> f >= 0 ? DataResult.success(f) : DataResult.error(() -> "Value must be non-negative: " + f));

    /**
     * Codec for colors. Does NOT support alpha
     */
    public static final Codec<Color> COLOR_CODEC = Codec.withAlternative(
            Codec.INT.xmap(Color::new, Color::getRGB),
            Codec.withAlternative(
                    Codec.INT.listOf(3, 3).xmap(integers -> new Color(integers.getFirst(), integers.get(1), integers.get(2)), color -> {
                        List<Integer> integers = new ArrayList<>();
                        integers.add(color.getRed());
                        integers.add(color.getGreen());
                        integers.add(color.getBlue());
                        return integers;
                    }),
                    Codec.STRING.xmap(s -> Color.decode(s.startsWith("#") ? s.substring(1) : s), color -> "#" + Integer.toHexString(color.getRGB()))
            )
    );
    public static final StreamCodec<ByteBuf, Color> COLOR_STREAM_CODEC = ByteBufCodecs.VAR_INT.map(Color::new, Color::getRGB);

    public static final Codec<Vec2> VEC2_CODEC = Codec.FLOAT.listOf().comapFlatMap((list) -> Util.fixedSize(list, 2).map((floats) -> new Vec2(floats.getFirst(), floats.get(1))), (vec2) -> List.of(vec2.x, vec2.y));
    public static final StreamCodec<ByteBuf, Vec2> VEC2_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, v -> v.x, ByteBufCodecs.FLOAT, v -> v.y, Vec2::new);

    public static <T> Codec<List<T>> listOrPrimitive(Codec<T> codec) {
        return Codec.either(codec.listOf(), codec).xmap(
                either -> either.map(
                        left -> left,
                        Collections::singletonList
                ),
                list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list)
        );
    }

    private static Codec<Float> floatRangeWithMessage(float min, float max, Function<Float, String> errorMessage) {
        return Codec.FLOAT.validate((integer) -> {
            return integer.compareTo(min) >= 0 && integer.compareTo(max) <= 0 ? DataResult.success(integer) : DataResult.error(() -> {
                return (String) errorMessage.apply(integer);
            });
        });
    }

}
