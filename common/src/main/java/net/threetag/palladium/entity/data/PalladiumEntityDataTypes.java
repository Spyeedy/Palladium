package net.threetag.palladium.entity.data;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.superpower.EntitySuperpowerHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class PalladiumEntityDataTypes {

    public static final DeferredRegister<PalladiumEntityDataType<?>> DATA_TYPES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ENTITY_DATA_TYPE);

    public static final RegistryHolder<PalladiumLivingEntityDataType<EntityPowerHandler>> POWER_HANDLER =
            DATA_TYPES.register("power_handler", () -> EntityPowerHandler::new);

    public static final RegistryHolder<PalladiumLivingEntityDataType<EntitySuperpowerHandler>> SUPERPOWER_HANDLER =
            DATA_TYPES.register("superpower_handler", () -> EntitySuperpowerHandler::new);

}
