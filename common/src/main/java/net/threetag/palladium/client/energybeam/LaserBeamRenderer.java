package net.threetag.palladium.client.energybeam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.util.SizeUtil;

public class LaserBeamRenderer extends EnergyBeamRenderer {

    public static final MapCodec<LaserBeamRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LaserRenderer.codec(2).fieldOf("laser").forGetter(beam -> beam.laserRenderer)
    ).apply(instance, LaserBeamRenderer::new));

    private final LaserRenderer laserRenderer;

    public LaserBeamRenderer(LaserRenderer laserRenderer) {
        this.laserRenderer = laserRenderer;
    }

    @Override
    public void render(AbstractClientPlayer player, Vec3 origin, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        this.laserRenderer
                .faceAndRender(poseStack, bufferSource, origin, target, player.tickCount, partialTick, lengthMultiplier, 1F,
                        new Vec2(
                                SizeUtil.getInstance().getModelWidthScale(player, partialTick),
                                SizeUtil.getInstance().getModelHeightScale(player, partialTick)
                        ));
    }

    @Override
    public EnergyBeamRendererSerializer<?> getSerializer() {
        return EnergyBeamRendererSerializers.LASER;
    }

    public static class Serializer extends EnergyBeamRendererSerializer<LaserBeamRenderer> {

        @Override
        public MapCodec<LaserBeamRenderer> codec() {
            return CODEC;
        }
    }
}
