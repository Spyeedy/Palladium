package net.threetag.palladium.power.ability.enabling;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class EnablingHandlerSerializers {

    public static final DeferredRegister<EnablingHandlerSerializer<?>> ENABLING_HANDLERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ABILITY_ENABLING_HANDLER_SERIALIZER);

    public static final RegistryHolder<ConditionalEnablingHandler.Serializer> CONDITIONAL = ENABLING_HANDLERS.register("conditional", ConditionalEnablingHandler.Serializer::new);
    public static final RegistryHolder<KeyBindEnablingHandler.Serializer> KEY_BIND = ENABLING_HANDLERS.register("key_bind", KeyBindEnablingHandler.Serializer::new);

}
