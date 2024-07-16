package net.threetag.palladium.power.energybar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public record EnergyBarReference(@Nullable ResourceLocation powerId, @NotNull String energyBarName) {

    public static final Codec<EnergyBarReference> CODEC = Codec.STRING.comapFlatMap(EnergyBarReference::read, EnergyBarReference::toString).stable();
    public static final StreamCodec<FriendlyByteBuf, EnergyBarReference> STREAM_CODEC = StreamCodec.of((buf, ref) -> {
        buf.writeNullable(ref.powerId, FriendlyByteBuf::writeResourceLocation);
        buf.writeUtf(ref.energyBarName);
    }, buf -> {
        var powerId = buf.readNullable(FriendlyByteBuf::readResourceLocation);
        var barName = buf.readUtf();
        return new EnergyBarReference(powerId, barName);
    });

    public static EnergyBarReference parse(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new EnergyBarReference(null, s[0]);
        } else {
            return new EnergyBarReference(ResourceLocation.parse(s[0]), s[1]);
        }
    }

    public static DataResult<EnergyBarReference> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid energy bar reference: " + path + " " + e.getMessage());
        }
    }

    @Nullable
    public EnergyBar getBar(LivingEntity entity) {
        return this.getBar(entity, null);
    }

    @Nullable
    public EnergyBar getBar(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
        if (this.powerId != null) {
            IPowerHandler handler = PowerUtil.getPowerHandler(entity).orElse(null);
            Power power = entity.registryAccess().registry(PalladiumRegistryKeys.POWER).orElseThrow().get(this.powerId);

            if (power != null && handler != null) {
                powerHolder = handler.getPowerHolder(power);
            } else {
                powerHolder = null;
            }
        }

        if (powerHolder != null) {
            return powerHolder.getEnergyBars().get(this.energyBarName);
        }

        return null;
    }

    public Optional<EnergyBar> optional(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
        return Optional.ofNullable(this.getBar(entity, powerHolder));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeNullable(this.powerId, (buf1, resourceLocation) -> buf.writeResourceLocation(resourceLocation));
        buf.writeUtf(this.energyBarName);
    }

    public static EnergyBarReference fromBuffer(FriendlyByteBuf buf) {
        return new EnergyBarReference(buf.readNullable(FriendlyByteBuf::readResourceLocation), buf.readUtf());
    }

    @Override
    public String toString() {
        if (this.powerId == null) {
            return this.energyBarName;
        }
        return this.powerId + "#" + this.energyBarName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnergyBarReference that)) return false;
        return Objects.equals(this.powerId, that.powerId) && Objects.equals(this.energyBarName, that.energyBarName);
    }
}
