package net.threetag.palladium.power.ability.unlocking;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class UnlockingHandlerSerializers {

    public static final DeferredRegister<UnlockingHandlerSerializer<?>> UNLOCKING_HANDLERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ABILITY_UNLOCKING_HANDLER_SERIALIZER);

    public static final RegistryHolder<ConditionalUnlockingHandler.Serializer> CONDITIONAL = UNLOCKING_HANDLERS.register("conditional", ConditionalUnlockingHandler.Serializer::new);

}
