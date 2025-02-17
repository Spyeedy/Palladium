package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.ConditionArrayProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class XorCondition extends Condition {

    public final Condition[] conditions;

    public XorCondition(Condition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean active(DataContext context) {
        int found = 0;

        for (Condition condition : this.conditions) {
            if (condition.active(context)) {
                found++;

                if (found > 1) {
                    return false;
                }
            }
        }

        return found == 1;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.XOR.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Condition[]> CONDITIONS = new ConditionArrayProperty("conditions").configurable("Array of conditions, one of which must be active");

        public Serializer() {
            this.withProperty(CONDITIONS, new Condition[0]);
        }

        @Override
        public Condition make(JsonObject json) {
            return new XorCondition(this.getProperty(json, CONDITIONS));
        }

        @Override
        public String getDocumentationDescription() {
            return "A condition that is active if one of the conditions in the array is active.";
        }
    }
}
