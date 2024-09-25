package net.threetag.palladium.power;

import com.google.gson.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuitSetPowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static SuitSetPowerManager INSTANCE;

    private final Map<SuitSet, List<ResourceLocation>> suitSetPowers = new HashMap<>();
    private final HolderLookup.Provider registries;

    public static void init() {
        ReloadListenerRegistry.registerServerListener(Palladium.id("suit_set_powers"), SuitSetPowerManager::new);
    }

    public SuitSetPowerManager(HolderLookup.Provider registries) {
        super(GSON, "palladium/suit_set_powers");
        this.registries = registries;
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.suitSetPowers.clear();
        object.forEach((id, json) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(json, "$");

                List<ResourceLocation> powers = new ArrayList<>();
                GsonUtil.forEachInListOrPrimitive(jsonObject.get("power"), js -> powers.add(GsonUtil.convertToResourceLocation(js, "power[]")));

                final List<SuitSet> suitSets = new ArrayList<>();
                GsonUtil.forEachInListOrPrimitive(jsonObject.get("suit_set"), js -> {
                    ResourceLocation suitSetId = GsonUtil.convertToResourceLocation(js, "suit_set[]");

                    if (!PalladiumRegistries.SUIT_SET.containsKey(suitSetId)) {
                        throw new JsonParseException("Unknown suit set '" + suitSetId + "'");
                    }

                    suitSets.add(PalladiumRegistries.SUIT_SET.get(suitSetId));
                });

                for (SuitSet suitSet : suitSets) {
                    this.suitSetPowers.computeIfAbsent(suitSet, suitSet1 -> new ArrayList<>()).addAll(powers);
                }
            } catch (Exception exception) {
                AddonPackLog.error("Parsing error loading suit set powers {}", id, exception);
            }
        });
        AddonPackLog.info("Loaded {} suit set powers", this.suitSetPowers.size());
    }

    @Nullable
    public List<ResourceLocation> getPowerForSuitSet(SuitSet suitSet) {
        return this.suitSetPowers.get(suitSet);
    }

    public static SuitSetPowerManager getInstance() {
        return INSTANCE;
    }
}
