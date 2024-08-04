package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.*;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class AbilityUtil {

    /**
     * Returns all ability instances from the given entity
     *
     * @param entity Entity having abilities
     * @return List of all ability instances
     */
    @NotNull
    public static Collection<AbilityInstance> getInstances(LivingEntity entity) {
        List<AbilityInstance> instances = new ArrayList<>();
        PowerUtil.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values()).forEach(instances::addAll));
        return instances;
    }

    /**
     * Returns all ability instances of the given ability type from the entity
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability that is being looked for
     * @return List of all ability instances of the given ability type
     */
    @NotNull
    public static Collection<AbilityInstance> getInstances(LivingEntity entity, ResourceLocation abilityId) {
        if (!PalladiumRegistries.ABILITY.containsKey(abilityId)) {
            return Collections.emptyList();
        }

        return getInstances(entity, PalladiumRegistries.ABILITY.get(abilityId));
    }

    /**
     * Returns all ability instances of the given ability type from the entity
     *
     * @param entity  Entity having abilities
     * @param ability The ability that is being looked for
     * @return List of all ability instances of the given ability type
     */
    @NotNull
    public static Collection<AbilityInstance> getInstances(LivingEntity entity, Ability ability) {
        List<AbilityInstance> instances = new ArrayList<>();
        PowerUtil.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(instance -> instance.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(instances::addAll));
        return instances;
    }

    /**
     * Returns all enabled ability instances from the given entity
     *
     * @param entity Entity having abilities
     * @return List of all enabled ability instances
     */
    @NotNull
    public static Collection<AbilityInstance> getEnabledInstances(LivingEntity entity) {
        List<AbilityInstance> instances = new ArrayList<>();
        PowerUtil.getPowerHandler(entity).ifPresent(handler -> {
            for (PowerHolder holder : handler.getPowerHolders().values()) {
                Collection<AbilityInstance> values = holder.getAbilities().values();
                for (AbilityInstance value : values) {
                    if (value.isEnabled()) {
                        instances.add(value);
                    }
                }
            }
        });
        return instances;
    }

    /**
     * Returns all enabled render layer ability instances from the given entity
     *
     * @param entity Entity having abilities
     * @return List of all enabled render layer ability instances
     */
    @NotNull
    public static Collection<AbilityInstance> getEnabledRenderLayerInstances(LivingEntity entity) {
        List<AbilityInstance> instances = new ArrayList<>();
        PowerUtil.getPowerHandler(entity).ifPresent(handler -> {
            for (PowerHolder holder : handler.getPowerHolders().values()) {
                Collection<AbilityInstance> values = holder.getAbilities().values();
                for (AbilityInstance value : values) {
                    if (value.getConfiguration().getAbility() instanceof RenderLayerProviderAbility && value.isEnabled()) {
                        instances.add(value);
                    }
                }
            }
        });
        return instances;
    }

    /**
     * Returns all enabled ability instances of the given ability type from the entity
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability that is being looked for
     * @return List of all enabled ability instances of the given ability type
     */
    @NotNull
    public static Collection<AbilityInstance> getEnabledInstances(LivingEntity entity, ResourceLocation abilityId) {
        if (!PalladiumRegistries.ABILITY.containsKey(abilityId)) {
            return Collections.emptyList();
        }

        return getEnabledInstances(entity, PalladiumRegistries.ABILITY.get(abilityId));
    }

    /**
     * Returns all enabled ability instances of the given ability type from the entity
     *
     * @param entity  Entity having abilities
     * @param ability The ability that is being looked for
     * @return List of all enabled ability instances of the given ability type
     */
    @NotNull
    public static Collection<AbilityInstance> getEnabledInstances(LivingEntity entity, Ability ability) {
        List<AbilityInstance> instances = new ArrayList<>();
        PowerUtil.getPowerHandler(entity).ifPresent(handler -> handler.getPowerHolders().values().stream().map(holder -> holder.getAbilities().values().stream().filter(instance -> instance.isEnabled() && instance.getConfiguration().getAbility() == ability).collect(Collectors.toList())).forEach(instances::addAll));
        return instances;
    }

    /**
     * Returns a specific ability instance from a specific power
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return The specific {@link AbilityInstance}, or null
     */
    @Nullable
    public static AbilityInstance getInstance(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
        Power power = entity.registryAccess().registry(PalladiumRegistryKeys.POWER).orElseThrow().get(powerId);

        if (power == null) {
            return null;
        }

        EntityPowerHandler handler = PowerUtil.getPowerHandler(entity).orElse(null);

        if (handler == null) {
            return null;
        }

        PowerHolder holder = handler.getPowerHolder(power);

        if (holder == null) {
            return null;
        }

        return holder.getAbilities().get(abilityKey);
    }

    /**
     * Checks if a specific ability instance is unlocked
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return True if the ability is unlocked
     */
    public static boolean isUnlocked(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
        var instance = getInstance(entity, powerId, abilityKey);
        return instance != null && instance.isUnlocked();
    }

    /**
     * Checks if a specific ability entry is enabled
     *
     * @param entity     Entity having abilities
     * @param powerId    ID of the power containing the specific ability
     * @param abilityKey The unique key being used in the power json for the ability
     * @return True if the ability is enabled
     */
    public static boolean isEnabled(LivingEntity entity, ResourceLocation powerId, String abilityKey) {
        var instance = getInstance(entity, powerId, abilityKey);
        return instance != null && instance.isEnabled();
    }

    /**
     * Checks if a specific ability instance of a certain type is unlocked
     *
     * @param entity  Entity having abilities
     * @param ability Type of the ability that must be unlocked
     * @return True if any ability of the type is unlocked
     */
    public static boolean isTypeUnlocked(LivingEntity entity, Ability ability) {
        return getInstances(entity, ability).stream().anyMatch(AbilityInstance::isUnlocked);
    }

    /**
     * Checks if a specific ability instance of a certain type is unlocked
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability type that must be unlocked
     * @return True if any ability of the type is unlocked
     */
    public static boolean isTypeUnlocked(LivingEntity entity, ResourceLocation abilityId) {
        if (!PalladiumRegistries.ABILITY.containsKey(abilityId)) {
            return false;
        }
        return isTypeUnlocked(entity, PalladiumRegistries.ABILITY.get(abilityId));
    }

    /**
     * Checks if a specific ability instance of a certain type is enabled
     *
     * @param entity  Entity having abilities
     * @param ability Type of the ability that must be enabled
     * @return True if any ability of the type is enabled
     */
    public static boolean isTypeEnabled(LivingEntity entity, Ability ability) {
        return getInstances(entity, ability).stream().anyMatch(AbilityInstance::isEnabled);
    }

    /**
     * Checks if a specific ability instance of a certain type is enabled
     *
     * @param entity    Entity having abilities
     * @param abilityId ID of the ability type that must be enabled
     * @return True if any ability of the type is enabled
     */
    public static boolean isTypeEnabled(LivingEntity entity, ResourceLocation abilityId) {
        if (!PalladiumRegistries.ABILITY.containsKey(abilityId)) {
            return false;
        }
        return isTypeEnabled(entity, PalladiumRegistries.ABILITY.get(abilityId));
    }

    /**
     * Checks if the entity has the given power
     *
     * @param entity  Entity having abilities
     * @param powerId ID of the power that is being checked for
     * @return True if the entity has the power
     */
    public static boolean hasPower(LivingEntity entity, ResourceLocation powerId) {
        Power power = entity.registryAccess().registry(PalladiumRegistryKeys.POWER).orElseThrow().get(powerId);

        if (power == null) {
            return false;
        }

        EntityPowerHandler handler = PowerUtil.getPowerHandler(entity).orElse(null);

        if (handler == null) {
            return false;
        }

        return handler.getPowerHolder(power) != null;
    }

    public static List<AbilityInstance> findParentsWithinHolder(AbilityConfiguration ability, PowerHolder powerHolder) {
        List<AbilityInstance> list = new ArrayList<>();
        for (String key : ability.getConditions().getDependencies()) {
            AbilityInstance parent = powerHolder.getAbilities().get(key);

            if (parent != null) {
                list.add(parent);
            }
        }
        return list;
    }

    public static List<AbilityInstance> findChildrenWithinHolder(AbilityConfiguration ability, PowerHolder powerHolder) {
        List<AbilityInstance> list = new ArrayList<>();
        for (Map.Entry<String, AbilityInstance> entries : powerHolder.getAbilities().entrySet()) {
            for (String key : ability.getConditions().getDependencies()) {
                if (key.equals(entries.getKey())) {
                    list.add(entries.getValue());
                }
            }
        }
        return list;
    }

}
