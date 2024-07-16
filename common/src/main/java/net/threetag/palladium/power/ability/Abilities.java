package net.threetag.palladium.power.ability;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.icon.TexturedIcon;
import net.threetag.palladiumcore.registry.DeferredRegister;

public class Abilities {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(Palladium.MOD_ID, Ability.REGISTRY);

    public static final Holder<Ability> DUMMY = ABILITIES.register("dummy", () -> new Ability().setDocumentationDescription("Does nothing."));
    public static final Holder<Ability> COMMAND = ABILITIES.register("command", CommandAbility::new);
    public static final Holder<Ability> RENDER_LAYER = ABILITIES.register("render_layer", RenderLayerAbility::new);
    public static final Holder<Ability> RENDER_LAYER_BY_ACCESSORY_SLOT = ABILITIES.register("render_layer_by_accessory_slot", RenderLayerByAccessorySlotAbility::new);
    public static final Holder<Ability> ANIMATION_TIMER = ABILITIES.register("animation_timer", AnimationTimerAbility::new);
    public static final Holder<Ability> REPEATING_ANIMATION_TIMER = ABILITIES.register("repeating_animation_timer", RepeatingAnimationTimerAbility::new);
    public static final Holder<Ability> SHRINK_BODY_OVERLAY = ABILITIES.register("shrink_body_overlay", ShrinkBodyOverlayAbility::new);
    public static final Holder<Ability> ATTRIBUTE_MODIFIER = ABILITIES.register("attribute_modifier", AttributeModifierAbility::new);
    public static final Holder<Ability> HEALING = ABILITIES.register("healing", HealingAbility::new);
    public static final Holder<Ability> SLOWFALL = ABILITIES.register("slowfall", SlowfallAbility::new);
    public static final Holder<Ability> DAMAGE_IMMUNITY = ABILITIES.register("damage_immunity", DamageImmunityAbility::new);
    public static final Holder<Ability> INVISIBILITY = ABILITIES.register("invisibility", () -> new Ability().withProperty(Ability.ICON, new TexturedIcon(ResourceLocation.fromNamespaceAndPath(Palladium.MOD_ID, "textures/icon/invisibility.png"))).setDocumentationDescription("Makes the player invisible. Also makes mobs not see the player anymore."));
    @Deprecated
    public static final Holder<Ability> ENERGY_BLAST = ABILITIES.register("energy_blast", EnergyBlastAbility::new);
    public static final Holder<Ability> ENERGY_BEAM = ABILITIES.register("energy_beam", EnergyBeamAbility::new);
    public static final Holder<Ability> SIZE = ABILITIES.register("size", SizeAbility::new);
    public static final Holder<Ability> PROJECTILE = ABILITIES.register("projectile", ProjectileAbility::new);
    public static final Holder<Ability> SKIN_CHANGE = ABILITIES.register("skin_change", SkinChangeAbility::new);
    public static final Holder<Ability> AIM = ABILITIES.register("aim", AimAbility::new);
    public static final Holder<Ability> HIDE_BODY_PART = ABILITIES.register("hide_body_part", HideBodyPartAbility::new);
    public static final Holder<Ability> REMOVE_BODY_PART = ABILITIES.register("remove_body_part", RemoveBodyPartAbility::new);
    public static final Holder<Ability> SHADER_EFFECT = ABILITIES.register("shader_effect", ShaderEffectAbility::new);
    public static final Holder<Ability> GUI_OVERLAY = ABILITIES.register("gui_overlay", GuiOverlayAbility::new);
    public static final Holder<Ability> SHOW_BOTH_ARMS = ABILITIES.register("show_both_arms", () -> new Ability().withProperty(Ability.HIDDEN_IN_GUI, true).setDocumentationDescription("Enables the rendering of your off-hand."));
    public static final Holder<Ability> PLAYER_ANIMATION = ABILITIES.register("player_animation", PlayerAnimationAbility::new);
    public static final Holder<Ability> WATER_WALK = ABILITIES.register("water_walk", () -> new Ability().setDocumentationDescription("Allows the player to walk on water."));
    public static final Holder<Ability> FLUID_WALKING = ABILITIES.register("fluid_walking", FluidWalkingAbility::new);
    public static final Holder<Ability> RESTRICT_SLOTS = ABILITIES.register("restrict_slots", RestrictSlotsAbility::new);
    public static final Holder<Ability> PLAY_SOUND = ABILITIES.register("play_sound", PlaySoundAbility::new);
    public static final Holder<Ability> VIBRATE = ABILITIES.register("vibrate", VibrateAbility::new);
    public static final Holder<Ability> INTANGIBILITY = ABILITIES.register("intangibility", IntangibilityAbility::new);
    public static final Holder<Ability> NAME_CHANGE = ABILITIES.register("name_change", NameChangeAbility::new);
    public static final Holder<Ability> SCULK_IMMUNITY = ABILITIES.register("sculk_immunity", () -> new Ability().withProperty(Ability.ICON, new ItemIcon(Items.SCULK)).setDocumentationDescription("When enabled, the player will not cause any walk-related sculk vibrations anymore."));
    public static final Holder<Ability> TRAIL = ABILITIES.register("trail", TrailAbility::new);
    public static final Holder<Ability> FIRE_ASPECT = ABILITIES.register("fire_aspect", FireAspectAbility::new);
    public static final Holder<Ability> PARTICLES = ABILITIES.register("particles", ParticleAbility::new);

    public static void init() {

    }

}
