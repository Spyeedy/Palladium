package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.icon.Icon;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AbilityConfiguration {

    public static final Codec<AbilityConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PalladiumRegistries.ABILITY.byNameCodec().fieldOf("type").forGetter(AbilityConfiguration::getAbility),
                    CompoundTag.CODEC.optionalFieldOf("properties", new CompoundTag()).forGetter(config -> config.getPropertyManager().toNBT(false)),
                    AbilityConditions.DIRECT_CODEC.optionalFieldOf("conditions", AbilityConditions.EMPTY).forGetter(AbilityConfiguration::getConditions),
                    CodecUtils.listOrPrimitive(EnergyBarUsage.CODEC).optionalFieldOf("energy_bar_usage", Collections.emptyList()).forGetter(AbilityConfiguration::getEnergyBarUsages)
            ).apply(instance, AbilityConfiguration::new)
    );

    private String key;
    private final Ability ability;
    private final PropertyManager propertyManager;
    private final AbilityConditions conditions;
    private final List<EnergyBarUsage> energyBarUsages;

    public AbilityConfiguration(Ability ability, CompoundTag rawProperties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        this.ability = ability;
        this.propertyManager = ability.propertyManager.copy();
        this.propertyManager.fromJSON(rawProperties);
        this.conditions = conditions;
        this.energyBarUsages = energyBarUsages;
    }

    public void setKey(String id) {
        this.key = id;
    }

    public String getKey() {
        return this.key;
    }

    public Ability getAbility() {
        return ability;
    }

    public Component getDisplayName() {
        Component title = this.propertyManager.get(Ability.TITLE);
        ResourceLocation id = PalladiumRegistries.ABILITY.getKey(this.getAbility());
        return title != null ? title : Component.translatable("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath());
    }

    public <T> AbilityConfiguration set(PalladiumProperty<T> data, T value) {
        this.propertyManager.set(data, value);
        return this;
    }

    public <T> T get(PalladiumProperty<T> property) {
        return this.propertyManager.get(property);
    }

    public List<EnergyBarUsage> getEnergyBarUsages() {
        return this.energyBarUsages;
    }

    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    public CompoundTag getPropertiesAsNbt() {
        return this.propertyManager.toNBT(false);
    }

    public AbilityConditions getConditions() {
        return this.conditions;
    }

    public record UnlockData(Icon icon, int amount, Component description) {

        public static final StreamCodec<RegistryFriendlyByteBuf, UnlockData> STREAM_CODEC = StreamCodec.composite(
                Icon.STREAM_CODEC, UnlockData::icon,
                ByteBufCodecs.VAR_INT, UnlockData::amount,
                ComponentSerialization.STREAM_CODEC, UnlockData::description,
                UnlockData::new
        );

    }
}
