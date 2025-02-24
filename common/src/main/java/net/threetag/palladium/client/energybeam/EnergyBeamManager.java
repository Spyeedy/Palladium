package net.threetag.palladium.client.energybeam;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EnergyBeamManager extends SimpleJsonResourceReloadListener<EnergyBeamConfiguration> {

    public static EnergyBeamManager INSTANCE = new EnergyBeamManager();

    public Map<ResourceLocation, EnergyBeamConfiguration> byName = ImmutableMap.of();

    public EnergyBeamManager() {
        super(EnergyBeamConfiguration.CODEC, FileToIdConverter.json("palladium/energy_beams"));
    }

    @Override
    protected void apply(Map<ResourceLocation, EnergyBeamConfiguration> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, EnergyBeamConfiguration> builder = ImmutableMap.builder();
        objects.forEach(builder::put);
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} energy beams", this.byName.size());
    }

    @Nullable
    public EnergyBeamConfiguration get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
