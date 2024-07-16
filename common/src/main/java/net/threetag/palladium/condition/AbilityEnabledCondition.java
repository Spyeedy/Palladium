package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;

public class AbilityEnabledCondition extends Condition {

    private final AbilityReference ability;

    public AbilityEnabledCondition(AbilityReference ability) {
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
        return dependency != null && dependency.isEnabled();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_ENABLED.get();
    }

    public static class Serializer extends ConditionSerializer {
        public static final PalladiumProperty<ResourceLocation> POWER = new ResourceLocationProperty("power").configurable("ID of the power where is the desired ability is located. Can be null IF used for abilities, then it will look into the current power");
        public static final PalladiumProperty<String> ABILITY = new StringProperty("ability").configurable("ID of the desired ability");

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability is enabled. If the power is not null, it will look for the ability in the specified power. If the power is null, it will look for the ability in the current power.";
        }

        public Serializer() {
            this.withProperty(POWER, null);
            this.withProperty(ABILITY, "ability_id");
        }

        @Override
        public Condition make(JsonObject json) {
            AbilityReference abilityReference = AbilityReference.parse(this.getProperty(json, ABILITY));

            if (this.getProperty(json, POWER) != null) {
                abilityReference = new AbilityReference(this.getProperty(json, POWER), this.getProperty(json, ABILITY));
            }

            return new AbilityEnabledCondition(abilityReference);
        }

    }
}
