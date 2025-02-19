package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.util.RenderUtil;

public class PackRenderLayerRenderer<T extends LivingEntityRenderState> extends RenderLayer<T, EntityModel<T>> {

    public PackRenderLayerRenderer(RenderLayerParent<T, EntityModel<T>> renderer) {
        super(renderer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntityRenderState renderState, float yRot, float xRot) {
        var entity = RenderUtil.getCurrentlyRenderedEntity();
        var l = PalladiumEntityData.get(entity, PalladiumEntityDataTypes.RENDER_LAYERS.get());

        if (l instanceof ClientEntityRenderLayers layers) {
            layers.getLayerStates().forEach((layer, state) -> {
                if (layer.shouldRender(state, PerspectiveAwareConditions.Perspective.THIRD_PERSON)) {
                    EntityModel model = this.getParentModel();
                    layer.render(state.getContext(), poseStack, bufferSource, model, renderState, packedLight, yRot, xRot);
                }
            });
        }
    }
}
