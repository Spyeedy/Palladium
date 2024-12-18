package net.threetag.palladium.condition;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityInstance;

public abstract class BuyableCondition implements Condition {

    @Override
    public void registerDataComponents(DataComponentMap.Builder components) {
        components.set(PalladiumDataComponents.Abilities.BOUGHT.get(), false);
    }

    @Override
    public boolean active(DataContext context) {
        var instance = context.get(DataContextType.ABILITY_INSTANCE);
        return instance != null && instance.getOrDefault(PalladiumDataComponents.Abilities.BOUGHT.get(), false);
    }

    public abstract Ability.UnlockData createData();

    /**
     * @return Returns true if the object that is required to activate the condition is available in the player
     */
    public abstract boolean isAvailable(LivingEntity entity);

    /**
     * Takes the required object from the entity to activate the condition
     *
     * @return Returns true if it was successful
     */
    public abstract boolean takeFromEntity(LivingEntity entity);

    public void buy(LivingEntity entity, AbilityInstance<?> instance) {
        if (isAvailable(entity) && takeFromEntity(entity)) {
            instance.set(PalladiumDataComponents.Abilities.BOUGHT.get(), true);
        }
    }
}
