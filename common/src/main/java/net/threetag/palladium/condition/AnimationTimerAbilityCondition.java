package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.AnimationTimer;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class AnimationTimerAbilityCondition extends Condition {

    private final AbilityReference ability;
    private final int min, max;

    public AnimationTimerAbilityCondition(AbilityReference ability, int min, int max) {
        this.ability = ability;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityEntry dependency = this.ability.getEntry(entity, holder);

        if (dependency == null || !(dependency.getConfiguration().getAbility() instanceof AnimationTimer animationTimer)) {
            return false;
        }

        var timer = (int) animationTimer.getAnimationTimer(dependency, 1F);
        return timer >= this.min && timer <= this.max;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ANIMATION_TIMER_ABILITY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the timer value");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the timer value");

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            AbilityReference abilityReference = AbilityReference.fromString(this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));

            if (this.getProperty(json, AbilityEnabledCondition.Serializer.POWER) != null) {
                abilityReference = new AbilityReference(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER), this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));
            }

            return new AnimationTimerAbilityCondition(abilityReference,
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the given animation timer ability has a certain value. This condition is a simplified version of the ability_integer_property condition designed to be used for animation timer abilities.";
        }
    }
}
