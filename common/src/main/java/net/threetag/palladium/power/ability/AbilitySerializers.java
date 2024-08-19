package net.threetag.palladium.power.ability;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistryHolder;

public class AbilitySerializers {

    public static final DeferredRegister<AbilitySerializer<?>> ABILITIES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ABILITY_SERIALIZER);

    public static final RegistryHolder<AbilitySerializer<?>, DummyAbility.Serializer> DUMMY = ABILITIES.register("dummy", DummyAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, CommandAbility.Serializer> COMMAND = ABILITIES.register("command", CommandAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, RenderLayerAbility.Serializer> RENDER_LAYER = ABILITIES.register("render_layer", RenderLayerAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, RenderLayerByAccessorySlotAbility.Serializer> RENDER_LAYER_BY_ACCESSORY_SLOT = ABILITIES.register("render_layer_by_accessory_slot", RenderLayerByAccessorySlotAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, ShrinkBodyOverlayAbility.Serializer> SHRINK_BODY_OVERLAY = ABILITIES.register("shrink_body_overlay", ShrinkBodyOverlayAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, AttributeModifierAbility.Serializer> ATTRIBUTE_MODIFIER = ABILITIES.register("attribute_modifier", AttributeModifierAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, HealingAbility.Serializer> HEALING = ABILITIES.register("healing", HealingAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, SlowfallAbility.Serializer> SLOWFALL = ABILITIES.register("slowfall", SlowfallAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, DamageImmunityAbility.Serializer> DAMAGE_IMMUNITY = ABILITIES.register("damage_immunity", DamageImmunityAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, InvisibilityAbility.Serializer> INVISIBILITY = ABILITIES.register("invisibility", InvisibilityAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, EnergyBeamAbility.Serializer> ENERGY_BEAM = ABILITIES.register("energy_beam", EnergyBeamAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, SizeAbility.Serializer> SIZE = ABILITIES.register("size", SizeAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, ProjectileAbility.Serializer> PROJECTILE = ABILITIES.register("projectile", ProjectileAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, SkinChangeAbility.Serializer> SKIN_CHANGE = ABILITIES.register("skin_change", SkinChangeAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, AimAbility.Serializer> AIM = ABILITIES.register("aim", AimAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, HideBodyPartAbility.Serializer> HIDE_BODY_PART = ABILITIES.register("hide_body_part", HideBodyPartAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, RemoveBodyPartAbility.Serializer> REMOVE_BODY_PART = ABILITIES.register("remove_body_part", RemoveBodyPartAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, ShaderEffectAbility.Serializer> SHADER_EFFECT = ABILITIES.register("shader_effect", ShaderEffectAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, GuiOverlayAbility.Serializer> GUI_OVERLAY = ABILITIES.register("gui_overlay", GuiOverlayAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, ShowBothArmsAbility.Serializer> SHOW_BOTH_ARMS = ABILITIES.register("show_both_arms", ShowBothArmsAbility.Serializer::new);
    //    public static final RegistryHolder<AbilitySerializer<?>, PlayerAnimationAbility.Serializer> PLAYER_ANIMATION = ABILITIES.register("player_animation", PlayerAnimationAbility::new);
    public static final RegistryHolder<AbilitySerializer<?>, WaterWalkAbility.Serializer> WATER_WALK = ABILITIES.register("water_walk", WaterWalkAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, FluidWalkingAbility.Serializer> FLUID_WALKING = ABILITIES.register("fluid_walking", FluidWalkingAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, RestrictSlotsAbility.Serializer> RESTRICT_SLOTS = ABILITIES.register("restrict_slots", RestrictSlotsAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, PlaySoundAbility.Serializer> PLAY_SOUND = ABILITIES.register("play_sound", PlaySoundAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, VibrateAbility.Serializer> VIBRATE = ABILITIES.register("vibrate", VibrateAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, IntangibilityAbility.Serializer> INTANGIBILITY = ABILITIES.register("intangibility", IntangibilityAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, NameChangeAbility.Serializer> NAME_CHANGE = ABILITIES.register("name_change", NameChangeAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, SculkImmunityAbility.Serializer> SCULK_IMMUNITY = ABILITIES.register("sculk_immunity", SculkImmunityAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, TrailAbility.Serializer> TRAIL = ABILITIES.register("trail", TrailAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, FireAspectAbility.Serializer> FIRE_ASPECT = ABILITIES.register("fire_aspect", FireAspectAbility.Serializer::new);
    public static final RegistryHolder<AbilitySerializer<?>, ParticleAbility.Serializer> PARTICLES = ABILITIES.register("particles", ParticleAbility.Serializer::new);

    public static void init() {

    }

}
