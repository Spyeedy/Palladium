package net.threetag.palladium.power.energybar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.entity.value.EntityDependentNumber;
import net.threetag.palladium.util.CodecUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record EnergyBarConfiguration(Color color, @Nullable EntityDependentNumber syncedValue,
                                     EntityDependentNumber maxValue, int autoIncrease, int autoIncreaseInterval) {

    public static final Codec<EnergyBarConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CodecUtils.COLOR_CODEC.optionalFieldOf("color", Color.WHITE).forGetter(EnergyBarConfiguration::color),
                    EntityDependentNumber.CODEC.optionalFieldOf("synced_value", null).forGetter(EnergyBarConfiguration::syncedValue),
                    EntityDependentNumber.CODEC.fieldOf("max").forGetter(EnergyBarConfiguration::maxValue),
                    Codec.INT.optionalFieldOf("auto_increase_per_tick", 0).forGetter(EnergyBarConfiguration::autoIncrease),
                    Codec.INT.optionalFieldOf("auto_increase_interval", 1).forGetter(EnergyBarConfiguration::autoIncreaseInterval)
            ).apply(instance, EnergyBarConfiguration::new)
    );
}
