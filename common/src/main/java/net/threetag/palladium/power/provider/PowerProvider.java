package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.PowerCollector;
import net.threetag.palladiumcore.registry.PalladiumRegistry;

public abstract class PowerProvider {

    public abstract void providePowers(LivingEntity entity, EntityPowerHandler handler, PowerCollector collector);

}
