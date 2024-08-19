package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityConditions;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.PropertyManager;

public class HeldCondition extends KeyCondition {

    public static final MapCodec<HeldCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.INT.optionalFieldOf("cooldown", 0).forGetter(HeldCondition::getCooldown),
                    AbilityConditions.KeyType.CODEC.optionalFieldOf("key_type", AbilityConditions.KeyType.KEY_BIND).forGetter(HeldCondition::getKeyType),
                    Codec.BOOL.optionalFieldOf("needs_empty_hand", false).forGetter(HeldCondition::needsEmptyHand)
            ).apply(instance, HeldCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HeldCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, HeldCondition::getCooldown,
            AbilityConditions.KeyType.STREAM_CODEC, HeldCondition::getKeyType,
            ByteBufCodecs.BOOL, HeldCondition::needsEmptyHand,
            HeldCondition::new
    );

    public HeldCondition(int cooldown, AbilityConditions.KeyType type, boolean needsEmptyHand) {
        super(cooldown, type, needsEmptyHand, true);
    }

    @Override
    public void init(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        abilityInstance.startCooldown(entity, this.cooldown);
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        var abilityInstance = context.get(DataContextType.ABILITY_INSTANCE);

        if (entity == null || abilityInstance == null) {
            return false;
        }

        if (this.cooldown != 0 && abilityInstance.getCooldown() == 0 && abilityInstance.isKeyPressed()) {
            abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
        }
        return abilityInstance.isKeyPressed();
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder) {
        abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), true);
    }

    @Override
    public void onKeyReleased(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder) {
        abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
    }

    @Override
    public AbilityConditions.KeyPressType getKeyPressType() {
        return AbilityConditions.KeyPressType.HOLD;
    }

    @Override
    public CooldownType getCooldownType() {
        return CooldownType.DYNAMIC;
    }

    @Override
    public ConditionSerializer<HeldCondition> getSerializer() {
        return ConditionSerializers.HELD.get();
    }

    public static class Serializer extends ConditionSerializer<HeldCondition> {

        @Override
        public MapCodec<HeldCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HeldCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Allows the ability to be used while holding a key bind.";
        }
    }
}
