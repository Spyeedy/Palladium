package net.threetag.palladium.power.ability.unlocking;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class UnlockingHandlerSerializers {

    public static final DeferredRegister<UnlockingHandlerSerializer<?>> UNLOCKING_HANDLERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ABILITY_UNLOCKING_HANDLER_SERIALIZER);

    public static final RegistryHolder<ConditionalUnlockingHandler.Serializer> CONDITIONAL = UNLOCKING_HANDLERS.register("conditional", ConditionalUnlockingHandler.Serializer::new);
    public static final RegistryHolder<ExperienceLevelBuyableUnlockingHandler.Serializer> XP_BUYABLE = UNLOCKING_HANDLERS.register("experience_level_buyable", ExperienceLevelBuyableUnlockingHandler.Serializer::new);
    public static final RegistryHolder<ItemBuyableUnlockingHandler.Serializer> ITEM_BUYABLE = UNLOCKING_HANDLERS.register("item_buyable", ItemBuyableUnlockingHandler.Serializer::new);
    public static final RegistryHolder<ScoreBuyableUnlockingHandler.Serializer> SCORE_BUYABLE = UNLOCKING_HANDLERS.register("score_buyable", ScoreBuyableUnlockingHandler.Serializer::new);

}
