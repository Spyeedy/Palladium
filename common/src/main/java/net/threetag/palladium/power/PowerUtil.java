package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PowerUtil {

    /**
     * Returns the power handler for the given entity
     */
    public static Optional<EntityPowerHandler> getPowerHandler(LivingEntity entity) {
        if (entity instanceof PalladiumLivingEntityExtension ext) {
            return Optional.of(ext.palladium$getPowerHandler());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the powers the entity currently has
     *
     * @param entity {@link LivingEntity} which has powers
     * @return {@link Collection} of powers of the entity
     */
    public static Collection<Power> getPowers(LivingEntity entity) {
        List<Power> powers = new ArrayList<>();
        getPowerHandler(entity).ifPresent(powerHandler -> {
            powers.addAll(powerHandler.getPowerHolders().values().stream().map(powerHolder -> powerHolder.getPower().value()).toList());
        });
        return powers;
    }

    /**
     * Returns the IDs of the powers the entity currently has
     *
     * @param entity {@link LivingEntity} which has superpowers
     * @return {@link Collection} of IDs of powers of the entity
     */
    public static Collection<ResourceLocation> getPowerIds(LivingEntity entity) {
        List<ResourceLocation> powers = new ArrayList<>();
        getPowerHandler(entity).ifPresent(powerHandler -> {
            powers.addAll(powerHandler.getPowerHolders().values().stream()
                    .map(h -> entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(h.getPower().value()))
                    .toList());
        });
        return powers;
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

        return handler.getPowerHolder(powerId) != null;
    }

    /**
     * Returns the powers the entity currently has, filtered by the namespace
     *
     * @param entity {@link LivingEntity} which has powers
     * @return {@link Collection} of powers of the entity
     */
    public static Collection<Power> getPowersForNamespace(LivingEntity entity, String namespace) {
        List<Power> powers = new ArrayList<>();
        getPowerHandler(entity).ifPresent(powerHandler -> {
            powers.addAll(powerHandler.getPowerHolders().values().stream()
                    .map(powerHolder -> powerHolder.getPower().value())
                    .filter(p -> entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(p).getNamespace().equals(namespace))
                    .toList());
        });
        return powers;
    }

    /**
     * Returns the IDs of the powers the entity currently has, filtered by the namespace
     *
     * @param entity {@link LivingEntity} which has powers
     * @return {@link Collection} of IDs of powers of the entity
     */
    public static Collection<ResourceLocation> getPowerIdsForNamespace(LivingEntity entity, String namespace) {
        List<ResourceLocation> powers = new ArrayList<>();
        getPowerHandler(entity).ifPresent(powerHandler -> {
            powers.addAll(powerHandler.getPowerHolders().values().stream()
                    .map(h -> entity.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER).getKey(h.getPower().value()))
                    .filter(id -> id.getNamespace().equals(namespace))
                    .toList());
        });
        return powers;
    }

}
