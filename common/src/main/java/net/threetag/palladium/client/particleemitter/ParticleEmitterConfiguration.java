package net.threetag.palladium.client.particleemitter;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticleEmitterConfiguration {

    private final List<ParticleEmitter> emitters;

    public ParticleEmitterConfiguration(List<ParticleEmitter> emitters) {
        this.emitters = emitters;
    }

    public void spawnParticles(Level level, AbstractClientPlayer player, ParticleOptions particleOptions, float partialTick) {
        for (ParticleEmitter emitter : this.emitters) {
            emitter.spawnParticles(level, player, particleOptions, partialTick);
        }
    }

    public static ParticleEmitterConfiguration fromJson(JsonElement json) {
        if (json.isJsonArray()) {
            List<ParticleEmitter> emitterList = new ArrayList<>();
            var array = json.getAsJsonArray();
            for (JsonElement jsonElement : array) {
                emitterList.add(ParticleEmitter.fromJson(GsonHelper.convertToJsonObject(jsonElement, "$[]")));
            }
            return new ParticleEmitterConfiguration(emitterList);
        } else if (json.isJsonObject()) {
            return new ParticleEmitterConfiguration(Collections.singletonList(ParticleEmitter.fromJson(json.getAsJsonObject())));
        } else {
            throw new JsonSyntaxException("Particle emitter configuration must be either an object or array of multiple objects");
        }
    }

}
