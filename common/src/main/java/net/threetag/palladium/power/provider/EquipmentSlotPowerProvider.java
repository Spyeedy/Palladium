package net.threetag.palladium.power.provider;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.threetag.palladium.power.*;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.List;

public class EquipmentSlotPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            List<ResourceLocation> powers = ItemPowerManager.getInstance().getPowerForItem(slot.getName(), entity.getItemBySlot(slot).getItem());

            if (powers != null) {
                for (ResourceLocation powerId : powers) {
                    var power = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getHolder(powerId);
                    power.ifPresent(powerReference -> collector.addPower(powerReference, () -> new Validator(entity.getItemBySlot(slot).getItem(), slot)));
                }
            }
        }
    }

    public record Validator(Item item, EquipmentSlot slot) implements PowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Holder<Power> power) {
            return entity.getItemBySlot(this.slot).is(this.item);
        }
    }

}
