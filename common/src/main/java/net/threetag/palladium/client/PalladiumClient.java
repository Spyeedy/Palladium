package net.threetag.palladium.client;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.core.registry.GuiLayerRegistry;

public class PalladiumClient {

    public static void init() {
        PalladiumKeyMappings.init();

        // Overlay Renderer
        GuiLayerRegistry.register(Palladium.id("ability_bar"), new AbilityBarRenderer());
    }

}
