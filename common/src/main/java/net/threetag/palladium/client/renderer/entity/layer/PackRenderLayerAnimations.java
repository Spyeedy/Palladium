package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.StringRepresentable;
import net.threetag.palladium.data.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.mocha.MochaEngine;
import team.unnamed.mocha.runtime.MochaFunction;
import team.unnamed.mocha.runtime.binding.Binding;
import team.unnamed.mocha.runtime.binding.JavaObjectBinding;
import team.unnamed.mocha.runtime.value.ObjectProperty;
import team.unnamed.mocha.runtime.value.ObjectValue;
import team.unnamed.mocha.runtime.value.Value;

import java.util.HashMap;
import java.util.Map;

public class PackRenderLayerAnimations {

    public static final Codec<PackRenderLayerAnimations> CODEC = Codec.unboundedMap(Codec.STRING, PartAnimation.CODEC)
            .xmap(PackRenderLayerAnimations::new, packRenderLayerAnimations -> packRenderLayerAnimations.animations);
    public static final PackRenderLayerAnimations EMPTY = new PackRenderLayerAnimations(Map.of());

    private final Map<String, PartAnimation> animations;
    private final MochaEngine<?> mocha;

    public PackRenderLayerAnimations(Map<String, PartAnimation> animations) {
        this.animations = animations;

        if (this.animations.isEmpty()) {
            this.mocha = null;
        } else {
            this.mocha = MochaEngine.createStandard();
            this.mocha.scope().set("query", JavaObjectBinding.of(Query.class, Query.INSTANCE, null));
            this.animations.values().forEach(partAnimation -> partAnimation.build(this.mocha));
        }
    }

    public void animate(Model model, DataContext context) {
        if (!this.animations.isEmpty()) {
            this.animations.forEach((bone, animation) -> {
                var part = getPart(model, bone);

                if (part != null) {
                    Query.CONTEXT = context;
                    animation.animate(part, this.mocha);
                }
            });
        }
    }

    public ModelPart getPart(Model model, String name) {
        if (name.contains(".")) {
            String[] split = name.split("\\.");
            ModelPart part = model.root().getChild(split[0]);
            for (int i = 1; i < split.length; i++) {
                part = part.getChild(split[i]);
            }
            return part;
        } else {
            return model.root().getChild(name);
        }
    }

    public enum PartAnimationType implements StringRepresentable {

        X("x"),
        Y("y"),
        Z("z"),
        X_ROT("x_rot"),
        Y_ROT("y_rot"),
        Z_ROT("z_rot"),
        X_SCALE("x_scale"),
        Y_SCALE("y_scale"),
        Z_SCALE("z_scale");

        public static final Codec<PartAnimationType> CODEC = StringRepresentable.fromEnum(PartAnimationType::values);
        private final String name;

        PartAnimationType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public static class PartAnimation {

        public static final Codec<PartAnimation> CODEC = Codec.unboundedMap(PartAnimationType.CODEC, Codec.STRING)
                .xmap(PartAnimation::new, partAnimation -> partAnimation.animationsRaw);

        private final Map<PartAnimationType, String> animationsRaw;
        private Map<PartAnimationType, MochaFunction> animations;

        public PartAnimation(Map<PartAnimationType, String> animationsRaw) {
            this.animationsRaw = animationsRaw;
        }

        public void build(MochaEngine<?> mocha) {
            this.animations = new HashMap<>();
            this.animationsRaw.forEach((type, value) -> {
                this.animations.put(type, mocha.compile(value));
            });
        }

        public void animate(ModelPart part, MochaEngine<?> mocha) {
            if (!this.animations.isEmpty()) {
                this.animations.forEach((type, value) -> {
                    float val = (float) value.evaluate();

                    switch (type) {
                        case X -> part.x = val;
                        case Y -> part.y = val;
                        case Z -> part.z = val;
                        case X_ROT -> part.xRot = val;
                        case Y_ROT -> part.yRot = val;
                        case Z_ROT -> part.zRot = val;
                        case X_SCALE -> part.xScale = val;
                        case Y_SCALE -> part.yScale = val;
                        case Z_SCALE -> part.zScale = val;
                    }
                });
            }
        }
    }

    @Binding("query")
    public static class Query implements ObjectValue {

        public static final Query INSTANCE = new Query();
        public static DataContext CONTEXT = null;

        @Binding("get_age")
        public double get_age() {
            var entity = CONTEXT.getEntity();
            return entity != null ? entity.tickCount + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() : 0;
        }

        @Override
        public @Nullable ObjectProperty getProperty(@NotNull String name) {
            if (name.equals("get_age")) {
                return ObjectProperty.property(Value.of(get_age()), false);
            }
            return null;
        }
    }

}
