package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

public class AbilityIntegerPropertyCondition extends Condition {

    private final AbilityReference ability;
    private final String propertyKey;
    private final int min, max;

    public AbilityIntegerPropertyCondition(AbilityReference ability, String propertyKey, int min, int max) {
        this.ability = ability;
        this.propertyKey = propertyKey;
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
        }

        PalladiumProperty<?> property = dependency.getEitherPropertyByKey(this.propertyKey);

        if (property instanceof IntegerProperty integerProperty) {
            int value = dependency.getProperty(integerProperty);
            return value >= this.min && value <= this.max;
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_INTEGER_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("Name of the integer property in the ability. For animation-timer abilities it's 'value'");
        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the property value");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the property value");

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
            this.withProperty(PROPERTY, "value");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            AbilityReference abilityReference = AbilityReference.fromString(this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));

            if (this.getProperty(json, AbilityEnabledCondition.Serializer.POWER) != null) {
                abilityReference = new AbilityReference(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER), this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));
            }

            return new AbilityIntegerPropertyCondition(abilityReference,
                    this.getProperty(json, PROPERTY),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the given ability has a certain integer property value.";
        }
    }
}
