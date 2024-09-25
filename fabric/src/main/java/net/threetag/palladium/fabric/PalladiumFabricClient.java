package net.threetag.palladium.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.addonpack.AddonPackManager;

public class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AddonPackManager.startLoading();
        AddonPackManager.waitForLoading();
        PalladiumClient.init();
    }

}
