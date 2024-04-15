package net.threetag.palladium.client.energybeam;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.particleemitter.ParticleEmitter;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.util.json.GsonUtil;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class EnergyBeam {

    private final BodyPart anchor;
    private final Vector3f offset;
    private final float rotationSpeed;
    private final LaserRenderer laserRenderer;
    private final List<Particle> particles;

    public EnergyBeam(BodyPart anchor, Vector3f offset, float rotationSpeed, Color glowColor, Color coreColor, float glowOpacity, float coreOpacity, float thickness, boolean normalTransparency, List<Particle> particles) {
        this.anchor = anchor;
        this.offset = offset;
        this.rotationSpeed = rotationSpeed;
        this.laserRenderer = new LaserRenderer(glowColor, coreColor)
                .opacity(glowOpacity, coreOpacity)
                .thickness(thickness)
                .normalTransparency(normalTransparency);
        this.particles = particles;
    }

    public Vec3 getOriginPosition(AbstractClientPlayer player, float partialTick) {
        return BodyPart.getInWorldPosition(this.anchor, this.offset, player, partialTick);
    }

    public void spawnParticles(Level level, Vec3 pos) {
        for (Particle particle : this.particles) {
            particle.spawn(level, pos);
        }
    }

    public void render(AbstractClientPlayer player, Vec3 anchor, Vec3 target, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        var origin = this.getOriginPosition(player, partialTick).subtract(anchor);
        target = target.subtract(anchor);

        poseStack.pushPose();
        poseStack.translate(origin.x, origin.y, origin.z);

        if (this.rotationSpeed > 0F) {
            this.laserRenderer.rotate((player.tickCount + partialTick) * rotationSpeed);
        }

        this.laserRenderer
                .length((float) origin.distanceTo(target))
                .faceAndRender(poseStack, bufferSource, origin, target);
        poseStack.popPose();
    }

    public static EnergyBeam fromJson(JsonObject json) {
        return new EnergyBeam(
                BodyPart.fromJson(GsonHelper.getAsString(json, "body_part", "")),
                GsonUtil.getAsVector3f(json, "offset", new Vector3f()).div(16, -16, 16),
                GsonUtil.getAsIntMin(json, "rotation_speed", 0, 0),
                GsonUtil.getAsColor(json, "glow_color", Color.WHITE),
                GsonUtil.getAsColor(json, "core_color", Color.WHITE),
                GsonUtil.getAsFloatRanged(json, "glow_opacity", 0F, 1F, 1F),
                GsonUtil.getAsFloatRanged(json, "core_opacity", 0F, 1F, 1F),
                GsonUtil.getAsFloatMin(json, "thickness", 0F, 1F) / 16F,
                GsonHelper.getAsBoolean(json, "normal_transparency", false),
                json.has("particles") ? GsonUtil.fromListOrPrimitive(json.get("particles"), jsonElement -> Particle.fromJson(GsonHelper.convertToJsonObject(jsonElement, "particles[].$"))) : Collections.emptyList()
        );
    }

    public record Particle(ParticleType<?> particleType, String options, ParticleEmitter emitter) {

        public static Particle fromJson(JsonObject json) {
            var particleTypeId = GsonUtil.getAsResourceLocation(json, "particle_type");

            if (!BuiltInRegistries.PARTICLE_TYPE.containsKey(particleTypeId)) {
                throw new JsonParseException("Unknown particle type '" + particleTypeId + "'");
            }

            var particleType = BuiltInRegistries.PARTICLE_TYPE.get(particleTypeId);
            var options = GsonHelper.getAsString(json, "options", "");
            var emitter = ParticleEmitter.fromJson(json);
            return new Particle(particleType, options, emitter);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public void spawn(Level level, Vec3 pos) {
            try {
                ParticleType type = this.particleType;
                ParticleOptions options = type.getDeserializer().fromCommand(type, new StringReader(" " + this.options.trim() + " "));
                this.emitter.spawnAtPosition(level, pos, options);
            } catch (CommandSyntaxException ignored) {

            }
        }
    }

}
