package net.threetag.palladium.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.client.renderer.WatcherRenderer;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerSerializers;
import net.threetag.palladium.core.registry.GuiLayerRegistry;

import java.util.Collections;

public class PalladiumClient {

    public static void init() {
        PalladiumKeyMappings.init();

        // Overlay Renderer
        GuiLayerRegistry.register(Palladium.id("ability_bar"), AbilityBar.INSTANCE);
        ClientTickEvent.CLIENT_POST.register(AbilityBar.INSTANCE);

        // Screens
        PowersScreen.register();

        // Render Layers
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, PackRenderLayerManager.INSTANCE, Palladium.id("render_layers"), Collections.singletonList(ModelLayerManager.ID));
        PackRenderLayerSerializers.init();

        // Misc
        WatcherRenderer.init();
    }

}
