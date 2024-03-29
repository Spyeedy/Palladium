package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.SizeUtil;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class SizeCondition extends Condition {

    private final float min, max;

    public SizeCondition(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(DataContext context) {
        Entity entity = context.getEntity();

        if (entity == null) {
            return false;
        }

        var averageScale = (SizeUtil.getInstance().getWidthScale(entity) + SizeUtil.getInstance().getHeightScale(entity)) / 2F;
        return averageScale >= this.min && averageScale <= this.max;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.SIZE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Float> MIN = new FloatProperty("min").configurable("Minimum required average size");
        public static final PalladiumProperty<Float> MAX = new FloatProperty("max").configurable("Minimum required average size");

        public Serializer() {
            this.withProperty(MIN, 0F);
            this.withProperty(MAX, Float.MAX_VALUE);
        }

        @Override
        public Condition make(JsonObject json) {
            return new SizeCondition(
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if an entity is within a certain size (requires Pehkui for real effect). It checks for the \"average \" size, which is the average of the width and height scale. Usually they are the same.";
        }
    }
}
