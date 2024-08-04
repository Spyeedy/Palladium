package net.threetag.palladium.entity;

import net.threetag.palladium.client.renderer.renderlayer.RenderLayerStates;
import net.threetag.palladium.power.EntityPowerHandler;

public interface PalladiumLivingEntityExtension {

    EntityPowerHandler palladium$getPowerHandler();

    RenderLayerStates palladium$getRenderLayerStates();

}
