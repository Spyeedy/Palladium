package net.threetag.palladium.power.provider;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.*;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Objects;

public class ItemPowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector) {
        if (entity instanceof LivingEntity living) {
            var registry = entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                var stack = living.getItemBySlot(slot);
                if (stack.has(PalladiumDataComponents.Items.POWERS.get())) {
                    var itemPowers = stack.get(PalladiumDataComponents.Items.POWERS.get());

                    for (ResourceLocation powerId : Objects.requireNonNull(itemPowers).forSlot(PlayerSlot.get(slot))) {
                        registry.get(powerId).ifPresent(power -> collector.addPower(power, () -> new Validator(slot, powerId)));
                    }
                }
            }
        }
    }

    public record Validator(EquipmentSlot slot, ResourceLocation id) implements PowerValidator {

        @Override
        public boolean stillValid(LivingEntity entity, Holder<Power> power) {
            var itemPowers = entity.getItemBySlot(this.slot).get(PalladiumDataComponents.Items.POWERS.get());

            if (itemPowers != null) {
                return itemPowers.forSlot(PlayerSlot.get(this.slot)).contains(this.id);
            }

            return false;
        }
    }

}
