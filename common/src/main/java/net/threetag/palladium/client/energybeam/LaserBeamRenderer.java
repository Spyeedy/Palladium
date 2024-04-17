package net.threetag.palladium.client.energybeam;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.SizeUtil;
import net.threetag.palladium.util.json.GsonUtil;
import org.joml.Vector2f;

public class LaserBeamRenderer extends EnergyBeamRenderer {

    private final float rotationSpeed;
    private final LaserRenderer laserRenderer;

    public LaserBeamRenderer(float rotationSpeed, LaserRenderer laserRenderer) {
        this.rotationSpeed = rotationSpeed;
        this.laserRenderer = laserRenderer;
    }

    @Override
    public void render(AbstractClientPlayer player, Vec3 origin, Vec3 target, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        if (this.rotationSpeed > 0F) {
            this.laserRenderer.rotate((player.tickCount + partialTick) * rotationSpeed);
        }

        var size = this.laserRenderer.getSize();

        this.laserRenderer
                .size(size.mul(SizeUtil.getInstance().getModelWidthScale(player), SizeUtil.getInstance().getModelHeightScale(player), new Vector2f()))
                .length((float) origin.distanceTo(target))
                .faceAndRender(poseStack, bufferSource, origin, target);

        this.laserRenderer.size(size);
    }

    public static class Serializer extends EnergyBeamRenderer.Serializer {

        public static final ResourceLocation ID = Palladium.id("laser");

        @Override
        public EnergyBeamRenderer fromJson(JsonObject json) {
            return new LaserBeamRenderer(GsonUtil.getAsIntMin(json, "rotation_speed", 0, 0), LaserRenderer.fromJson(json));
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {

        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
