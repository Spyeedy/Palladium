package net.threetag.palladium.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.entity.value.EntityDependentNumberType;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.icon.IconSerializer;

public class PalladiumRegistryKeys {

    public static final ResourceKey<Registry<Ability>> ABILITY = createRegistryKey("ability");
    public static final ResourceKey<Registry<ConditionSerializer<?>>> CONDITION_SERIALIZER = createRegistryKey("condition_serializer");
    public static final ResourceKey<Registry<Power>> POWER = createRegistryKey("power");
    public static final ResourceKey<Registry<IconSerializer<?>>> ICON_SERIALIZER = createRegistryKey("icon_serializer");
    public static final ResourceKey<Registry<EntityDependentNumberType<?>>> ENTITY_DEPENDENT_NUMBER_TYPE = createRegistryKey("entity_dependent_number_type");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(Palladium.id(name));
    }

}
