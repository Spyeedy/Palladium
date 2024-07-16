package net.threetag.palladium.compat.kubejs.neoforge;

import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RegisterGuiLayerEventJS implements KubeEvent {

    public static final Map<String, LayeredDraw.Layer> OVERLAYS = new HashMap<>();

    public RegisterGuiLayerEventJS() {
        OVERLAYS.clear();
    }

    public void register(String id, LayeredDraw.Layer overlay) {
        OVERLAYS.put(id, overlay);
    }

    public static class Layer implements LayeredDraw.Layer {

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
            for (LayeredDraw.Layer layer : OVERLAYS.values()) {
                layer.render(guiGraphics, deltaTracker);
            }
        }
    }

}
