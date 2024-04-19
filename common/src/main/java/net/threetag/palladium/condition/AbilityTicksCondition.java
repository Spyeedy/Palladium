package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class AbilityTicksCondition extends Condition {

    private final AbilityReference ability;
    private final int min, max;

    public AbilityTicksCondition(AbilityReference ability, int min, int max) {
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

        AbilityInstance dependency = this.ability.getEntry(entity, holder);

        if (dependency == null) {
            return false;
        } else {
            return this.min <= dependency.getEnabledTicks() && dependency.getEnabledTicks() <= this.max;
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_TICKS.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of enabled ticks");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of enabled ticks");

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

            return new AbilityTicksCondition(abilityReference,
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability has been enabled for a certain amount of ticks.";
        }
    }
}
