package net.threetag.palladium.addonpack.fabric;

import net.fabricmc.fabric.impl.resource.loader.FabricRecipeManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;

public class AddonPackManagerImpl {

    public static void afterPackRepositoryCreation(PackRepository packRepository) {
        // nothing
    }

    public static RecipeManager getRecipeManager() {
        return new RecipeManagerWrapper();
    }

    public static class RecipeManagerWrapper extends RecipeManager implements FabricRecipeManager {

        public RecipeManagerWrapper() {
            super(null);
        }

        @Override
        protected RecipeMap prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
            return null;
        }

        @Override
        protected void apply(RecipeMap recipeMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        }

        @Override
        public HolderLookup.Provider fabric_getRegistries() {
            return null;
        }
    }

}
