package net.threetag.palladium.client.renderer.entity.layer;

import net.threetag.palladium.Palladium;

public class PackRenderLayerSerializers {

    public static final PackRenderLayerSerializer<?> DEFAULT = register("render_layer", new DefaultPackRenderLayer.Serializer());
    public static final PackRenderLayerSerializer<?> COMPOUND = register("compound", new CompoundPackRenderLayer.Serializer());

    private static <T extends PackRenderLayer> PackRenderLayerSerializer<T> register(String id, PackRenderLayerSerializer<T> serializer) {
        return PackRenderLayerSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
