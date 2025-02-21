package net.threetag.palladium.client.particleemitter;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ParticleEmitterManager extends SimpleJsonResourceReloadListener<ParticleEmitterConfiguration> {

    public static ParticleEmitterManager INSTANCE = new ParticleEmitterManager();

    public Map<ResourceLocation, ParticleEmitterConfiguration> byName = ImmutableMap.of();

    public ParticleEmitterManager() {
        super(ParticleEmitterConfiguration.CODEC, FileToIdConverter.json("palladium/particle_emitters"));
    }

    @Override
    protected void apply(Map<ResourceLocation, ParticleEmitterConfiguration> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ParticleEmitterConfiguration> builder = ImmutableMap.builder();
        objects.forEach(builder::put);
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} particle emitters", this.byName.size());
    }

    @Nullable
    public ParticleEmitterConfiguration get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
