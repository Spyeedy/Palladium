package net.threetag.palladium.power.provider;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.power.*;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.List;

public class SuitSetPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        for (SuitSet suitSet : PalladiumRegistries.SUIT_SET) {
            if (suitSet.isWearing(entity)) {
                List<ResourceLocation> powers = SuitSetPowerManager.getInstance().getPowerForSuitSet(suitSet);

                if (powers != null) {
                    for (ResourceLocation powerId : powers) {
                        var power = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getHolder(powerId);
                        power.ifPresent(powerReference -> collector.addPower(powerReference, () -> new Validator(suitSet)));
                    }
                }
            }
        }
    }

    public record Validator(SuitSet suitSet) implements PowerValidator {

        @Override
            public boolean stillValid(LivingEntity entity, Holder<Power> power) {
                return this.suitSet.isWearing(entity);
            }
        }

}
