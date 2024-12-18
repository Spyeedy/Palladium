package net.threetag.palladium.entity.data;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface PalladiumLivingEntityDataType<T extends PalladiumEntityData<? extends LivingEntity>> extends PalladiumEntityDataType<T> {

    T makeForLiving(LivingEntity entity);

    @Override
    default T make(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return this.makeForLiving(livingEntity);
        } else {
            return null;
        }
    }
}
