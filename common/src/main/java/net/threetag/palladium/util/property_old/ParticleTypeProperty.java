package net.threetag.palladium.util.property_old;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ParticleTypeProperty extends RegistryObjectProperty<ParticleType<?>> {

    public ParticleTypeProperty(String key) {
        super(key, BuiltInRegistries.PARTICLE_TYPE);
    }
}
