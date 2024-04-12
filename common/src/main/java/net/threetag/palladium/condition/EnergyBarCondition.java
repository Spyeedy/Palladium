package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;

public class EnergyBarCondition extends Condition {

    private final EnergyBarReference energyBar;
    private final int min, max;

    public EnergyBarCondition(EnergyBarReference energyBar, int min, int max) {
        this.energyBar = energyBar;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        EnergyBar energyBar = this.energyBar.getEntry(entity, context.getPowerHolder());

        if (energyBar == null) {
            return false;
        } else {
            return this.min <= energyBar.get() && energyBar.get() <= this.max;
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ENERGY_BAR.get();
    }

    public static class Serializer extends ConditionSerializer {

        // TODO remove in 1.21
        public static final PalladiumProperty<ResourceLocation> POWER = new ResourceLocationProperty("power").configurable("ID of the power where is the desired energy bar is located. Can be null IF used for abilities, then it will look into the current power");
        public static final PalladiumProperty<String> ENERGY_BAR = new StringProperty("energy_bar").configurable("ID of the desired energy bar");
        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the energy in the energy bar");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the energy in the energy bar");

        public Serializer() {
            this.withProperty(POWER, null);
            this.withProperty(ENERGY_BAR, "energy_bar_name");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, Integer.MAX_VALUE);
        }

        @Override
        public Condition make(JsonObject json) {
            EnergyBarReference energyBar = EnergyBarReference.fromString(this.getProperty(json, ENERGY_BAR));

            if (this.getProperty(json, POWER) != null) {
                energyBar = new EnergyBarReference(this.getProperty(json, POWER), this.getProperty(json, ENERGY_BAR));
            }

            return new EnergyBarCondition(energyBar,
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if energy bar has enough energy in it.";
        }
    }
}
