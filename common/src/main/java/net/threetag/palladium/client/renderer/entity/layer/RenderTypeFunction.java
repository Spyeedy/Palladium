package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public interface RenderTypeFunction {

    VertexConsumer createVertexConsumer(MultiBufferSource buffer, ResourceLocation texture, boolean withGlint);

    default int getPackedLight(int packedLight) {
        return packedLight;
    }

}
