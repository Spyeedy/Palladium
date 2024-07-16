package net.threetag.palladium.entity.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;

public class StaticNumber extends EntityDependentNumber {

    public static final MapCodec<StaticNumber> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(Codec.DOUBLE.fieldOf("value").forGetter(n -> n.value))
            .apply(instance, StaticNumber::new));

    private final double value;

    public StaticNumber(double value) {
        this.value = value;
    }

    @Override
    public double get(Entity entity) {
        return this.value;
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public EntityDependentNumberType<?> getType() {
        return EntityDependentNumberTypes.STATIC.get();
    }

    public static class Type extends EntityDependentNumberType<StaticNumber> {

        @Override
        public MapCodec<StaticNumber> codec() {
            return CODEC;
        }

    }
}
