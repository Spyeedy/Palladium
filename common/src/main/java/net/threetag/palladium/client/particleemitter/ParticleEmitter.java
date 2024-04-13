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
    private final int amount;
    private final Vector3f offset;
    private final Vector3f offsetRandom;
    private final Vector3f spread;
    private final Vector3f motion;
    private final Vector3f motionRandom;

    public ParticleEmitter(@Nullable BodyPart anchor, int amount, Vector3f offset, Vector3f offsetRandom, Vector3f spread, Vector3f motion, Vector3f motionRandom) {
        this.anchor = anchor;
        this.amount = amount;
        this.offset = offset;
        this.offsetRandom = offsetRandom;
        this.spread = spread;
        this.motion = motion;
        this.motionRandom = motionRandom;
    }

    public static ParticleEmitter fromJson(JsonObject json) {
        var bodyPart = BodyPart.byName(GsonHelper.getAsString(json, "body_part", ""));
        var amount = GsonUtil.getAsIntMin(json, "amount", 1, 1);
        var offset = GsonUtil.getAsVector3f(json, "offset", new Vector3f()).div(16);
        var offsetRandom = GsonUtil.getAsVector3f(json, "offset_random", new Vector3f()).div(16);
        var spread = GsonUtil.getAsVector3f(json, "spread", new Vector3f()).div(16);
        var motion = GsonUtil.getAsVector3f(json, "motion", new Vector3f());
        var motionRandom = GsonUtil.getAsVector3f(json, "motion_random", new Vector3f());

        return new ParticleEmitter(bodyPart, amount, offset, offsetRandom, spread, motion, motionRandom);
    }

    public Vec3 getCenter(AbstractClientPlayer player, float partialTick) {
        if (this.anchor != null) {
            return BodyPart.getInWorldPosition(this.anchor, this.offset, player, partialTick);
        } else {
            return player.getPosition(partialTick).add(0, player.getBbHeight() / 2D, 0).add(this.offset.x, this.offset.y, this.offset.z);
        }
    }

    public void spawnParticle(Level level, AbstractClientPlayer player, ParticleOptions particleOptions, float partialTick) {
        if (player == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            return;
        }

        var random = RandomSource.create();
        for (int i = 0; i < this.amount; i++) {
            Vector3f offset = new Vector3f(this.offset).add((random.nextFloat() - 0.5F) * 2F * this.offsetRandom.x, (random.nextFloat() - 0.5F) * 2F * this.offsetRandom.y, (random.nextFloat() - 0.5F) * 2F * this.offsetRandom.z);
            offset.x += (random.nextFloat() - 0.5F) * this.spread.x * 2;
            offset.y += (random.nextFloat() - 0.5F) * this.spread.y * 2;
            offset.z += (random.nextFloat() - 0.5F) * this.spread.z * 2;

            Vec3 pos = this.anchor != null ? BodyPart.getInWorldPosition(this.anchor, offset, player, partialTick) :
                    player.getPosition(partialTick).add(0, player.getBbHeight() / 2D, 0).add(offset.x, offset.y, offset.z);

            var motion = new Vector3f(this.motion).add((random.nextFloat() - 0.5F) * 2F * this.motionRandom.x, (random.nextFloat() - 0.5F) * 2F * this.motionRandom.y, (random.nextFloat() - 0.5F) * 2F * this.motionRandom.z);
            level.addParticle(particleOptions, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        }
    }

}
