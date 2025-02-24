package net.threetag.palladium.client.energybeam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.util.CodecExtras;
import net.threetag.palladium.util.EntityScaleUtil;

public class LightningBeamRenderer extends EnergyBeamRenderer {

    public static final MapCodec<LightningBeamRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LaserRenderer.codec(1).fieldOf("laser").forGetter(beam -> beam.laserRenderer),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("segments", 5).forGetter(beam -> beam.segments),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("frequency", 2).forGetter(beam -> beam.frequency),
            CodecExtras.NON_NEGATIVE_VOXEL_FLOAT.optionalFieldOf("spread", 5F).forGetter(beam -> beam.spread)
    ).apply(instance, LightningBeamRenderer::new));

    private final LaserRenderer laserRenderer;
    private final int segments;
    private final int frequency;
    private final float spread;

    public LightningBeamRenderer(LaserRenderer laserRenderer, int segments, int frequency, float spread) {
        this.laserRenderer = laserRenderer;
        this.segments = segments;
        this.frequency = frequency;
        this.spread = spread;
    }

    @Override
    public void render(AbstractClientPlayer player, Vec3 origin, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        var widthScale = EntityScaleUtil.getInstance().getModelWidthScale(player, partialTick);
        var heightScale = EntityScaleUtil.getInstance().getModelHeightScale(player, partialTick);
        var scale = new Vec2(widthScale, heightScale);
        var segmentPartVec = target.subtract(origin).scale(1F / this.segments);
        var randomStart = RandomSource.create(player.getId() + (player.tickCount / this.frequency));
        var startVec = origin;

        for (int i = 0; i < this.segments; i++) {
            var startProgress = (1F / this.segments) * i;
            var endProgress = (1F / this.segments) * (i + 1);
            var currentProgress = lengthMultiplier <= startProgress ? 0F : (lengthMultiplier >= endProgress ? 1F : (lengthMultiplier - startProgress) / (endProgress - startProgress));

            if (currentProgress > 0F) {
                var end = i == this.segments - 1 ? target : origin.add(segmentPartVec.scale(i + 1)).add(randomizeVector(randomStart, this.spread));
                var offset = startVec.subtract(origin);

                poseStack.pushPose();
                poseStack.translate(offset.x, offset.y, offset.z);
                this.laserRenderer
                        .faceAndRender(poseStack, bufferSource, startVec, end, player.tickCount, partialTick, currentProgress, 1F, scale);
                poseStack.popPose();
                startVec = end;
            }
        }
    }

    private static Vec3 randomizeVector(RandomSource random, float spread) {
        return new Vec3((random.nextFloat() - 0.5F) * 2F * spread, (random.nextFloat() - 0.5F) * 2F * spread, (random.nextFloat() - 0.5F) * 2F * spread);
    }

    @Override
    public EnergyBeamRendererSerializer<?> getSerializer() {
        return EnergyBeamRendererSerializers.LIGHTNING;
    }

    public static class Serializer extends EnergyBeamRendererSerializer<LightningBeamRenderer> {

        @Override
        public MapCodec<LightningBeamRenderer> codec() {
            return CODEC;
        }
    }
}
