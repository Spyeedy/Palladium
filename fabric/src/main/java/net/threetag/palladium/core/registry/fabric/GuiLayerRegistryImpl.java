package net.threetag.palladium.core.registry.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class GuiLayerRegistryImpl {

    public static void register(ResourceLocation id, LayeredDraw.Layer layer) {
        HudLayerRegistrationCallback.EVENT.register(layers -> layers.addLayer(new Wrapper(id, layer)));
    }

    private record Wrapper(ResourceLocation id, LayeredDraw.Layer layer) implements IdentifiedLayer {

        @Override
        public ResourceLocation id() {
            return this.id;
        }

        @Override
        public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
            this.layer.render(guiGraphics, deltaTracker);
        }
    }

}
