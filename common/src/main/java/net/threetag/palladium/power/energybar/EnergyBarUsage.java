package net.threetag.palladium.power.energybar;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.json.GsonUtil;

public record EnergyBarUsage(EnergyBarReference energyBar, int amount) {

    public static final Codec<EnergyBarUsage> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                EnergyBarReference.CODEC.fieldOf("energy_bar").forGetter(EnergyBarUsage::energyBar),
                Codec.INT.fieldOf("amount").forGetter(EnergyBarUsage::amount)
        ).apply(instance, EnergyBarUsage::new)
    );

    public static final StreamCodec<FriendlyByteBuf, EnergyBarUsage> STREAM_CODEC = StreamCodec.composite(EnergyBarReference.STREAM_CODEC, EnergyBarUsage::energyBar, ByteBufCodecs.INT, EnergyBarUsage::amount, EnergyBarUsage::new);

    public void consume(IPowerHolder holder) {
        var energyBar = this.energyBar.getBar(holder.getEntity(), holder);

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
