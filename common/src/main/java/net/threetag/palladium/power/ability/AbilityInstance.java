package net.threetag.palladium.power.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.CooldownType;
import net.threetag.palladium.network.SyncAbilityInstancePropertyPacket;
import net.threetag.palladium.network.SyncAbilityStatePacket;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyValue;
import net.threetag.palladium.util.property.PropertyManager;
import net.threetag.palladium.util.property.SyncOption;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.Nullable;

public class AbilityInstance {

    private final AbilityConfiguration abilityConfiguration;
    private final PowerHolder holder;
    private boolean unlocked = false;
    private boolean enabled = false;
    public boolean keyPressed = false;
    public int maxCooldown = 0, cooldown = 0;
    public int maxActivationTimer = 0, activationTimer = 0;
    private int lifetime = 0;
    private int prevEnabledTicks = 0;
    private int enabledTicks = 0;
    public AbilityReference reference;
    private final PropertyManager propertyManager = new PropertyManager().setListener(new PropertyManager.Listener() {
        @Override
        public <T> void onChanged(PalladiumProperty<T> property, PalladiumPropertyValue<T> valueHolder, T oldValue, T newValue) {
            syncProperty(property, holder.getEntity(), null);
        }
    });

    public AbilityInstance(AbilityConfiguration abilityConfiguration, PowerHolder holder, AbilityReference reference) {
        this.abilityConfiguration = abilityConfiguration;
        this.holder = holder;
        this.abilityConfiguration.getAbility().registerUniqueProperties(this.propertyManager);
        this.abilityConfiguration.getConditions().getUnlockingConditions().forEach(condition -> condition.registerAbilityProperties(this, this.propertyManager));
        this.abilityConfiguration.getConditions().getEnablingConditions().forEach(condition -> condition.registerAbilityProperties(this, this.propertyManager));
    }

    public AbilityConfiguration getConfiguration() {
        return this.abilityConfiguration;
    }

    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    public PowerHolder getHolder() {
        return this.holder;
    }

    public AbilityReference getReference() {
        return this.reference;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryLocalVariable"})
    public void syncProperty(PalladiumProperty<?> property, LivingEntity entity, @Nullable SyncOption syncOption) {
        if (!entity.level().isClientSide && this.propertyManager.isRegistered(property)) {
            if (syncOption == null) {
                syncOption = property.getSyncType();
            }

            PalladiumProperty property1 = property;
            if (syncOption == SyncOption.EVERYONE) {
                NetworkManager.get().sendToPlayersInDimension((ServerLevel) entity.level(), new SyncAbilityInstancePropertyPacket(entity.getId(), this.reference, this.propertyManager.getHolder(property1)));
            } else if (syncOption == SyncOption.SELF && entity instanceof ServerPlayer serverPlayer) {
                NetworkManager.get().sendToPlayer(serverPlayer, new SyncAbilityInstancePropertyPacket(entity.getId(), this.reference, this.propertyManager.getHolder(property1)));
            }
        }
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isOnCooldown() {
        return this.getConfiguration().getConditions().getCooldownType() == CooldownType.STATIC ? this.cooldown > 0 : this.cooldown < this.maxCooldown;
    }

    public int getEnabledTicks() {
        return this.enabledTicks;
    }

    public int getPrevEnabledTicks() {
        return this.prevEnabledTicks;
    }

    public void setClientState(LivingEntity entity, PowerHolder powerHolder, boolean unlocked, boolean enabled, int maxCooldown, int cooldown, int maxActivationTimer, int activationTimer) {
        this.unlocked = unlocked;
        this.maxCooldown = maxCooldown;
        this.cooldown = cooldown;
        this.maxActivationTimer = maxActivationTimer;
        this.activationTimer = activationTimer;

        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (this.enabled) {
                this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
            } else {
                this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, !this.isEnabled());
            }
        }
    }

    public void tick(LivingEntity entity, PowerHolder powerHolder) {
        this.prevEnabledTicks = this.enabledTicks;

        if (!entity.level().isClientSide) {
            if (this.lifetime == 0) {
                this.abilityConfiguration.getConditions().getUnlockingConditions().forEach(condition -> condition.init(entity, this, this.propertyManager));
                this.abilityConfiguration.getConditions().getEnablingConditions().forEach(condition -> condition.init(entity, this, this.propertyManager));
            }

            boolean unlocked = true;
            boolean sync = false;

            for (Condition unlockingCondition : this.abilityConfiguration.getConditions().getUnlockingConditions()) {
                if (!unlockingCondition.active(DataContext.forAbility(entity, this))) {
                    unlocked = false;
                    break;
                }
            }

            if (entity.isSpectator()) {
                unlocked = false;
            }

            if (this.unlocked != unlocked) {
                this.unlocked = unlocked;
                sync = true;
            }

            boolean enabled = this.unlocked;

            if (this.unlocked) {
                for (Condition enablingCondition : this.abilityConfiguration.getConditions().getEnablingConditions()) {
                    if (!enablingCondition.active(DataContext.forAbility(entity, this))) {
                        enabled = false;
                        break;
                    }
                }
            }

            if (entity.isSpectator()) {
                enabled = false;
            }

            if (this.enabled != enabled) {
                if (!this.enabled) {
                    this.enabled = true;
                    sync = true;
                    this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
                } else {
                    this.keyPressed = false;
                    this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, this.isEnabled());
                    this.enabled = false;
                    sync = true;

                }
            }

            if (sync || lifetime == 0) {
                this.syncState(entity);
            }
        }

        if (this.isEnabled()) {
            this.enabledTicks++;

            for (EnergyBarUsage usage : this.getConfiguration().getEnergyBarUsages()) {
                usage.consume(this.holder);
            }
        } else if (this.enabledTicks > 0) {
            this.enabledTicks--;
        }

        if (this.abilityConfiguration.getConditions().getCooldownType() == CooldownType.STATIC) {
            if (this.cooldown > 0) {
                this.cooldown--;
            }
        } else if (this.abilityConfiguration.getConditions().getCooldownType() == CooldownType.DYNAMIC) {
            if (this.isEnabled() && this.cooldown > 0) {
                this.cooldown--;
            } else if (!this.isEnabled() && this.cooldown < this.maxCooldown) {
                this.cooldown++;
            }
        }

        if (this.activationTimer > 0) {
            this.activationTimer--;
        }

        this.lifetime++;
        this.abilityConfiguration.getAbility().tick(entity, this, powerHolder, this.isEnabled());
    }

    public void syncState(LivingEntity entity) {
        getSyncStateMessage(entity).sendToDimension(entity.level());
    }

    public SyncAbilityStatePacket getSyncStateMessage(LivingEntity entity) {
        return new SyncAbilityStatePacket(entity.getId(), this.getReference(), this.unlocked, this.enabled, this.maxCooldown, this.cooldown, this.maxActivationTimer, this.activationTimer);
    }

    public void startCooldown(LivingEntity entity, int cooldown) {
        this.maxCooldown = this.cooldown = cooldown;
        this.syncState(entity);
    }

    public void startActivationTimer(LivingEntity entity, int activationTimer) {
        this.maxActivationTimer = this.activationTimer = activationTimer;
        this.syncState(entity);
    }

    public void keyPressed(LivingEntity entity, boolean pressed) {
        for (Condition condition : this.getConfiguration().getConditions().getUnlockingConditions()) {
            if (condition.needsKey()) {
                if (pressed) {
                    condition.onKeyPressed(entity, this, holder.getPower(), holder);
                } else {
                    condition.onKeyReleased(entity, this, holder.getPower(), holder);
                }
                return;
            }
        }

        for (Condition condition : this.getConfiguration().getConditions().getEnablingConditions()) {
            if (condition.needsKey()) {
                if (pressed) {
                    condition.onKeyPressed(entity, this, holder.getPower(), holder);
                } else {
                    condition.onKeyReleased(entity, this, holder.getPower(), holder);
                }
                return;
            }
        }
    }

    public PalladiumProperty<?> getEitherPropertyByKey(String key) {
        PalladiumProperty<?> property = this.propertyManager.getPropertyByName(key);
        return property != null ? property : this.abilityConfiguration.getPropertyManager().getPropertyByName(key);
    }

    public <T> T getProperty(PalladiumProperty<T> property) {
        return this.propertyManager.isRegistered(property) ? this.propertyManager.get(property) : this.abilityConfiguration.get(property);
    }

    public Object getPropertyByName(String key) {
        var property = getEitherPropertyByKey(key);
        return property != null ? this.getProperty(property) : null;
    }

    public <T> AbilityInstance setUniqueProperty(PalladiumProperty<T> property, T value) {
        this.propertyManager.set(property, value);
        return this;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean setUniquePropertyByName(String key, Object value) {
        PalladiumProperty property = this.getPropertyManager().getPropertyByName(key);

        if (property != null) {
            this.setUniqueProperty(property, value);
            return true;
        }

        return false;
    }

    public void fromNBT(CompoundTag tag) {
        this.propertyManager.fromNBT(tag);
    }

    public CompoundTag toNBT(boolean toDisk) {
        return this.propertyManager.toNBT(toDisk);
    }

}
