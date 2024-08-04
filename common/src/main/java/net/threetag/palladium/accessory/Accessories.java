package net.threetag.palladium.accessory;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.registry.DeferredRegister;

public class Accessories {

    public static final DeferredRegister<Accessory> ACCESSORIES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ACCESSORY);

    public static final Holder<Accessory> LUCRAFT_ARC_REACTOR = ACCESSORIES.register("lucraft_arc_reactor",
            () -> new OverlayAccessory("lucraft_arc_reactor").glowing().slot(AccessorySlot.SPECIAL).setExclusive());

    public static final Holder<Accessory> HEROBRINE_EYES = ACCESSORIES.register("herobrine_eyes",
            () -> new OverlayAccessory("herobrine_eyes").glowing().slot(AccessorySlot.FACE).setExclusive());

    public static final Holder<Accessory> FACE_MASK = ACCESSORIES.register("face_mask",
            () -> new OverlayAccessory("face_mask").slot(AccessorySlot.FACE));

    public static final Holder<Accessory> GLASSES_3D = ACCESSORIES.register("3d_glasses",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "glasses"), "3d_glasses").slot(AccessorySlot.FACE));

    public static final Holder<Accessory> SUN_GLASSES = ACCESSORIES.register("sun_glasses",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "glasses"), "sun_glasses").slot(AccessorySlot.FACE));

    public static final Holder<Accessory> HEART_GLASSES = ACCESSORIES.register("heart_glasses",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "glasses"), "heart_glasses").slot(AccessorySlot.FACE));

    public static final Holder<Accessory> OWCA_FEDORA = ACCESSORIES.register("owca_fedora",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "fedora"), "owca_fedora").slot(AccessorySlot.HAT).setExclusive());

    public static final Holder<Accessory> ELTON_HAT = ACCESSORIES.register("elton_hat",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "fedora"), "elton_hat").slot(AccessorySlot.HAT).setExclusive());

    public static final Holder<Accessory> STRAWHAT = ACCESSORIES.register("strawhat",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "strawhat"), ResourceLocation.withDefaultNamespace("textures/entity/villager/profession/farmer.png")).slot(AccessorySlot.HAT));

    public static final Holder<Accessory> FEZ = ACCESSORIES.register("fez",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "fez"), "fez").slot(AccessorySlot.HAT).setExclusive());

    public static final Holder<Accessory> ANTENNA = ACCESSORIES.register("antenna",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "antenna"), "antenna").slot(AccessorySlot.HAT));

    public static final Holder<Accessory> KRUSTY_KRAB_HAT = ACCESSORIES.register("krusty_krab_hat",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "krusty_krab_hat"), "krusty_krab_hat").slot(AccessorySlot.HAT).setExclusive());

    public static final Holder<Accessory> SEA_PICKLE_HAT = ACCESSORIES.register("sea_pickle_hat", SeaPickleHatAccessory::new);

    public static final Holder<Accessory> WINTER_SOLDIER_ARM = ACCESSORIES.register("winter_soldier_arm",
            () -> new OverlayAccessory("winter_soldier_arms", "winter_soldier_slim_arms").onlyRenderSlot().slot(AccessorySlot.MAIN_ARM, AccessorySlot.OFF_ARM).setExclusive());

    public static final Holder<Accessory> HYPERION_ARM = ACCESSORIES.register("hyperion_arm",
            () -> new OverlayAccessory("hyperion_arms", "hyperion_slim_arms").onlyRenderSlot().slot(AccessorySlot.MAIN_ARM, AccessorySlot.OFF_ARM).setExclusive());

    public static final Holder<Accessory> MECHANICAL_ARM = ACCESSORIES.register("mechanical_arm",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("player"), "mechanical_arms"), () -> new ModelLayerLocation(Palladium.id("player_slim"), "mechanical_arms"), "mechanical_arm", "mechanical_slim_arm")
                    .onlyRenderSlot().slot(AccessorySlot.MAIN_ARM, AccessorySlot.OFF_ARM).setExclusive());

    public static final Holder<Accessory> HAMMOND_CANE = ACCESSORIES.register("hammond_cane",
            () -> new HumanoidModelOverlayAccessory(() -> new ModelLayerLocation(Palladium.id("humanoid"), "hammond_cane"), "hammond_cane")
                    .handVisibilityFix().slot(AccessorySlot.MAIN_HAND, AccessorySlot.OFF_HAND).setExclusive());

    public static final Holder<Accessory> WOODEN_LEG = ACCESSORIES.register("wooden_leg", () -> new WoodenLegAccessory().setExclusive());

}
