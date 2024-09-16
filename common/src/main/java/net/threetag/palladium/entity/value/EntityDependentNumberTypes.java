package net.threetag.palladium.entity.value;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistryHolder;

public class EntityDependentNumberTypes {

    public static final DeferredRegister<EntityDependentNumberType<?>> TYPES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ENTITY_DEPENDENT_NUMBER_TYPE);

    public static final RegistryHolder<EntityDependentNumberType<?>, StaticNumber.Type> STATIC = TYPES.register("static", StaticNumber.Type::new);
    public static final RegistryHolder<EntityDependentNumberType<?>, ScoreNumber.Type> SCORE = TYPES.register("score", ScoreNumber.Type::new);

}
