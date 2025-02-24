package net.threetag.palladium.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.entity.EffectEntity;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class EntityEffect {

    @Environment(EnvType.CLIENT)
    public abstract void render(EffectEntityRenderer.EffectEntityRenderState renderState, Entity anchor, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTicks);

    public abstract void tick(EffectEntity entity, Entity anchor);

    public boolean isPlaying(EffectEntity entity) {
        return !entity.isDonePlaying();
    }

    public void stopPlaying(EffectEntity entity) {
        entity.setDonePlaying(true);
    }

    @Environment(EnvType.CLIENT)
    public static void start(Entity anchor, EntityEffect entityEffect) {
        EffectEntity effectEntity = new EffectEntity(anchor.level(), anchor, entityEffect);
        Objects.requireNonNull(Minecraft.getInstance().level).addEntity(effectEntity);
    }

    @Environment(EnvType.CLIENT)
    public static void stop(Entity anchor, Predicate<EntityEffect> predicate) {
        anchor.level().getEntities(anchor, anchor.getBoundingBox().inflate(2), entity -> entity instanceof EffectEntity && ((EffectEntity) entity).getAnchorEntity() == anchor && predicate.test(((EffectEntity) entity).entityEffect)).forEach(Entity::discard);
    }

    public static void stop(Entity anchor, EntityEffect entityEffectType) {
        stop(anchor, effect -> effect == entityEffectType);
    }

}
