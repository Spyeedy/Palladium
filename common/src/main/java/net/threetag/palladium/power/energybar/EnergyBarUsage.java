package net.threetag.palladium.power.energybar;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.json.GsonUtil;

public class EnergyBarUsage {

    private final EnergyBarReference energyBar;
    private final int amount;

    public EnergyBarUsage(EnergyBarReference energyBar, int amount) {
        this.energyBar = energyBar;
        this.amount = amount;
    }

    public void consume(IPowerHolder holder) {
        var energyBar = this.energyBar.getEntry(holder.getEntity(), holder);

        if (energyBar != null) {
            energyBar.add(-this.amount);
        }
    }

    public static EnergyBarUsage fromJson(JsonObject json) {
        return new EnergyBarUsage(GsonUtil.getAsEnergyBarReference(json, "energy_bar"), GsonHelper.getAsInt(json, "amount"));
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("energy_bar", this.energyBar.toString());
        json.addProperty("amount", this.amount);
        return json;
    }
}
