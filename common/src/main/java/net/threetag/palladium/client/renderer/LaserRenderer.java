package net.threetag.palladium.client.renderer;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.RenderUtil;
import net.threetag.palladium.util.json.GsonUtil;
import org.joml.Vector2f;

import java.awt.*;

public class LaserRenderer {

    private Color glowColor = Color.WHITE;
    private Color coreColor = Color.WHITE;
    private float glowOpacity = 1F;
    private float coreOpacity = 1F;
    private Vector2f size = new Vector2f(1F / 16F, 1F / 16F);
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

    public Color getCoreColor() {
        return this.coreColor;
    }

    public Color getGlowColor() {
        return this.glowColor;
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

    public float getCoreOpacity() {
        return this.coreOpacity;
    }

    public float getGlowOpacity() {
        return this.glowOpacity;
    }

    public LaserRenderer size(float size) {
        return this.size(size, size);
    }

    public LaserRenderer size(float width, float height) {
        this.size = new Vector2f(width, height);
        return this;
    }

    public LaserRenderer size(Vector2f size) {
        this.size = size;
        return this;
    }

    public Vector2f getSize() {
        return this.size;
    }

    public LaserRenderer length(float length) {
        this.length = length;
        return this;
    }

    public float getLength() {
        return this.length;
    }

    public LaserRenderer normalTransparency() {
        this.normalTransparency = true;
        return this;
    }

    public LaserRenderer normalTransparency(boolean normalTransparency) {
        this.normalTransparency = normalTransparency;
        return this;
    }

    public boolean hasNormalTransparency() {
        return this.normalTransparency;
    }

    public LaserRenderer rotate(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public float getRotation() {
        return this.rotation;
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
        AABB box = new AABB(-this.size.x / 2F, 0, -this.size.y / 2F, this.size.x / 2F, this.length, this.size.y / 2F);
        RenderUtil.renderFilledBox(poseStack, consumer, box, this.coreColor.getRed() / 255F, this.coreColor.getGreen() / 255F, this.coreColor.getBlue() / 255F, this.coreOpacity, 15728640);
        var r = this.glowColor.getRed() / 255F;
        var g = this.glowColor.getGreen() / 255F;
        var b = this.glowColor.getBlue() / 255F;

        for (int i = 0; i < 3; i++) {
            RenderUtil.renderFilledBox(poseStack, consumer, box.inflate(i * 0.5F * 0.0625F), r, g, b, (1F / i / 2) * this.glowOpacity, 15728640);
        }
        poseStack.popPose();
    }

    public static LaserRenderer fromJson(JsonObject json) {
        var laser = new LaserRenderer(
                GsonUtil.getAsColor(json, "glow_color", Color.WHITE),
                GsonUtil.getAsColor(json, "core_color", Color.WHITE))
                .opacity(GsonUtil.getAsFloatRanged(json, "glow_opacity", 0F, 1F, 1F),
                        GsonUtil.getAsFloatRanged(json, "core_opacity", 0F, 1F, 1F))
                .size(parseSize(json, "size", new Vector2f(1, 1)).mul(1F / 16F))
                .length(GsonUtil.getAsFloatMin(json, "length", 0F, 1F) / 16F)
                .normalTransparency(GsonHelper.getAsBoolean(json, "normal_transparency", false))
                .rotate(GsonUtil.getAsFloatMin(json, "rotation", 0F, 0F));

        if (json.has("thickness")) {
            laser.size(GsonUtil.getAsFloatMin(json, "thickness", 0, 1) / 16F);
        }

        return laser;
    }

    private static Vector2f parseSize(JsonObject json, String memberName, Vector2f fallback) {
        if (json.has(memberName)) {
            var el = json.get(memberName);

            if (el.isJsonPrimitive()) {
                var val = GsonHelper.convertToFloat(json, memberName);
                return new Vector2f(val, val);
            } else if (el.isJsonArray()) {
                var array = el.getAsJsonArray();

                if (array.size() != 2) {
                    throw new JsonSyntaxException("Size must be an array of 2 numbers");
                }

                return new Vector2f(array.get(0).getAsFloat(), array.get(1).getAsFloat());
            } else {
                throw new JsonSyntaxException(memberName + " must be a simple number or an array of 2");
            }
        } else {
            return fallback;
        }
    }

    public void generateDocumentation(JsonDocumentationBuilder builder) {

    }
}
