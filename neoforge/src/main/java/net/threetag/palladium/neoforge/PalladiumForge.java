package net.threetag.palladium.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.data.neoforge.*;
import net.threetag.palladium.energy.neoforge.EnergyHelperImpl;
import net.threetag.palladium.mixin.ReloadableResourceManagerMixin;
import net.threetag.palladiumcore.util.Platform;

import java.util.List;

@Mod(Palladium.MOD_ID)
@EventBusSubscriber(modid = Palladium.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PalladiumForge {

    public PalladiumForge(IEventBus modBus) {
        Palladium.init();
        EnergyHelperImpl.COMPONENTS.register(modBus);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PalladiumConfig.Client.generateConfig());
//        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PalladiumConfig.Server.generateConfig());

        if (Platform.isClient()) {
            PalladiumClient.init();
        }
    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent e) {
        AddonPackManager.waitForLoading();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onConstructMod(FMLConstructModEvent event) {
        event.enqueueWork(AddonPackManager::startLoading);
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent e) {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(PalladiumBlocks.HEART_SHAPED_HERB.getId(), PalladiumBlocks.POTTED_HEART_SHAPED_HERB);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        Palladium.generateDocumentation();
        var output = e.getGenerator().getPackOutput();

        PalladiumBlockTagsProvider blockTagsProvider = new PalladiumBlockTagsProvider(output, e.getLookupProvider(), e.getExistingFileHelper());
        e.getGenerator().addProvider(e.includeServer(), blockTagsProvider);
        e.getGenerator().addProvider(e.includeServer(), new PalladiumItemTagsProvider(output, e.getLookupProvider(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeServer(), new PalladiumRecipeProvider(output, e.getLookupProvider()));
        e.getGenerator().addProvider(e.includeServer(), new PalladiumLootTableProvider(output, e.getLookupProvider()));
        e.getGenerator().addProvider(e.includeServer(), new PalladiumWorldGenProvider(output, e.getLookupProvider()));

        e.getGenerator().addProvider(e.includeClient(), new PalladiumBlockStateProvider(output, e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumItemModelProvider(output, e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumSoundDefinitionsProvider(output, e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.English(output));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.German(output));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.Saxon(output));
    }

    @SubscribeEvent
    public static void packFinder(AddPackFindersEvent e) {
        if (e.getPackType() != AddonPackManager.getPackType()) {
            e.addRepositorySource(AddonPackManager.getInstance().getWrappedPackFinder());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        Minecraft mc = Minecraft.getInstance();
        List<PreparableReloadListener> listeners = ((ReloadableResourceManagerMixin) mc.getResourceManager()).getListeners();
        int idx = listeners.indexOf(mc.getEntityModels());
        listeners.add(idx + 1, new ModelLayerManager());
    }
}
