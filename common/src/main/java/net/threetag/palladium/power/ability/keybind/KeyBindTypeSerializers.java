package net.threetag.palladium.power.ability.keybind;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class KeyBindTypeSerializers {

    public static final DeferredRegister<KeyBindTypeSerializer<?>> KEY_BIND_TYPES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.KEY_BIND_TYPE_SERIALIZER);

    public static final RegistryHolder<AbilityKeyBind.Serializer> ABILITY_KEY = KEY_BIND_TYPES.register("ability_key", AbilityKeyBind.Serializer::new);
    public static final RegistryHolder<JumpKeyBind.Serializer> JUMP = KEY_BIND_TYPES.register("jump", JumpKeyBind.Serializer::new);

}
