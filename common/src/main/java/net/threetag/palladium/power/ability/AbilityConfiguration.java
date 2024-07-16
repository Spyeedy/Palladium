package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.icon.Icon;
import net.threetag.palladium.util.icon.IconSerializer;
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

    private String id;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
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

    public static class UnlockData {

        public final Icon icon;
        public final int amount;
        public final Component description;

        public UnlockData(Icon icon, int amount, Component description) {
            this.icon = icon;
            this.amount = amount;
            this.description = description;
        }

        public UnlockData(FriendlyByteBuf buf) {
            this.icon = IconSerializer.parseNBT(Objects.requireNonNull(buf.readAnySizeNbt()));
            this.amount = buf.readInt();
            this.description = buf.readComponent();
        }

        public void toBuffer(FriendlyByteBuf buf) {
            buf.writeNbt(IconSerializer.serializeNBT(this.icon));
            buf.writeInt(this.amount);
            buf.writeComponent(this.description);
        }
    }
}
