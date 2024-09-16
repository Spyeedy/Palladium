package net.threetag.palladium.entity.value;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.registry.PalladiumRegistries;

public abstract class EntityDependentNumber {

    public static final Codec<EntityDependentNumber> CODEC = Codec.withAlternative(
            PalladiumRegistries.ENTITY_DEPENDENT_NUMBER_TYPE.byNameCodec()
                    .dispatch(EntityDependentNumber::getType, EntityDependentNumberType::codec),
            Codec.DOUBLE.xmap(EntityDependentNumber::staticValue, EntityDependentNumber::getFromStatic)
    );

    private static double getFromStatic(StaticNumber staticNumber) {
        return staticNumber.getValue();
    }

    public abstract double get(Entity entity);

    public int asInt(Entity entity) {
        return (int) this.get(entity);
    }

    public float asFloat(Entity entity) {
        return (float) this.get(entity);
    }

    public abstract EntityDependentNumberType<?> getType();

    public boolean isDynamic() {
        return true;
    }

    public static StaticNumber staticValue(double value) {
        return new StaticNumber(value);
    }

    public static EntityDependentNumber score(String objectiveName, double fallbackValue) {
        return new ScoreNumber(objectiveName, fallbackValue);
    }

}
