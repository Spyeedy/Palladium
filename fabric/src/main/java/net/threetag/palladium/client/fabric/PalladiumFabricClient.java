package net.threetag.palladium.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.threetag.palladium.client.PalladiumClient;

public final class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PalladiumClient.init();
    }

}
