package net.threetag.palladium.entity.data;

import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Predicate;

public interface PalladiumEntityDataType<T extends PalladiumEntityData<? extends Entity>> {

    T make(Entity entity);

    class Builder<T extends PalladiumEntityData<?>> {

        private final Function<Entity, T> function;
        private Predicate<Entity> predicate;

        public Builder(Function<Entity, T> function) {
            this.function = function;
        }

        public Builder<T> predicate(Predicate<Entity> predicate) {
            this.predicate = predicate;
            return this;
        }

        public PalladiumEntityDataType<T> build() {
            return entity -> {
                if (predicate != null) {
                    return this.predicate.test(entity) ? this.function.apply(entity) : null;
                } else {
                    return this.function.apply(entity);
                }
            };
        }
    }

}
