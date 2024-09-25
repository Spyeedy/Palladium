package net.threetag.palladium.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistryHolder;

public class PalladiumSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Palladium.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistryHolder<SoundEvent, SoundEvent> HEAT_VISION = make("entity.ability.heat_vision");

    public static RegistryHolder<SoundEvent, SoundEvent> make(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Palladium.id(name)));
    }

}
