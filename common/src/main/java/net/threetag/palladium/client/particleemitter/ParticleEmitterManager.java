package net.threetag.palladium.client.particleemitter;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ParticleEmitterManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static ParticleEmitterManager INSTANCE = new ParticleEmitterManager();

    public Map<ResourceLocation, ParticleEmitter> byName = ImmutableMap.of();

    public ParticleEmitterManager() {
        super(GSON, "palladium/particle_emitters");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, ParticleEmitter> builder = ImmutableMap.builder();
        object.forEach((id, json) -> {
            try {
                builder.put(id, ParticleEmitter.fromJson(GsonHelper.convertToJsonObject(json, "$")));
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading particle emitter {}", id, e);
            }
        });
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} particle emitters", this.byName.size());
    }

    @Nullable
    public ParticleEmitter get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
