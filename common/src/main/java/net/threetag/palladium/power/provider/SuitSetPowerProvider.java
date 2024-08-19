package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.power.*;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.List;

public class SuitSetPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        for (SuitSet suitSet : PalladiumRegistries.SUIT_SET) {
            if (suitSet.isWearing(entity)) {
                List<Power> powers = SuitSetPowerManager.getInstance().getPowerForSuitSet(suitSet);

                if (powers != null) {
                    for (Power power : powers) {
                        collector.addPower(power, () -> new Validator(suitSet));
                    }
                }
            }
        }
    }

    public record Validator(SuitSet suitSet) implements PowerValidator {

        @Override
            public boolean stillValid(LivingEntity entity, Power power) {
                return this.suitSet.isWearing(entity);
            }
        }

}
