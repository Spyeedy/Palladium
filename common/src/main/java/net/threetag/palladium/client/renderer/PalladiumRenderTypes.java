package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.threetag.palladium.Palladium;

public class PalladiumRenderTypes extends RenderType {

    public PalladiumRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public static final RenderType LASER = create(Palladium.MOD_ID + ":laser", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
            .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
            .setTextureState(NO_TEXTURE)
            .setCullState(NO_CULL)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setLightmapState(LIGHTMAP)
            .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
            .setLayeringState(POLYGON_OFFSET_LAYERING)
            .createCompositeState(true));

    public static final RenderType LASER_NORMAL_TRANSPARENCY = create(Palladium.MOD_ID + ":laser_normal_transparency  ", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
            .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
            .setTextureState(NO_TEXTURE)
            .setCullState(NO_CULL)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setLightmapState(LIGHTMAP)
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setLayeringState(POLYGON_OFFSET_LAYERING)
            .createCompositeState(true));

}
