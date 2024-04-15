package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.util.RenderUtil;

import java.awt.*;

public class LaserRenderer {

    private Color glowColor = Color.WHITE;
    private Color coreColor = Color.WHITE;
    private float glowOpacity = 1F;
    private float coreOpacity = 1F;
    private float thickness = 1F / 16F;
    private float length = 1F;
    private boolean normalTransparency = false;
    private float rotation = 0F;

    public LaserRenderer(Color color) {
        this.glowColor = color;
    }

    public LaserRenderer(Color glowColor, Color coreColor) {
        this.glowColor = glowColor;
        this.coreColor = coreColor;
    }

    public LaserRenderer color(Color color) {
        this.glowColor = color;
        return this;
    }

    public LaserRenderer color(Color glowColor, Color coreColor) {
        this.glowColor = glowColor;
        this.coreColor = coreColor;
        return this;
    }

    public LaserRenderer opacity(float opacity) {
        this.glowOpacity = this.coreOpacity = opacity;
        return this;
    }

    public LaserRenderer opacity(float glowOpacity, float coreOpacity) {
        this.glowOpacity = glowOpacity;
        this.coreOpacity = coreOpacity;
        return this;
    }

    public LaserRenderer thickness(float thickness) {
        this.thickness = thickness;
        return this;
    }

    public LaserRenderer length(float length) {
        this.length = length;
        return this;
    }

    public LaserRenderer dimensions(float thickness, float length) {
        this.thickness = thickness;
        this.length = length;
        return this;
    }

    public LaserRenderer normalTransparency() {
        this.normalTransparency = true;
        return this;
    }

    public LaserRenderer normalTransparency(boolean normalTransparency) {
        this.normalTransparency = normalTransparency;
        return this;
    }

    public LaserRenderer rotate(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public void face(PoseStack poseStack, Vec3 origin, Vec3 target) {
        RenderUtil.faceVec(poseStack, origin, target);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
    }

    public void faceAndRender(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 origin, Vec3 target) {
        poseStack.pushPose();
        this.face(poseStack, origin, target);
        this.render(poseStack, bufferSource);
        poseStack.popPose();
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(this.rotation % 360F));

        var consumer = bufferSource.getBuffer(this.normalTransparency ? PalladiumRenderTypes.LASER_NORMAL_TRANSPARENCY : PalladiumRenderTypes.LASER);
        AABB box = new AABB(-this.thickness / 2F, 0, -this.thickness / 2F, this.thickness / 2F, this.length, this.thickness / 2F);
        RenderUtil.renderFilledBox(poseStack, consumer, box, this.coreColor.getRed() / 255F, this.coreColor.getGreen() / 255F, this.coreColor.getBlue() / 255F, this.coreOpacity, 15728640);
        var r = this.glowColor.getRed() / 255F;
        var g = this.glowColor.getGreen() / 255F;
        var b = this.glowColor.getBlue() / 255F;

        for (int i = 0; i < 3; i++) {
            RenderUtil.renderFilledBox(poseStack, consumer, box.inflate(i * 0.5F * 0.0625F), r, g, b, (1F / i / 2) * this.glowOpacity, 15728640);
        }
        poseStack.popPose();
    }
}
