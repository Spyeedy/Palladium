package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodecUtils {

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

    public static final Codec<Vec2> VEC2_CODEC = Codec.FLOAT.listOf().comapFlatMap((list) -> Util.fixedSize(list, 2).map((floats) -> new Vec2(floats.getFirst(), floats.get(1))), (vec2) -> List.of(vec2.x, vec2.y));

    public static <T> Codec<List<T>> listOrPrimitive(Codec<T> codec) {
        return Codec.withAlternative(
            codec.listOf(),
            codec.xmap(Collections::singletonList, List::getFirst)
        );
    }

}
