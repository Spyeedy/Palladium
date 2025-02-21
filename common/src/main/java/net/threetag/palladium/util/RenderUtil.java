package net.threetag.palladium.util;

import dev.architectury.platform.Platform;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.compat.iris.IrisCompat;

public class RenderUtil {

    private static Entity CURRENTLY_RENDERED_ENTITY = null;
    private static float CURRENTLY_RENDERED_PARTIAL_TICK = 0F;

    public static void setCurrentlyRenderedEntity(Entity currentlyRenderedEntity) {
        CURRENTLY_RENDERED_ENTITY = currentlyRenderedEntity;
    }

    public static Entity getCurrentlyRenderedEntity() {
        return CURRENTLY_RENDERED_ENTITY;
    }

    public static void setCurrentlyRenderedPartialTick(float currentlyRenderedPartialTick) {
        CURRENTLY_RENDERED_PARTIAL_TICK = currentlyRenderedPartialTick;
    }

    public static float getCurrentlyRenderedPartialTick() {
        return CURRENTLY_RENDERED_PARTIAL_TICK;
    }

    public static boolean isIrisShaderActive() {
        return Platform.isModLoaded("iris") && IrisCompat.isShaderPackActive();
    }
}
