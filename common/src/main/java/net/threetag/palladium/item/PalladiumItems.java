package net.threetag.palladium.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.energy.EnergyHelper;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistryHolder;

public class PalladiumItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Palladium.MOD_ID, Registries.ITEM);

    public static final RegistryHolder<Item, Item> LEAD_ORE = ITEMS.register("lead_ore", () -> new BlockItem(PalladiumBlocks.LEAD_ORE.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> DEEPSLATE_LEAD_ORE = ITEMS.register("deepslate_lead_ore", () -> new BlockItem(PalladiumBlocks.DEEPSLATE_LEAD_ORE.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> TITANIUM_ORE = ITEMS.register("titanium_ore", () -> new BlockItem(PalladiumBlocks.TITANIUM_ORE.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> VIBRANIUM_ORE = ITEMS.register("vibranium_ore", () -> new BlockItem(PalladiumBlocks.VIBRANIUM_ORE.value(), new Item.Properties()));

    public static final RegistryHolder<Item, Item> REDSTONE_FLUX_CRYSTAL_GEODE = ITEMS.register("redstone_flux_crystal_geode", () -> new BlockItem(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE = ITEMS.register("deepslate_redstone_flux_crystal_geode", () -> new BlockItem(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> SMALL_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("small_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("medium_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> LARGE_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("large_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> REDSTONE_FLUX_CRYSTAL_CLUSTER = ITEMS.register("redstone_flux_crystal_cluster", () -> new BlockItem(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER.value(), new Item.Properties()));

    public static final RegistryHolder<Item, Item> LEAD_BLOCK = ITEMS.register("lead_block", () -> new BlockItem(PalladiumBlocks.LEAD_BLOCK.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> VIBRANIUM_BLOCK = ITEMS.register("vibranium_block", () -> new BlockItem(PalladiumBlocks.VIBRANIUM_BLOCK.value(), new Item.Properties()));

    public static final RegistryHolder<Item, Item> RAW_LEAD_BLOCK = ITEMS.register("raw_lead_block", () -> new BlockItem(PalladiumBlocks.RAW_LEAD_BLOCK.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> RAW_TITANIUM_BLOCK = ITEMS.register("raw_titanium_block", () -> new BlockItem(PalladiumBlocks.RAW_TITANIUM_BLOCK.value(), new Item.Properties()));
    public static final RegistryHolder<Item, Item> RAW_VIBRANIUM_BLOCK = ITEMS.register("raw_vibranium_block", () -> new BlockItem(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.value(), new Item.Properties()));

    public static final RegistryHolder<Item, Item> HEART_SHAPED_HERB = ITEMS.register("heart_shaped_herb", () -> new BlockItem(PalladiumBlocks.HEART_SHAPED_HERB.value(), new Item.Properties()));

    // -----------------------------------------------------------------------------------------------------------------

    public static final RegistryHolder<Item, Item> RAW_LEAD = ITEMS.register("raw_lead", () -> new Item(new Item.Properties()));
    public static final RegistryHolder<Item, Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryHolder<Item, Item> RAW_TITANIUM = ITEMS.register("raw_titanium", () -> new Item(new Item.Properties()));
    public static final RegistryHolder<Item, Item> RAW_VIBRANIUM = ITEMS.register("raw_vibranium", () -> new Item(new Item.Properties()));
    public static final RegistryHolder<Item, Item> VIBRANIUM_INGOT = ITEMS.register("vibranium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryHolder<Item, Item> REDSTONE_FLUX_CRYSTAL = ITEMS.register("redstone_flux_crystal", () -> new Item(new Item.Properties()));

    public static final RegistryHolder<Item, Item> SUIT_STAND = ITEMS.register("suit_stand", () -> new SuitStandItem(new Item.Properties().stacksTo(16)));
    public static final RegistryHolder<Item, Item> LEAD_CIRCUIT = ITEMS.register("lead_circuit", () -> new Item(new Item.Properties()));
    public static final RegistryHolder<Item, Item> QUARTZ_CIRCUIT = ITEMS.register("quartz_circuit", () -> new Item(new Item.Properties()));
    public static final RegistryHolder<Item, Item> VIBRANIUM_CIRCUIT = ITEMS.register("vibranium_circuit", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryHolder<Item, EnergyItem> LEAD_FLUX_CAPACITOR = ITEMS.register("lead_flux_capacitor", () -> new EnergyItem(new Item.Properties().stacksTo(1).component(EnergyHelper.getItemComponent(), 500000), 500000, 1000, 1000));
    public static final RegistryHolder<Item, EnergyItem> QUARTZ_FLUX_CAPACITOR = ITEMS.register("quartz_flux_capacitor", () -> new EnergyItem(new Item.Properties().stacksTo(1).component(EnergyHelper.getItemComponent(), 1000000), 1000000, 5000, 5000));
    public static final RegistryHolder<Item, EnergyItem> VIBRANIUM_FLUX_CAPACITOR = ITEMS.register("vibranium_flux_capacitor", () -> new EnergyItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).component(EnergyHelper.getItemComponent(), 2000000), 2000000, 10000, 10000));

    public static final RegistryHolder<Item, VibraniumWeaveArmorItem> VIBRANIUM_WEAVE_BOOTS = ITEMS.register("vibranium_weave_boots", () -> new VibraniumWeaveArmorItem(PalladiumArmorMaterials.VIBRANIUM_WEAVE, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static void init() {
        CreativeModeTabRegistry.addToTab(PalladiumCreativeModeTabs.PALLADIUM_MODS.value(), entries -> {
            entries.add(
                    LEAD_ORE.value(),
                    DEEPSLATE_LEAD_ORE.value(),
                    TITANIUM_ORE.value(),
                    VIBRANIUM_ORE.value(),
                    REDSTONE_FLUX_CRYSTAL_GEODE.value(),
                    DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.value(),
                    SMALL_REDSTONE_FLUX_CRYSTAL_BUD.value(),
                    MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.value(),
                    LARGE_REDSTONE_FLUX_CRYSTAL_BUD.value(),
                    REDSTONE_FLUX_CRYSTAL_CLUSTER.value(),
                    LEAD_BLOCK.value(),
                    VIBRANIUM_BLOCK.value(),
                    RAW_LEAD_BLOCK.value(),
                    RAW_TITANIUM_BLOCK.value(),
                    RAW_VIBRANIUM_BLOCK.value(),
                    HEART_SHAPED_HERB.value(),
                    RAW_LEAD.value(),
                    LEAD_INGOT.value(),
                    RAW_TITANIUM.value(),
                    RAW_VIBRANIUM.value(),
                    VIBRANIUM_INGOT.value(),
                    REDSTONE_FLUX_CRYSTAL.value(),
                    SUIT_STAND.value(),
                    LEAD_CIRCUIT.value(),
                    QUARTZ_CIRCUIT.value(),
                    VIBRANIUM_CIRCUIT.value(),
                    LEAD_FLUX_CAPACITOR.value(),
                    LEAD_BLOCK.value(),
                    QUARTZ_FLUX_CAPACITOR.value(),
                    VIBRANIUM_FLUX_CAPACITOR.value(),
                    VIBRANIUM_WEAVE_BOOTS.value()
            );
        });

        CreativeModeTabRegistry.addToTab(PalladiumCreativeModeTabs.TECHNOLOGY.value(), entries -> {
            entries.add(LEAD_CIRCUIT.value());
            entries.add(QUARTZ_CIRCUIT.value());
            entries.add(VIBRANIUM_CIRCUIT.value());
            entries.add(LEAD_FLUX_CAPACITOR.value());
            entries.add(LEAD_FLUX_CAPACITOR.get().getFullyChargedInstance());
            entries.add(QUARTZ_FLUX_CAPACITOR.value());
            entries.add(QUARTZ_FLUX_CAPACITOR.get().getFullyChargedInstance());
            entries.add(VIBRANIUM_FLUX_CAPACITOR.value());
            entries.add(VIBRANIUM_FLUX_CAPACITOR.get().getFullyChargedInstance());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.FUNCTIONAL_BLOCKS), entries -> {
            entries.addAfter(Items.ARMOR_STAND, SUIT_STAND.value());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.INGREDIENTS), entries -> {
            entries.addAfter(Items.COPPER_INGOT, RAW_LEAD.value(), LEAD_INGOT.value());
            entries.addAfter(Items.GOLD_INGOT, RAW_TITANIUM.value());
            entries.addAfter(Items.NETHERITE_INGOT, RAW_VIBRANIUM.value(), VIBRANIUM_INGOT.value());
            entries.addAfter(Items.REDSTONE, REDSTONE_FLUX_CRYSTAL.value());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.BUILDING_BLOCKS), entries -> {
            entries.addAfter(Items.COPPER_BLOCK, LEAD_BLOCK.value());
            entries.addAfter(Items.NETHERITE_BLOCK, VIBRANIUM_BLOCK.value());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.NATURAL_BLOCKS), entries -> {
            entries.addAfter(Items.DEEPSLATE_COPPER_ORE, LEAD_ORE.value(), DEEPSLATE_LEAD_ORE.value(), TITANIUM_ORE.value(), VIBRANIUM_ORE.value());
            entries.addAfter(Items.DEEPSLATE_REDSTONE_ORE, REDSTONE_FLUX_CRYSTAL_GEODE.value(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.value());
            entries.addAfter(Items.AMETHYST_CLUSTER, SMALL_REDSTONE_FLUX_CRYSTAL_BUD.value(), MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.value(), LARGE_REDSTONE_FLUX_CRYSTAL_BUD.value(), REDSTONE_FLUX_CRYSTAL_CLUSTER.value());
            entries.addAfter(Items.RAW_GOLD_BLOCK, RAW_LEAD_BLOCK.value(), RAW_TITANIUM_BLOCK.value(), RAW_VIBRANIUM_BLOCK.value());
            entries.addAfter(Items.WITHER_ROSE, HEART_SHAPED_HERB.value());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.COMBAT), entries -> {
            entries.addAfter(Items.LEATHER_BOOTS, VIBRANIUM_WEAVE_BOOTS.value());
        });
    }
}
