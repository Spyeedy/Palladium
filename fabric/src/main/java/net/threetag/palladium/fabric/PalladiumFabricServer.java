package net.threetag.palladium.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.core.registry.SimpleRegister;

public class PalladiumFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        AddonPackManager.initiateBasicLoaders();
        AddonPackManager.initiateAllLoaders(SimpleRegister::register);
    }
}
