package net.threetag.palladium.client.energybeam;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.particleemitter.ParticleEmitter;

public record EnergyBeamParticle(Holder<ParticleType<?>> particleType, CompoundTag options, ParticleEmitter emitter) {

    public static final Codec<EnergyBeamParticle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.PARTICLE_TYPE.holderByNameCodec().fieldOf("particle_type").forGetter(EnergyBeamParticle::particleType),
            CompoundTag.CODEC.optionalFieldOf("options", new CompoundTag()).forGetter(EnergyBeamParticle::options),
            ParticleEmitter.CODEC.optionalFieldOf("emitter", ParticleEmitter.DEFAULT).forGetter(EnergyBeamParticle::emitter)
    ).apply(instance, EnergyBeamParticle::new));

    public void spawn(Level level, Vec3 pos) {
        ParticleType<?> type = this.particleType.value();
        ParticleOptions options = type.codec().codec().parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.options).getOrThrow();
        this.emitter.spawnAtPosition(level, pos, options);
    }

}
