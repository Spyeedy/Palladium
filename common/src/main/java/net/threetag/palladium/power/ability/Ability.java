package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.icon.Icon;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Ability {

    public static final Codec<Ability> CODEC = PalladiumRegistries.ABILITY_SERIALIZER.byNameCodec().dispatch(Ability::getSerializer, AbilitySerializer::codec);

    private final AbilityProperties properties;
    private final AbilityConditions conditions;
    private final List<EnergyBarUsage> energyBarUsages;
    private String key;

    public Ability(AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        this.properties = properties;
        this.conditions = conditions;
        this.energyBarUsages = energyBarUsages;
    }

    /**
     * Do NOT use!
     */
    @Deprecated
    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public abstract AbilitySerializer<?> getSerializer();

    public void registerDataComponents(DataComponentMap.Builder components) {

    }

    public AbilityProperties getProperties() {
        return this.properties;
    }

    public AbilityConditions getConditions() {
        return this.conditions;
    }

    public List<EnergyBarUsage> getEnergyBarUsages() {
        return this.energyBarUsages;
    }

    public Component getDisplayName() {
        Component title = this.properties.getTitle();
        ResourceLocation id = PalladiumRegistries.ABILITY_SERIALIZER.getKey(this.getSerializer());
        return title != null ? title : Component.translatable("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath());
    }

    public void tick(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder, boolean enabled) {

    }

    public void animationTimerTick(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder, boolean enabled, AnimationTimer animationTimer) {
        animationTimer.tickAndUpdate(enabled);
    }

    public void firstTick(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder, boolean enabled) {

    }

    public void lastTick(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder, boolean enabled) {

    }

    protected static <B extends Ability> RecordCodecBuilder<B, AbilityProperties> propertiesCodec() {
        return AbilityProperties.CODEC.optionalFieldOf("properties", AbilityProperties.BASIC).forGetter(Ability::getProperties);
    }

    protected static <B extends Ability> RecordCodecBuilder<B, AbilityConditions> conditionsCodec() {
        return AbilityConditions.CODEC.optionalFieldOf("conditions", AbilityConditions.EMPTY).forGetter(Ability::getConditions);
    }

    protected static <B extends Ability> RecordCodecBuilder<B, List<EnergyBarUsage>> energyBarUsagesCodec() {
        return CodecUtils.listOrPrimitive(EnergyBarUsage.CODEC).optionalFieldOf("energy_bar_usage", Collections.emptyList()).forGetter(Ability::getEnergyBarUsages);
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
