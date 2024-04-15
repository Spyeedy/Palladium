package net.threetag.palladium.client.particleemitter;

import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class ParticleEmitter {

    @Nullable
    private final BodyPart anchor;
    private final float amount;
    private final Vector3f offset;
    private final Vector3f offsetRandom;
    private final Vector3f motion;
    private final Vector3f motionRandom;
    private final boolean visibleInFirstPerson;

    public ParticleEmitter(@Nullable BodyPart anchor, float amount, Vector3f offset, Vector3f offsetRandom, Vector3f motion, Vector3f motionRandom, boolean visibleInFirstPerson) {
        this.anchor = anchor;
        this.amount = amount;
        this.offset = offset;
        this.offsetRandom = offsetRandom;
        this.motion = motion;
        this.motionRandom = motionRandom;
        this.visibleInFirstPerson = visibleInFirstPerson;
    }

    public static ParticleEmitter fromJson(JsonObject json) {
        var bodyPart = BodyPart.byName(GsonHelper.getAsString(json, "body_part", ""));
        var amount = GsonUtil.getAsFloatMin(json, "amount", 0, 1);
        var offset = GsonUtil.getAsVector3f(json, "offset", new Vector3f()).div(16, -16, 16);
        var offsetRandom = GsonUtil.getAsVector3f(json, "offset_random", new Vector3f()).div(16, -16, 16);
        var motion = GsonUtil.getAsVector3f(json, "motion", new Vector3f()).div(16, -16, 16);
        var motionRandom = GsonUtil.getAsVector3f(json, "motion_random", new Vector3f()).div(16, -16, 16);
        var visibleInFirstPerson = GsonHelper.getAsBoolean(json, "visible_in_first_person", true);

        return new ParticleEmitter(bodyPart, amount, offset, offsetRandom, motion, motionRandom, visibleInFirstPerson);
    }

    public Vec3 getCenter(AbstractClientPlayer player, float partialTick) {
        if (this.anchor != null) {
            return BodyPart.getInWorldPosition(this.anchor, this.offset, player, partialTick);
        } else {
            return player.getPosition(partialTick).add(0, player.getBbHeight() / 2D, 0).add(this.offset.x, this.offset.y, this.offset.z);
        }
    }

    public void spawnParticles(Level level, AbstractClientPlayer player, ParticleOptions particleOptions, float partialTick) {
        if (!this.visibleInFirstPerson && player == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            return;
        }

        var random = RandomSource.create();

        if (this.amount < 1) {
            if (Math.random() < this.amount) {
                this.spawnParticleOnPlayer(level, player, particleOptions, random, partialTick);
            }
        } else {
            for (int i = 0; i < this.amount; i++) {
                this.spawnParticleOnPlayer(level, player, particleOptions, random, partialTick);
            }
        }
    }

    public void spawnAtPosition(Level level, Vec3 position, ParticleOptions particleOptions) {
        var random = RandomSource.create();

        if (this.amount < 1) {
            if (Math.random() < this.amount) {
                this.spawnParticleOnPosition(level, position, particleOptions, random);
            }
        } else {
            for (int i = 0; i < this.amount; i++) {
                this.spawnParticleOnPosition(level, position, particleOptions, random);
            }
        }
    }

    private void spawnParticleOnPlayer(Level level, AbstractClientPlayer player, ParticleOptions particleOptions, RandomSource random, float partialTick) {
        Vector3f offset = randomizeVector(random, this.offset, this.offsetRandom);
        var motion = randomizeVector(random, this.motion, this.motionRandom);

        Vec3 pos = this.anchor != null ? BodyPart.getInWorldPosition(this.anchor, offset, player, partialTick) :
                player.getPosition(partialTick).add(0, player.getBbHeight() / 2D, 0).add(offset.x, offset.y, offset.z);

        if (this.anchor != null) {
            var combined = new Vector3f(offset).add(motion);
            var transformed = BodyPart.getInWorldPosition(this.anchor, combined, player, partialTick);
            var motionRotated = transformed.subtract(pos);
            level.addParticle(particleOptions, pos.x, pos.y, pos.z, motionRotated.x, motionRotated.y, motionRotated.z);
        } else {
            level.addParticle(particleOptions, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        }
    }

    private void spawnParticleOnPosition(Level level, Vec3 position, ParticleOptions particleOptions, RandomSource random) {
        Vector3f offset = randomizeVector(random, this.offset, this.offsetRandom);
        var motion = randomizeVector(random, this.motion, this.motionRandom);
        Vec3 pos = position.add(offset.x, -offset.y, offset.z);
        level.addParticle(particleOptions, pos.x, pos.y, pos.z, motion.x, -motion.y, motion.z);
    }

    private static Vector3f randomizeVector(RandomSource random, Vector3f center, Vector3f randomOffset) {
        return new Vector3f(center).add((random.nextFloat() - 0.5F) * 2F * randomOffset.x, (random.nextFloat() - 0.5F) * 2F * randomOffset.y, (random.nextFloat() - 0.5F) * 2F * randomOffset.z);
    }

}
