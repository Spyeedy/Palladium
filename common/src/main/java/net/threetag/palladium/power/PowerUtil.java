package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PowerUtil {

    /**
     * Returns the power handler for the given entity
     */
    public static Optional<PowerHandler> getPowerHandler(LivingEntity entity) {
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
            powers.addAll(powerHandler.getPowerHolders().values().stream().map(IPowerHolder::getPower).toList());
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
            powers.addAll(powerHandler.getPowerHolders().values().stream().map(h -> h.getPower().getId()).toList());
        });
        return powers;
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
            powers.addAll(powerHandler.getPowerHolders().values().stream().map(IPowerHolder::getPower).filter(p -> p.getId().getNamespace().equals(namespace)).toList());
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
            powers.addAll(powerHandler.getPowerHolders().values().stream().map(h -> h.getPower().getId()).filter(id -> id.getNamespace().equals(namespace)).toList());
        });
        return powers;
    }

}
