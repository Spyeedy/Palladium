package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.context.DataContext;

public class AbilityOnCooldownCondition extends Condition {

    private final AbilityReference ability;

    public AbilityOnCooldownCondition(AbilityReference ability) {
        this.ability = ability;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityInstance dependency = this.ability.getInstance(entity, holder);
        return dependency != null && dependency.isOnCooldown();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_ON_COOLDOWN.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability is currently on cooldown. If the power is not null, it will look for the ability in the specified power. If the power is null, it will look for the ability in the current power.";
        }

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
        }

        @Override
        public Condition make(JsonObject json) {
            AbilityReference abilityReference = AbilityReference.parse(this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));

            if (this.getProperty(json, AbilityEnabledCondition.Serializer.POWER) != null) {
                abilityReference = new AbilityReference(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER), this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));
            }

            return new AbilityOnCooldownCondition(abilityReference);
        }

    }
}
