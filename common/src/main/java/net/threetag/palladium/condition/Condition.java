package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConditions;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.Collections;
import java.util.List;

public abstract class Condition {

    public static final Codec<Condition> CODEC = PalladiumRegistries.CONDITION_SERIALIZER.byNameCodec().dispatch(Condition::getSerializer, ConditionSerializer::codec);

    private ConditionEnvironment environment;

    public Condition setEnvironment(ConditionEnvironment environment) {
        this.environment = environment;
        return this;
    }

    public ConditionEnvironment getEnvironment() {
        return environment;
    }

    public abstract boolean active(DataContext context);

    public boolean needsKey() {
        return false;
    }

    public AbilityConditions.KeyType getKeyType() {
        return AbilityConditions.KeyType.KEY_BIND;
    }

    public AbilityConditions.KeyPressType getKeyPressType() {
        return AbilityConditions.KeyPressType.ACTION;
    }

    public boolean handlesCooldown() {
        return false;
    }

    public CooldownType getCooldownType() {
        return CooldownType.STATIC;
    }

    public void init(LivingEntity entity, AbilityInstance entry, PropertyManager manager) {

    }

    public void registerAbilityProperties(AbilityInstance entry, PropertyManager manager) {

    }

    public void onKeyPressed(LivingEntity entity, AbilityInstance entry, Power power, IPowerHolder holder) {

    }

    public void onKeyReleased(LivingEntity entity, AbilityInstance entry, Power power, IPowerHolder holder) {

    }

    public abstract ConditionSerializer<?> getSerializer();

    public List<String> getDependentAbilities() {
        return Collections.emptyList();
    }
}
