package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyBuilder;
import net.threetag.palladium.util.property.PalladiumPropertyType;

public class HealingAbility extends Ability {

    public static final PalladiumProperty<Integer> FREQUENCY = PalladiumPropertyBuilder.create("frequency", PalladiumPropertyType.INTEGER).configurable("Sets the frequency of healing (in ticks)", false, 20).build();
    public static final PalladiumProperty<Float> AMOUNT = PalladiumPropertyBuilder.create("amount", PalladiumPropertyType.FLOAT).configurable("Sets the amount of hearts for each healing", false, 3F).build();

    public HealingAbility() {
        this.withProperty(FREQUENCY, AMOUNT);
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (enabled && !entity.level().isClientSide) {
            int frequency = entry.getProperty(FREQUENCY);
            if (frequency != 0 && entity.tickCount % frequency == 0) {
                entity.heal(entry.getProperty(AMOUNT));
            }
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Heals the entity.";
    }
}
