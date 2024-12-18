package net.threetag.palladium.fabric;

import net.fabricmc.api.ModInitializer;

import net.threetag.palladium.Palladium;

public final class PalladiumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Palladium.init();
    }

}
