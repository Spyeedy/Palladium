package net.threetag.palladium.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.loot.LootTableModificationManager;
import net.threetag.palladium.world.PalladiumPlacedFeatures;

public class PalladiumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Palladium.init();
//        ForgeConfigRegistry.INSTANCE.register(Palladium.MOD_ID, ModConfig.Type.CLIENT, PalladiumConfig.Client.generateConfig());
//        ForgeConfigRegistry.INSTANCE.register(Palladium.MOD_ID, ModConfig.Type.SERVER, PalladiumConfig.Server.generateConfig());

        registerEnergyHandlers();
        registerEvents();
        registerPlacedFeatures();
    }

    private static void registerEnergyHandlers() {

    }

    private static void registerEvents() {
        LootTableEvents.MODIFY.register((key, builder, source) -> {
            LootTableModificationManager.Modification mod = LootTableModificationManager.getInstance().getFor(key.location());

            if (mod != null) {
                builder.pools(mod.getLootPools());
            }
        });
    }

    private static void registerPlacedFeatures() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_LEAD_UPPER);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_LEAD_MIDDLE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_LEAD_SMALL);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.VEGETAL_DECORATION, PalladiumPlacedFeatures.UNDERGROUND_VIBRANIUM_METEORITE);
    }

}
