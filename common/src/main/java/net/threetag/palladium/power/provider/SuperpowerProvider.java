package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.PowerCollector;

public class SuperpowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
//        for (ResourceLocation id : PalladiumProperties.SUPERPOWER_IDS.get(entity)) {
//            Power power = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).get(id);
//
//            if (power != null) {
//                collector.addPower(power, Validator::new);
//            }
//        }
    }

//    public static class Validator implements PowerValidator {
//
//        @Override
//        public boolean stillValid(LivingEntity entity, Holder<Power> power) {
//            var stored = PalladiumProperties.SUPERPOWER_IDS.get(entity);
//            var powerId = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power);
//            return stored != null && stored.contains(powerId);
//        }
//    }

}
