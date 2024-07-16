package net.threetag.palladium.entity.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumPropertyType;

public class PalladiumPropertyNumber extends EntityDependentNumber {

    public static final MapCodec<PalladiumPropertyNumber> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    Codec.STRING.fieldOf("property").forGetter(n -> n.property),
                    Codec.DOUBLE.fieldOf("fallback").forGetter(n -> n.fallback)
            )
            .apply(instance, PalladiumPropertyNumber::new));

    private final String property;
    private final double fallback;

    public PalladiumPropertyNumber(String property, double fallback) {
        this.property = property;
        this.fallback = fallback;
    }

    @Override
    public double get(Entity entity) {
        var opt = EntityPropertyHandler.getHandler(entity);

        if (opt.isPresent()) {
            var handler = opt.get();
            var property = handler.getPropertyByName(this.property);
            Object value = null;

            if (property.getType() == PalladiumPropertyType.INTEGER || property.getType() == PalladiumPropertyType.FLOAT || property.getType() == PalladiumPropertyType.DOUBLE) {
                value = handler.get(property);
            }

            return value == null ? this.fallback : (double) value;
        }

        return this.fallback;
    }

    @Override
    public EntityDependentNumberType<?> getType() {
        return EntityDependentNumberTypes.PALLADIUM_PROPERTY.get();
    }

    public static class Type extends EntityDependentNumberType<PalladiumPropertyNumber> {

        @Override
        public MapCodec<PalladiumPropertyNumber> codec() {
            return CODEC;
        }

    }
}
