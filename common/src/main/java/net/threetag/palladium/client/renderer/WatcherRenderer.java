package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.Easing;
import org.joml.Matrix4f;

public class WatcherRenderer implements ClientTickEvent.ClientLevel {

    public static final WatcherRenderer INSTANCE = new WatcherRenderer();
    private static final ResourceLocation TEXTURE = Palladium.id("textures/environment/watcher.png");
    private static final int OCCURRENCE_INTERVAL = 24000; // full Minecraft day
    private static final float OCCURRENCE_CHANCE = 0.01F; // 1% chance per day
    private static final int OCCURRENCE_DURATION = 20 * 60; // 1 minute
    private static final int OCCURRENCE_FADE = 40; // 2 seconds

    private int ticksTilOccurrence = OCCURRENCE_INTERVAL;
    private int visibleTicks = 0;
    private int visibility = 0;
    private int prevVisibility = 0;

    public static void init() {
        ClientTickEvent.CLIENT_LEVEL_POST.register(INSTANCE);
    }

    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float visibility) {
        if (visibility > 0F) {
            var easing = this.visibleTicks > 0 ? Easing.INSINE : Easing.OUTSINE;
            visibility = easing.apply(visibility);

            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(-135.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(60));
            float f = 40.0F;
            float g = 100.0F;
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.celestial(TEXTURE));
            int i = ARGB.white(visibility);
            Matrix4f matrix4f = poseStack.last().pose();
            vertexConsumer.addVertex(matrix4f, -f, g, -f).setUv(1.0F, 0.0F).setColor(i);
            vertexConsumer.addVertex(matrix4f, f, g, -f).setUv(0.0F, 0.0F).setColor(i);
            vertexConsumer.addVertex(matrix4f, f, g, f).setUv(0.0F, 1.0F).setColor(i);
            vertexConsumer.addVertex(matrix4f, -f, g, f).setUv(1.0F, 1.0F).setColor(i);
            bufferSource.endBatch();
            poseStack.popPose();
        }
    }

    @Override
    public void tick(net.minecraft.client.multiplayer.ClientLevel clientLevel) {
        this.prevVisibility = this.visibility;

        if (this.ticksTilOccurrence > 0) {
            this.ticksTilOccurrence--;

            if (this.ticksTilOccurrence <= 0) {
                if (Math.random() < OCCURRENCE_CHANCE) {
                    this.visibleTicks = OCCURRENCE_DURATION;
                }

                this.ticksTilOccurrence = (int) (OCCURRENCE_INTERVAL * (1 + Math.random()));
            }
        }

        if (this.visibleTicks > 0) {
            this.visibleTicks--;

            if (this.visibility < OCCURRENCE_FADE) {
                this.visibility++;
            }
        } else if (this.visibility > 0) {
            this.visibility--;
        }
    }

    public float getVisibility(float partialTick) {
        return Mth.lerp(partialTick, this.prevVisibility, this.visibility) / (float) OCCURRENCE_FADE;
    }
}
