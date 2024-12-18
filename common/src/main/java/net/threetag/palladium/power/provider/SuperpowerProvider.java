package net.threetag.palladium.power.provider;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.*;

public class SuperpowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        collector.addPower(SuperpowerUtil.getHandler(entity).getSuperpower(), Validator::new);
    }

    public static class Validator implements PowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Holder<Power> power) {
            var current = SuperpowerUtil.getHandler(entity).getSuperpower();
            return current != null && current.is(power);
        }
    }

}
