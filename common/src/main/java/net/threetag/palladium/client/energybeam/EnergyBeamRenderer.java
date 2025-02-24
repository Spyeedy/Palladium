package net.threetag.palladium.client.energybeam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

public abstract class EnergyBeamRenderer {

    public static Codec<EnergyBeamRenderer> CODEC = EnergyBeamRendererSerializer.TYPE_CODEC.dispatch(EnergyBeamRenderer::getSerializer, EnergyBeamRendererSerializer::codec);

    public abstract void render(AbstractClientPlayer player, Vec3 origin, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick);

    public abstract EnergyBeamRendererSerializer<?> getSerializer();

}
