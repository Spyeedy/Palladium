package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.serialization.Codec;
import dev.architectury.platform.Platform;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.StringRepresentable;
import net.threetag.palladium.data.DataContext;
import team.unnamed.mocha.MochaEngine;
import team.unnamed.mocha.runtime.MochaFunction;
import team.unnamed.mocha.runtime.binding.JavaObjectBinding;

import java.util.HashMap;
import java.util.Map;

public class PackRenderLayerAnimations {

    public static final Codec<PackRenderLayerAnimations> CODEC = Codec.unboundedMap(Codec.STRING, PartAnimation.CODEC)
            .xmap(PackRenderLayerAnimations::new, packRenderLayerAnimations -> packRenderLayerAnimations.animations);
    public static final PackRenderLayerAnimations EMPTY = new PackRenderLayerAnimations(Map.of());

    private final Map<String, PartAnimation> animations;

    public PackRenderLayerAnimations(Map<String, PartAnimation> animations) {
        this.animations = animations;

        if (!this.animations.isEmpty()) {
            MochaEngine<?> mocha = MochaEngine.createStandard();

            if (!Platform.isNeoForge()) {
                mocha.scope().set("query", JavaObjectBinding.of(MoLangQuery.class, MoLangQuery.INSTANCE, null));
                this.animations.values().forEach(partAnimation -> partAnimation.build(mocha));
            }
        }
    }

    public void animate(Model model, DataContext context) {
        if (!this.animations.isEmpty()) {
            this.animations.forEach((bone, animation) -> {
                var part = getPart(model, bone);

                if (part != null) {
                    MoLangQuery.CONTEXT = context;
                    animation.animate(part);
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

        public void animate(ModelPart part) {
            if (this.animations != null && !this.animations.isEmpty()) {
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

}
