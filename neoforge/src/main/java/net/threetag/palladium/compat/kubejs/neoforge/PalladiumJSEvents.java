package net.threetag.palladium.compat.kubejs.neoforge;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface PalladiumJSEvents {

    EventGroup GROUP = EventGroup.of("PalladiumEvents");

    EventHandler CUSTOM_PROJECTILE_TICK = GROUP.server("customProjectileTick", () -> ProjectileTickEventJS.class);

    EventHandler REGISTER_ANIMATIONS = GROUP.client("registerAnimations", () -> RegisterAnimationsEventJS.class);
    EventHandler REGISTER_GUI_OVERLAYS = GROUP.client("registerGuiLayer", () -> RegisterGuiLayerEventJS.class);

}
