package net.threetag.palladium.addonpack;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Map;

public class RecipeManagerWrapper extends RecipeManager {

    public RecipeManagerWrapper() {
        super(null);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {

    }
}
