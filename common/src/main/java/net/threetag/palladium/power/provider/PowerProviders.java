package net.threetag.palladium.power.provider;

import net.minecraft.core.Holder;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.registry.DeferredRegister;

public class PowerProviders {

    public static final DeferredRegister<PowerProvider> PROVIDERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.POWER_PROVIDER);

    public static final Holder<PowerProvider> SUPERPOWER = PROVIDERS.register("superpower", SuperpowerProvider::new);
    public static final Holder<PowerProvider> EQUIPMENT_SLOTS = PROVIDERS.register("equipment_slots", EquipmentSlotPowerProvider::new);
    public static final Holder<PowerProvider> SUIT_SETS = PROVIDERS.register("suit_sets", SuitSetPowerProvider::new);

}
