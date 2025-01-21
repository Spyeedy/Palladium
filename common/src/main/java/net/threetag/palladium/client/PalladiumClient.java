package net.threetag.palladium.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.TickEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.abilitybar.AbilityBar;
import net.threetag.palladium.core.registry.GuiLayerRegistry;

public class PalladiumClient {

    public static void init() {
        PalladiumKeyMappings.init();

        // Overlay Renderer
        GuiLayerRegistry.register(Palladium.id("ability_bar"), AbilityBar.INSTANCE);
        ClientTickEvent.CLIENT_POST.register(AbilityBar.INSTANCE);
    }

}
