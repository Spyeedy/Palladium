package net.threetag.palladium.condition;

import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class ConditionSerializers {

    public static final DeferredRegister<ConditionSerializer> CONDITION_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, ConditionSerializer.REGISTRY);

    public static final RegistrySupplier<ConditionSerializer> NOT = CONDITION_SERIALIZERS.register("not", NotCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> OR = CONDITION_SERIALIZERS.register("or", OrCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> AND = CONDITION_SERIALIZERS.register("and", AndCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> HAS_POWER = CONDITION_SERIALIZERS.register("has_power", HasPowerCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> WEARS_SUIT_SET = CONDITION_SERIALIZERS.register("wears_suit_set", WearsSuitSetCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_UNLOCKED = CONDITION_SERIALIZERS.register("ability_unlocked", AbilityUnlockedCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_TYPE_UNLOCKED = CONDITION_SERIALIZERS.register("ability_type_unlocked", AbilityTypeUnlockedCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_ENABLED = CONDITION_SERIALIZERS.register("ability_enabled", AbilityEnabledCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_TYPE_ENABLED = CONDITION_SERIALIZERS.register("ability_type_enabled", AbilityTypeEnabledCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ACTION = CONDITION_SERIALIZERS.register("action", ActionCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ACTIVATION = CONDITION_SERIALIZERS.register("activation", ActivationCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> TOGGLE = CONDITION_SERIALIZERS.register("toggle", ToggleCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> HELD = CONDITION_SERIALIZERS.register("held", HeldCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> HEALTH = CONDITION_SERIALIZERS.register("health", HealthCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> OBJECTIVE_SCORE = CONDITION_SERIALIZERS.register("objective_score", ObjectiveScoreCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> HAS_TAG = CONDITION_SERIALIZERS.register("has_tag", HasTagCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_INTEGER_PROPERTY = CONDITION_SERIALIZERS.register("ability_integer_property", AbilityIntegerPropertyCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_TICKS = CONDITION_SERIALIZERS.register("ability_ticks", AbilityTicksCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> CROUCHING = CONDITION_SERIALIZERS.register("crouching", CrouchingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> POSE = CONDITION_SERIALIZERS.register("pose", PoseCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> MOON_PHASE = CONDITION_SERIALIZERS.register("moon_phase", MoonPhaseCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> MOD_LOADED = CONDITION_SERIALIZERS.register("mod_loaded", ModLoadedCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ITEM_IN_SLOT = CONDITION_SERIALIZERS.register("item_in_slot", ItemInSlotCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ENTITY_TYPE = CONDITION_SERIALIZERS.register("entity_type", EntityTypeCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ENTITY_TYPE_TAG = CONDITION_SERIALIZERS.register("entity_type_tag", EntityTypeTagCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> BRIGHTNESS_AT_POSITION = CONDITION_SERIALIZERS.register("brightness_at_position", BrightnessAtPositionCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ON_GROUND = CONDITION_SERIALIZERS.register("on_ground", OnGroundCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> UNDER_WATER = CONDITION_SERIALIZERS.register("under_water", UnderWaterCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_MOVING = CONDITION_SERIALIZERS.register("is_moving", IsMovingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ITEM_BUYABLE = CONDITION_SERIALIZERS.register("item_buyable", ItemBuyableCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> EXPERIENCE_LEVEL_BUYABLE = CONDITION_SERIALIZERS.register("experience_level_buyable", ExperienceLevelBuyableCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> SCOREBOARD_SCORE_BUYABLE = CONDITION_SERIALIZERS.register("scoreboard_score_buyable", ScoreboardScoreBuyableCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> COMMAND_RESULT = CONDITION_SERIALIZERS.register("command_result", CommandResultCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> INTERVAL = CONDITION_SERIALIZERS.register("interval", IntervalCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> SPRINTING = CONDITION_SERIALIZERS.register("sprinting", SprintingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> INTEGER_PROPERTY = CONDITION_SERIALIZERS.register("integer_property", IntegerPropertyCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> FLOAT_PROPERTY = CONDITION_SERIALIZERS.register("float_property", FloatPropertyCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> DIMENSION = CONDITION_SERIALIZERS.register("dimension", DimensionCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_ON_FIRE = CONDITION_SERIALIZERS.register("is_on_fire", IsOnFireCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_SWIMMING = CONDITION_SERIALIZERS.register("is_swimming", IsSwimmingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_ELYTRA_FLYING = CONDITION_SERIALIZERS.register("is_elytra_flying", IsElytraFlyingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_FLYING = CONDITION_SERIALIZERS.register("is_flying", IsFlyingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_LEVITATING = CONDITION_SERIALIZERS.register("is_levitating", IsLevitatingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_FAST_FLYING = CONDITION_SERIALIZERS.register("is_fast_flying", IsFastFlyingCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IN_WATER = CONDITION_SERIALIZERS.register("in_water", InWaterCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IN_LAVA = CONDITION_SERIALIZERS.register("in_lava", InLavaCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_UNDER_WATER = CONDITION_SERIALIZERS.register("is_under_water", IsUnderWaterCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_IN_RAIN = CONDITION_SERIALIZERS.register("is_in_rain", IsInRainCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_IN_WATER_OR_RAIN = CONDITION_SERIALIZERS.register("is_in_water_or_rain", IsInWaterOrRainCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_IN_WATER_RAIN_OR_BUBBLE = CONDITION_SERIALIZERS.register("is_in_water_rain_or_bubble", IsInWaterRainOrBubbleCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> IS_IN_WATER_OR_BUBBLE = CONDITION_SERIALIZERS.register("is_in_water_or_bubble", IsInWaterOrBubbleCondition.Serializer::new);

}
