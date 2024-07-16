package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.*;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public record AbilityReference(@Nullable ResourceLocation powerId, @NotNull String abilityId) {

    public static final Codec<AbilityReference> CODEC = Codec.STRING.comapFlatMap(AbilityReference::read, AbilityReference::toString).stable();

    public static AbilityReference parse(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new AbilityReference(null, s[0]);
        } else {
            return new AbilityReference(ResourceLocation.parse(s[0]), s[1]);
        }
    }

    public static DataResult<AbilityReference> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid ability reference: " + path + " " + e.getMessage());
        }
    }

    @Nullable
    public AbilityInstance getInstance(LivingEntity entity) {
        return this.getInstance(entity, null);
    }

    @Nullable
    public AbilityInstance getInstance(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
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
            return powerHolder.getAbilities().get(this.abilityId);
        }

        return null;
    }

    public Optional<AbilityInstance> optional(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
        return Optional.ofNullable(this.getInstance(entity, powerHolder));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeNullable(this.powerId, (buf1, resourceLocation) -> buf.writeResourceLocation(resourceLocation));
        buf.writeUtf(this.abilityId);
    }

    public static AbilityReference fromBuffer(FriendlyByteBuf buf) {
        return new AbilityReference(buf.readNullable(FriendlyByteBuf::readResourceLocation), buf.readUtf());
    }

    @Override
    public String toString() {
        if (this.powerId == null) {
            return this.abilityId;
        }
        return this.powerId + "#" + this.abilityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbilityReference that)) return false;
        return Objects.equals(this.powerId, that.powerId) && Objects.equals(this.abilityId, that.abilityId);
    }
}
