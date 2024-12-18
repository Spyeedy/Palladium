package net.threetag.palladium.entity.number;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class EntityDependentNumberTypes {

    public static final DeferredRegister<EntityDependentNumberType<?>> TYPES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ENTITY_DEPENDENT_NUMBER_TYPE);

    public static final RegistryHolder<StaticNumber.Type> STATIC = TYPES.register("static", StaticNumber.Type::new);
    public static final RegistryHolder<ScoreNumber.Type> SCORE = TYPES.register("score", ScoreNumber.Type::new);

}
