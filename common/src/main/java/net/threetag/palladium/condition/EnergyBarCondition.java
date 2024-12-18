package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladium.data.DataContext;

public record EnergyBarCondition(EnergyBarReference energyBar, int min, int max) implements Condition {

    public static final MapCodec<EnergyBarCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    EnergyBarReference.CODEC.fieldOf("energy_bar").forGetter(EnergyBarCondition::energyBar),
                    Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(EnergyBarCondition::min),
                    Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(EnergyBarCondition::max)
            ).apply(instance, EnergyBarCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyBarCondition> STREAM_CODEC = StreamCodec.composite(
            EnergyBarReference.STREAM_CODEC, EnergyBarCondition::energyBar,
            ByteBufCodecs.VAR_INT, EnergyBarCondition::min,
            ByteBufCodecs.VAR_INT, EnergyBarCondition::max,
            EnergyBarCondition::new
    );

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        EnergyBar energyBar = this.energyBar.getBar(entity, context.getPowerHolder());

        if (energyBar == null) {
            return false;
        } else {
            return this.min <= energyBar.get() && energyBar.get() <= this.max;
        }
    }

    @Override
    public ConditionSerializer<EnergyBarCondition> getSerializer() {
        return ConditionSerializers.ENERGY_BAR.get();
    }

    public static class Serializer extends ConditionSerializer<EnergyBarCondition> {

        @Override
        public MapCodec<EnergyBarCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EnergyBarCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if energy bar has enough energy in it.";
        }
    }
}
