package net.threetag.palladium.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.threetag.palladium.client.PalladiumClient;

public final class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PalladiumClient.init();
    }

}
