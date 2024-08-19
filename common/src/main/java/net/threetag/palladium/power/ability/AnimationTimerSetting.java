package net.threetag.palladium.power.ability;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AnimationTimerSetting(int min, int max) {

    private static final Codec<AnimationTimerSetting> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("min").forGetter(AnimationTimerSetting::min),
                    Codec.INT.fieldOf("max").forGetter(AnimationTimerSetting::max)
            ).apply(instance, AnimationTimerSetting::new));

    public static final Codec<AnimationTimerSetting> CODEC = Codec.either(DIRECT_CODEC, Codec.INT).xmap(
            either -> either.map(
                    left -> left,
                    right -> new AnimationTimerSetting(right, right)
            ),
            setting -> setting.min == 0 ? Either.right(setting.max) : Either.left(setting)
    );

}
