package net.threetag.palladium.power.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerCollector;
import net.threetag.palladium.power.PowerValidator;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.property.PalladiumProperties;

public class SuperpowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        for (ResourceLocation id : PalladiumProperties.SUPERPOWER_IDS.get(entity)) {
            Power power = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).get(id);

            if (power != null) {
                collector.addPower(power, Validator::new);
            }
        }
    }

    public static class Validator implements PowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Power power) {
            var stored = PalladiumProperties.SUPERPOWER_IDS.get(entity);
            var powerId = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power);
            return stored != null && stored.contains(powerId);
        }
    }

}
