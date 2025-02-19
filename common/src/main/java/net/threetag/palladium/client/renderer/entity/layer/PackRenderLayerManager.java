package net.threetag.palladium.client.renderer.entity.layer;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.HashMap;
import java.util.Map;

public class PackRenderLayerManager extends SimpleJsonResourceReloadListener<PackRenderLayer> {

    public static final PackRenderLayerManager INSTANCE = new PackRenderLayerManager();

    private final Map<ResourceLocation, PackRenderLayer> byName = new HashMap<>();

    protected PackRenderLayerManager() {
        super(PackRenderLayer.CODEC, FileToIdConverter.json("palladium/render_layers"));
    }

    @Override
    protected void apply(Map<ResourceLocation, PackRenderLayer> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " render layers");
    }

    public PackRenderLayer get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
