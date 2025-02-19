package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.Easing;
import net.threetag.palladiumcore.event.ClientTickEvents;
import org.joml.Matrix4f;

public class WatcherRenderer implements ClientTickEvents.ClientLevelTick {

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
        ClientTickEvents.CLIENT_LEVEL_POST.register(INSTANCE);
    }

    public void render(PoseStack poseStack, float visibility) {
        if (visibility > 0F) {
            var easing = this.visibleTicks > 0 ? Easing.INSINE : Easing.OUTSINE;
            visibility = easing.apply(visibility);

            poseStack.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            poseStack.mulPose(Axis.YP.rotationDegrees(-135.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(60F));
            Matrix4f matrix4f2 = poseStack.last().pose();
            float f = 40.0F;
            float g = 100.0F;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, visibility);
            RenderSystem.setShaderTexture(0, TEXTURE);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(matrix4f2, -f, g, -f).uv(1.0F, 0.0F).endVertex();
            bufferBuilder.vertex(matrix4f2, f, g, -f).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.vertex(matrix4f2, f, g, f).uv(0.0F, 1.0F).endVertex();
            bufferBuilder.vertex(matrix4f2, -f, g, f).uv(1.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferBuilder.end());
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            poseStack.popPose();
        }
    }

    @Override
    public void clientLevelTick(Minecraft minecraft, ClientLevel clientLevel) {
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
