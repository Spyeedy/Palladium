package net.threetag.palladium.power.ability;

import net.minecraft.core.component.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.CooldownType;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class AbilityInstance<T extends Ability> implements DataComponentHolder {

    private final T ability;
    private final PowerHolder holder;
    private int lifetime = 0;
    private int prevEnabledTicks = 0;
    private int enabledTicks = 0;
    private PatchedDataComponentMap components;
    private final AbilityReference reference;
    private final AnimationTimer animationTimer;

    public AbilityInstance(T ability, PowerHolder holder) {
        this.ability = ability;
        this.holder = holder;
        this.reference = new AbilityReference(holder.getPowerId(), ability.getKey());
        this.animationTimer = ability.getProperties().getAnimationTimerSetting() != null ? new AnimationTimer(ability.getProperties().getAnimationTimerSetting(), ability.getProperties().getAnimationTimerSetting().min()) : null;

        var componentsBuilder = DataComponentMap.builder().addAll(PalladiumDataComponents.Abilities.getCommonComponents());
        this.ability.registerDataComponents(componentsBuilder);
        this.ability.getConditions().getUnlockingConditions().forEach(condition -> condition.registerDataComponents(componentsBuilder));
        this.ability.getConditions().getEnablingConditions().forEach(condition -> condition.registerDataComponents(componentsBuilder));
        this.components = new PatchedDataComponentMap(componentsBuilder.build());
    }

    public AbilityInstance(T ability, PowerHolder holder, CompoundTag componentData) {
        this.ability = ability;
        this.holder = holder;
        this.reference = new AbilityReference(holder.getPowerId(), ability.getKey());
        this.animationTimer = ability.getProperties().getAnimationTimerSetting() != null ? new AnimationTimer(ability.getProperties().getAnimationTimerSetting(), ability.getProperties().getAnimationTimerSetting().min()) : null;

        var componentsBuilder = DataComponentMap.builder().addAll(PalladiumDataComponents.Abilities.getCommonComponents());
        this.ability.registerDataComponents(componentsBuilder);
        this.ability.getConditions().getUnlockingConditions().forEach(condition -> condition.registerDataComponents(componentsBuilder));
        this.ability.getConditions().getEnablingConditions().forEach(condition -> condition.registerDataComponents(componentsBuilder));
        this.components = PatchedDataComponentMap.fromPatch(componentsBuilder.build(), DataComponentPatch.CODEC.parse(this.holder.entity.registryAccess().createSerializationContext(NbtOps.INSTANCE), componentData).getOrThrow());
    }

    public T getAbility() {
        return this.ability;
    }

    public PowerHolder getHolder() {
        return this.holder;
    }

    public AbilityReference getReference() {
        return this.reference;
    }

    public boolean isUnlocked() {
        return this.components.getOrDefault(PalladiumDataComponents.Abilities.UNLOCKED.get(), true);
    }

    public boolean isEnabled() {
        return this.components.getOrDefault(PalladiumDataComponents.Abilities.ENABLED.get(), true);
    }

    public int getCooldown() {
        return this.components.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0);
    }

    public boolean isOnCooldown() {
        return this.ability.getConditions().getCooldownType() == CooldownType.STATIC ? this.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0) > 0 : this.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0) < this.getOrDefault(PalladiumDataComponents.Abilities.MAX_COOLDOWN.get(), 0);
    }

    public int getActivatedTime() {
        return this.components.getOrDefault(PalladiumDataComponents.Abilities.ACTIVATED_TIME.get(), 0);
    }

    public boolean isKeyPressed() {
        return this.components.getOrDefault(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
    }

    public int getEnabledTicks() {
        return this.enabledTicks;
    }

    public int getPrevEnabledTicks() {
        return this.prevEnabledTicks;
    }

    public void tick(LivingEntity entity, PowerHolder powerHolder) {
        this.prevEnabledTicks = this.enabledTicks;

        if (!entity.level().isClientSide) {
            if (this.lifetime == 0) {
                this.ability.getConditions().getUnlockingConditions().forEach(condition -> condition.init(entity, this));
                this.ability.getConditions().getEnablingConditions().forEach(condition -> condition.init(entity, this));
            }

            boolean unlocked = true;
            boolean sync = false;

            for (Condition unlockingCondition : this.ability.getConditions().getUnlockingConditions()) {
                if (!unlockingCondition.active(DataContext.forAbility(entity, this))) {
                    unlocked = false;
                    break;
                }
            }

            if (entity.isSpectator()) {
                unlocked = false;
            }

            if (this.isUnlocked() != unlocked) {
                this.set(PalladiumDataComponents.Abilities.UNLOCKED.get(), unlocked);
                sync = true;
            }

            boolean enabled = this.isUnlocked();

            if (this.isUnlocked()) {
                for (Condition enablingCondition : this.ability.getConditions().getEnablingConditions()) {
                    if (!enablingCondition.active(DataContext.forAbility(entity, this))) {
                        enabled = false;
                        break;
                    }
                }
            }

            if (entity.isSpectator()) {
                enabled = false;
            }

            if (this.isEnabled() != enabled) {
                if (!this.isEnabled()) {
                    this.set(PalladiumDataComponents.Abilities.ENABLED.get(), true);
                    this.ability.firstTick(entity, this, powerHolder, this.isEnabled());
                } else {
                    this.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
                    this.ability.lastTick(entity, this, powerHolder, this.isEnabled());
                    this.set(PalladiumDataComponents.Abilities.ENABLED.get(), false);
                }
            }
        }

        if (this.isEnabled()) {
            this.enabledTicks++;

            for (EnergyBarUsage usage : this.ability.getEnergyBarUsages()) {
                usage.consume(this.holder);
            }
        } else if (this.enabledTicks > 0) {
            this.enabledTicks--;
        }

        int cooldown = this.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0);

        if (this.ability.getConditions().getCooldownType() == CooldownType.STATIC) {
            if (cooldown > 0) {
                this.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), cooldown - 1);
            }
        } else if (this.ability.getConditions().getCooldownType() == CooldownType.DYNAMIC) {
            if (this.isEnabled() && cooldown > 0) {
                this.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), cooldown - 1);
            } else if (!this.isEnabled() && this.getOrDefault(PalladiumDataComponents.Abilities.COOLDOWN.get(), 0) < this.getOrDefault(PalladiumDataComponents.Abilities.MAX_COOLDOWN.get(), 0)) {
                this.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), cooldown + 1);
            }
        }

        var activationTimer = this.getOrDefault(PalladiumDataComponents.Abilities.ACTIVATED_TIME.get(), 0);
        if (activationTimer > 0) {
            this.set(PalladiumDataComponents.Abilities.ACTIVATED_TIME.get(), activationTimer - 1);
        }

        this.lifetime++;
        this.ability.tick(entity, this, powerHolder, this.isEnabled());

        if (this.animationTimer != null) {
            this.ability.animationTimerTick(entity, this, powerHolder, this.isEnabled(), this.animationTimer);
        }
    }

    public void startCooldown(LivingEntity entity, int cooldown) {
        this.set(PalladiumDataComponents.Abilities.COOLDOWN.get(), cooldown);
        this.set(PalladiumDataComponents.Abilities.MAX_COOLDOWN.get(), cooldown);
    }

    public void startActivationTimer(LivingEntity entity, int activationTimer) {
        this.set(PalladiumDataComponents.Abilities.ACTIVATED_TIME.get(), activationTimer);
        this.set(PalladiumDataComponents.Abilities.MAX_ACTIVATED_TIME.get(), activationTimer);
    }

    public void keyPressed(LivingEntity entity, boolean pressed) {
        for (Condition condition : this.ability.getConditions().getUnlockingConditions()) {
            if (condition.needsKey()) {
                if (pressed) {
                    condition.onKeyPressed(entity, this, holder);
                } else {
                    condition.onKeyReleased(entity, this, holder);
                }
                return;
            }
        }

        for (Condition condition : this.ability.getConditions().getEnablingConditions()) {
            if (condition.needsKey()) {
                if (pressed) {
                    condition.onKeyPressed(entity, this, holder);
                } else {
                    condition.onKeyReleased(entity, this, holder);
                }
                return;
            }
        }
    }

    @Nullable
    public AnimationTimer getAnimationTimer() {
        return this.animationTimer;
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return this.components;
    }

    @Nullable
    public <T> T set(DataComponentType<? super T> component, @Nullable T value) {
        var changed = this.components.set(component, value);
        if (!this.holder.getEntity().level().isClientSide) {
            var patch = DataComponentPatch.builder().set(component, value).build();
            // sync
        }
        return changed;
    }

    @Nullable
    public <T> T setSilently(DataComponentType<? super T> component, @Nullable T value) {
        return this.components.set(component, value);
    }

    @Nullable
    public <T, U> T update(DataComponentType<T> component, T defaultValue, U updateValue, BiFunction<T, U, T> updater) {
        return this.set(component, updater.apply(this.getOrDefault(component, defaultValue), updateValue));
    }

    @Nullable
    public <T> T update(DataComponentType<T> component, T defaultValue, UnaryOperator<T> updater) {
        T object = this.getOrDefault(component, defaultValue);
        return this.set(component, updater.apply(object));
    }

    @Nullable
    public <T, U> T updateSilently(DataComponentType<T> component, T defaultValue, U updateValue, BiFunction<T, U, T> updater) {
        return this.setSilently(component, updater.apply(this.getOrDefault(component, defaultValue), updateValue));
    }

    @Nullable
    public <T> T remove(DataComponentType<? extends T> component) {
        return this.components.remove(component);
    }

    public void applyComponents(DataComponentPatch components) {
        this.components.applyPatch(components);
    }

    public void applyComponents(DataComponentMap components) {
        this.components.setAll(components);
    }

    public Tag save() {
        return DataComponentMap.CODEC.encodeStart(this.holder.entity.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.components).getOrThrow();
    }

}
