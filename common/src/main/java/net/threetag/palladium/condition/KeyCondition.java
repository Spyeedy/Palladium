package net.threetag.palladium.condition;

import net.threetag.palladium.power.ability.AbilityConditions;

public abstract class KeyCondition implements Condition {

    public final int cooldown;
    public final AbilityConditions.KeyType type;
    public final boolean needsEmptyHand;
    public final boolean allowScrollingWhenCrouching;

    public KeyCondition(int cooldown, AbilityConditions.KeyType type, boolean needsEmptyHand, boolean allowScrollingWhenCrouching) {
        this.cooldown = cooldown;
        this.type = type;
        this.needsEmptyHand = needsEmptyHand;
        this.allowScrollingWhenCrouching = allowScrollingWhenCrouching;
    }

    @Override
    public boolean needsKey() {
        return true;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    @Override
    public AbilityConditions.KeyType getKeyType() {
        return this.type;
    }

    public boolean needsEmptyHand() {
        return this.needsEmptyHand;
    }

    public boolean allowScrollingWhenCrouching() {
        return this.allowScrollingWhenCrouching;
    }

    @Override
    public boolean handlesCooldown() {
        return this.cooldown > 0;
    }

}
