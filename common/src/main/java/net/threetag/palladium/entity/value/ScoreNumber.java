package net.threetag.palladium.entity.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;

public class ScoreNumber extends EntityDependentNumber {

    public static final MapCodec<ScoreNumber> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    Codec.STRING.fieldOf("objective").forGetter(n -> n.objective),
                    Codec.DOUBLE.fieldOf("fallback").forGetter(n -> n.fallback)
            )
            .apply(instance, ScoreNumber::new));

    private final String objective;
    private final double fallback;

    public ScoreNumber(String objective, double fallback) {
        this.objective = objective;
        this.fallback = fallback;
    }

    @Override
    public double get(Entity entity) {
        var scoreboard = entity.level().getScoreboard();
        var objective = scoreboard.getObjective(this.objective);

        if (objective != null) {
            var info = scoreboard.getPlayerScoreInfo(entity, objective);
            return info != null ? info.value() : this.fallback;
        }

        return this.fallback;
    }

    @Override
    public EntityDependentNumberType<?> getType() {
        return EntityDependentNumberTypes.SCORE.get();
    }

    public static class Type extends EntityDependentNumberType<ScoreNumber> {

        @Override
        public MapCodec<ScoreNumber> codec() {
            return CODEC;
        }

    }
}
