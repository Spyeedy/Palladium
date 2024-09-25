package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SuperpowerUtil {

    /**
     * Returns the superpowers the entity currently has
     *
     * @param entity {@link LivingEntity} which has superpowers
     * @return {@link Collection} of superpowers of the entity
     */
    public static Collection<Power> getSuperpowers(LivingEntity entity) {
        List<Power> powers = new ArrayList<>();
//        for (ResourceLocation id : PalladiumProperties.SUPERPOWER_IDS.get(entity)) {
//            var power = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).get(id);
//
//            if (power != null) {
//                powers.add(power);
//            }
//        }
        return powers;
    }

    /**
     * Returns the IDs of the superpowers the entity currently has
     *
     * @param entity {@link LivingEntity} which has superpowers
     * @return {@link Collection} of {@link ResourceLocation}s for the superpowers of the entity
     */
    public static Collection<ResourceLocation> getSuperpowerIds(LivingEntity entity) {
        // TODO
        return Collections.emptyList();
    }

    /**
     * Sets the entity's superpowers to just the given one
     *
     * @param entity  {@link LivingEntity} receiving the {@link Power}
     * @param powerId ID of the {@link Power} being given to the {@link LivingEntity}
     */
    public static void setSuperpower(LivingEntity entity, ResourceLocation powerId) {
//        PalladiumProperties.SUPERPOWER_IDS.set(entity, Collections.singletonList(powerId));
    }

    /**
     * Sets the entity's superpowers to just the given one
     *
     * @param entity {@link LivingEntity} receiving the {@link Power}
     * @param power  The {@link Power} being given to the {@link LivingEntity}
     */
    public static void setSuperpower(LivingEntity entity, Power power) {
        setSuperpower(entity, entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(power));
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity   {@link LivingEntity} receiving the {@link Power}s
     * @param powerIds {@link List} of IDs of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpowerIds(LivingEntity entity, List<ResourceLocation> powerIds) {
//        PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity {@link LivingEntity} receiving the {@link Power}s
     * @param powers {@link List} of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpowers(LivingEntity entity, List<Power> powers) {
        var registry = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);
        setSuperpowerIds(entity, powers.stream().map(registry::getKey).collect(Collectors.toList()));
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity   {@link LivingEntity} receiving the {@link Power}s
     * @param powerIds Array of IDs of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpowerIds(LivingEntity entity, ResourceLocation... powerIds) {
//        PalladiumProperties.SUPERPOWER_IDS.set(entity, Arrays.asList(powerIds));
    }

    /**
     * Sets the entity's superpowers to the given ones
     *
     * @param entity {@link LivingEntity} receiving the {@link Power}s
     * @param powers Array of the {@link Power}s being given to the {@link LivingEntity}
     */
    public static void setSuperpower(LivingEntity entity, Power... powers) {
        var registry = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);
        setSuperpowerIds(entity, Arrays.stream(powers).map(registry::getKey).collect(Collectors.toList()));
    }

    /**
     * Tests if the entity has the given superpower
     *
     * @param entity  The {@link LivingEntity} being tested for the superpower
     * @param powerId ID of the {@link Power} that is being checked for
     * @return true if the entity has the superpower
     */
    public static boolean hasSuperpower(LivingEntity entity, ResourceLocation powerId) {
        return false;
//        return PalladiumProperties.SUPERPOWER_IDS.get(entity).contains(powerId);
    }

    /**
     * Tests if the entity has the given superpower
     *
     * @param entity The {@link LivingEntity} being tested for the superpower
     * @param power  {@link Power} that is being checked for
     * @return true if the entity has the superpower
     */
    public static boolean hasSuperpower(LivingEntity entity, Power power) {
        var registry = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);
        return hasSuperpower(entity, registry.getKey(power));
    }

    /**
     * Adds a superpower to the entity
     *
     * @param entity  The {@link LivingEntity} receiving the superpower
     * @param powerId ID of the {@link Power} being given to the {@link LivingEntity}
     * @return true if the {@link Power} exists and wasn't already given to the {@link LivingEntity}
     */
    public static boolean addSuperpower(LivingEntity entity, ResourceLocation powerId) {
        var registry = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);

        if (!registry.containsKey(powerId) || hasSuperpower(entity, powerId)) {
            return false;
        }

//        List<ResourceLocation> powerIds = new ArrayList<>(PalladiumProperties.SUPERPOWER_IDS.get(entity));
//        powerIds.add(powerId);
//        PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
        return true;
    }

    /**
     * Adds a superpower to the entity
     *
     * @param entity The {@link LivingEntity} receiving the superpower
     * @param power  {@link Power} being given to the {@link LivingEntity}
     * @return true if the {@link Power} exists and wasn't already given to the {@link LivingEntity}
     */
    public static boolean addSuperpower(LivingEntity entity, Power power) {
        var registry = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);
        return addSuperpower(entity, registry.getKey(power));
    }

    /**
     * Removes a superpower to the entity
     *
     * @param entity  The {@link LivingEntity} having the superpower removed
     * @param powerId ID of the {@link Power} being removed from the {@link LivingEntity}
     * @return true if the {@link Power} exists and was already given to the {@link LivingEntity}
     */
    public static boolean removeSuperpower(LivingEntity entity, ResourceLocation powerId) {
        var registry = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);

        if (!registry.containsKey(powerId) || !hasSuperpower(entity, powerId)) {
            return false;
        }

//        List<ResourceLocation> powerIds = new ArrayList<>(PalladiumProperties.SUPERPOWER_IDS.get(entity));
//        powerIds.remove(powerId);
//        PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
        return true;
    }

    /**
     * Removes a superpower to the entity
     *
     * @param entity The {@link LivingEntity} having the superpower removed
     * @param power  {@link Power} being removed from the {@link LivingEntity}
     * @return true if the {@link Power} exists and was already given to the {@link LivingEntity}
     */
    public static boolean removeSuperpower(LivingEntity entity, Power power) {
        var registry = entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);
        return removeSuperpower(entity, registry.getKey(power));
    }

    /**
     * Removes all superpowers matching the predicate from the entity
     *
     * @param entity    The {@link LivingEntity} having the superpowers removed
     * @param predicate {@link Predicate} to test which powers are set to be removed
     * @return Amount of powers which have been removed
     */
    public static int removeSuperpowers(LivingEntity entity, Predicate<Power> predicate) {
        List<Power> toRemove = new ArrayList<>();

        for (Power superpower : getSuperpowers(entity)) {
            if (predicate.test(superpower)) {
                toRemove.add(superpower);
            }
        }

        int i = 0;
        for (Power power : toRemove) {
            if (removeSuperpower(entity, power)) {
                i++;
            }
        }

        return i;
    }

    /**
     * Removes all superpowers matching the predicate from the entity, using the IDs
     *
     * @param entity    The {@link LivingEntity} having the superpowers removed
     * @param predicate {@link Predicate} to test which powers are set to be removed
     * @return Amount of powers which have been removed
     */
    public static int removeSuperpowersByIds(LivingEntity entity, Predicate<ResourceLocation> predicate) {
        List<ResourceLocation> toRemove = new ArrayList<>();

        for (ResourceLocation superpower : getSuperpowerIds(entity)) {
            if (predicate.test(superpower)) {
                toRemove.add(superpower);
            }
        }

        int i = 0;
        for (ResourceLocation power : toRemove) {
            if (removeSuperpower(entity, power)) {
                i++;
            }
        }

        return i;
    }

    /**
     * Removes all superpowers from an entity
     *
     * @param entity {@link LivingEntity} having all superpowers removed
     */
    public static void removeAllSuperpowers(LivingEntity entity) {
//        PalladiumProperties.SUPERPOWER_IDS.set(entity, Collections.emptyList());
    }

}
