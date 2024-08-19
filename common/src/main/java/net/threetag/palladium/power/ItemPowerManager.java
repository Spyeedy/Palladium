package net.threetag.palladium.power;

import com.google.gson.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static ItemPowerManager INSTANCE;

    private final Map<String, Map<Item, List<ResourceLocation>>> itemPowers = new HashMap<>();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("item_powers"), INSTANCE = new ItemPowerManager());
    }

    public ItemPowerManager() {
        super(GSON, "palladium/item_powers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.itemPowers.clear();
        object.forEach((id, json) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(json, "$");
                String slot = GsonHelper.getAsString(jsonObject, "slot");

                List<ResourceLocation> powers = new ArrayList<>();
                GsonUtil.forEachInListOrPrimitive(jsonObject.get("power"), js -> powers.add(GsonUtil.convertToResourceLocation(js, "power[]")));

                final List<Item> items = new ArrayList<>();
                GsonUtil.forEachInListOrPrimitive(jsonObject.get("power"), js -> {
                    ResourceLocation itemId = GsonUtil.convertToResourceLocation(js, "item[]");

                    if (!BuiltInRegistries.ITEM.containsKey(itemId)) {
                        throw new JsonParseException("Unknown item '" + itemId + "'");
                    }

                    items.add(BuiltInRegistries.ITEM.get(itemId));
                });

                for (Item item : items) {
                    this.itemPowers.computeIfAbsent(slot, s -> new HashMap<>()).computeIfAbsent(item, item1 -> new ArrayList<>()).addAll(powers);
                }
            } catch (Exception exception) {
                AddonPackLog.error("Parsing error loading item powers {}", id, exception);
            }
        });
        AddonPackLog.info("Loaded {} item powers", this.itemPowers.size());
    }

    @Nullable
    public List<ResourceLocation> getPowerForItem(String slot, Item item) {
        return this.itemPowers.containsKey(slot) ? this.itemPowers.get(slot).get(item) : null;
    }

    public static ItemPowerManager getInstance() {
        return INSTANCE;
    }
}
