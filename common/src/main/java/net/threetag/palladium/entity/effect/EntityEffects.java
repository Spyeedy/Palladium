package net.threetag.palladium.entity.effect;

import net.minecraft.core.Holder;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladiumcore.registry.DeferredRegister;

public class EntityEffects {

    public static final DeferredRegister<EntityEffect> EFFECTS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistries.ENTITY_EFFECT);

    public static final Holder<EntityEffect> ENERGY_BEAM = EFFECTS.register("energy_beam", EnergyBeamEffect::new);

    public static void init() {
        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            if (handler.getEntity() instanceof EffectEntity) {
                for (EntityEffect entityEffect : PalladiumRegistries.ENTITY_EFFECT) {
                    entityEffect.registerProperties(handler);
                }
            }
        });
    }

}
