package net.threetag.palladium.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.client.PalladiumClient;
import net.threetag.palladium.core.registry.SimpleRegister;

public final class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AddonPackManager.initiateBasicLoaders();
        AddonPackManager.initiateAllLoaders(SimpleRegister::register);
        PalladiumClient.init();
    }

}
