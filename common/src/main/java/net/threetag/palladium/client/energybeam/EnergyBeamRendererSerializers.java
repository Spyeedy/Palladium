package net.threetag.palladium.client.energybeam;

import net.threetag.palladium.Palladium;

public class EnergyBeamRendererSerializers {

    public static final EnergyBeamRendererSerializer<?> LASER = register("laser", new LaserBeamRenderer.Serializer());
    public static final EnergyBeamRendererSerializer<?> LIGHTNING = register("lightning", new LightningBeamRenderer.Serializer());

    private static <T extends EnergyBeamRenderer> EnergyBeamRendererSerializer<T> register(String id, EnergyBeamRendererSerializer<T> serializer) {
        return EnergyBeamRendererSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
