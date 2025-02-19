package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.data.DataContext;

import java.util.List;

public class CompoundPackRenderLayer extends PackRenderLayer {

    public static final MapCodec<CompoundPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PackRenderLayer.CODEC.listOf().fieldOf("layers").forGetter(l -> l.layers),
            conditionsCodec()
    ).apply(instance, CompoundPackRenderLayer::new));

    private final List<PackRenderLayer> layers;

    protected CompoundPackRenderLayer(List<PackRenderLayer> layers, PerspectiveAwareConditions conditions) {
        super(conditions);
        this.layers = layers;
    }

    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState state, int packedLight, float yRot, float xRot) {
        // nothing
    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return PackRenderLayerSerializers.COMPOUND;
    }

    @Override
    public boolean isOrContains(PackRenderLayer layer) {
        if (super.isOrContains(layer)) {
            return true;
        }

        for (PackRenderLayer child : this.layers) {
            if (child.isOrContains(layer)) {
                return true;
            }
        }

        return false;
    }

    public List<PackRenderLayer> getLayers() {
        return this.layers;
    }

    public static class Serializer extends PackRenderLayerSerializer<CompoundPackRenderLayer> {

        @Override
        public MapCodec<CompoundPackRenderLayer> codec() {
            return CODEC;
        }

    }
}
