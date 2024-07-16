package net.threetag.palladium.registry;

import net.minecraft.core.Registry;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.entity.value.EntityDependentNumberType;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.icon.IconSerializer;
import net.threetag.palladiumcore.registry.DataPackRegistries;
import net.threetag.palladiumcore.registry.RegistryBuilder;

public class PalladiumRegistries {

    public static final Registry<Ability> ABILITY = RegistryBuilder.create(PalladiumRegistryKeys.ABILITY).build();
    public static final Registry<ConditionSerializer<?>> CONDITION_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.CONDITION_SERIALIZER).build();
    public static final Registry<IconSerializer<?>> ICON_SERIALIZER = RegistryBuilder.create(PalladiumRegistryKeys.ICON_SERIALIZER).build();
    public static final Registry<EntityDependentNumberType<?>> ENTITY_DEPENDENT_NUMBER_TYPE = RegistryBuilder.create(PalladiumRegistryKeys.ENTITY_DEPENDENT_NUMBER_TYPE).build();

    public static void init() {
        DataPackRegistries.create(PalladiumRegistryKeys.POWER, Power.CODEC, Power.CODEC);
    }

}
