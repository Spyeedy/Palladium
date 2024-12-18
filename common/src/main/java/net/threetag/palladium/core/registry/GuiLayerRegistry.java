package net.threetag.palladium.core.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

public class GuiLayerRegistry {

    @ExpectPlatform
    public static void register(ResourceLocation id, LayeredDraw.Layer layer) {
        throw new AssertionError();
    }

}
