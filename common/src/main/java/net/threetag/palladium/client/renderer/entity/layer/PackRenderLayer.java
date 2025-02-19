package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.data.DataContext;

public abstract class PackRenderLayer {

    public static Codec<PackRenderLayer> CODEC = PackRenderLayerSerializer.TYPE_CODEC.dispatch(PackRenderLayer::getSerializer, PackRenderLayerSerializer::codec);

    protected final PerspectiveAwareConditions conditions;

    protected PackRenderLayer(PerspectiveAwareConditions conditions) {
        this.conditions = conditions;
    }

    public abstract void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState state, int packedLight, float yRot, float xRot);

    public boolean shouldRender(State state, PerspectiveAwareConditions.Perspective perspective) {
        return this.conditions.test(state.context, perspective);
    }

    public State createState(DataContext context) {
        return new State(this, context);
    }

    public boolean isOrContains(PackRenderLayer layer) {
        return this == layer;
    }

    public abstract PackRenderLayerSerializer<?> getSerializer();

    protected static <B extends PackRenderLayer> RecordCodecBuilder<B, PerspectiveAwareConditions> conditionsCodec() {
        return PerspectiveAwareConditions.CODEC.optionalFieldOf("conditions", PerspectiveAwareConditions.EMPTY).forGetter(l -> l.conditions);
    }

    public static class State {

        public final PackRenderLayer renderLayer;
        private DataContext context;
        public int ticks = 0;
        private boolean markedForRemoval = false;

        public State(PackRenderLayer renderLayer, DataContext context) {
            this.renderLayer = renderLayer;
            this.context = context;
        }

        public void tick(LivingEntity entity) {
            this.ticks++;
        }

        public void updateContext(DataContext context) {
            this.context = context;
        }

        public DataContext getContext() {
            return this.context;
        }

        public void markForRemoval() {
            this.markedForRemoval = true;
        }

        public boolean isMarkedForRemoval() {
            return this.markedForRemoval;
        }

    }

}
