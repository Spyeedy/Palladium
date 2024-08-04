package net.threetag.palladium.power;

import net.minecraft.world.entity.LivingEntity;

public interface PowerValidator {

    PowerValidator ALWAYS_ACTIVE = (entity, power) -> true;

    boolean stillValid(LivingEntity entity, Power power);

}
