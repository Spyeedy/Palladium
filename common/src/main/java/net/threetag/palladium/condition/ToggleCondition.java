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
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public class ToggleCondition extends KeyCondition {

    public static final MapCodec<ToggleCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.INT.optionalFieldOf("cooldown", 0).forGetter(ToggleCondition::getCooldown),
                    AbilityConditions.KeyType.CODEC.optionalFieldOf("key_type", AbilityConditions.KeyType.KEY_BIND).forGetter(ToggleCondition::getKeyType),
                    Codec.BOOL.optionalFieldOf("needs_empty_hand", false).forGetter(ToggleCondition::needsEmptyHand),
                    Codec.BOOL.optionalFieldOf("allow_scrolling_when_crouching", true).forGetter(ToggleCondition::allowScrollingWhenCrouching)

            ).apply(instance, ToggleCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ToggleCondition::getCooldown,
            AbilityConditions.KeyType.STREAM_CODEC, ToggleCondition::getKeyType,
            ByteBufCodecs.BOOL, ToggleCondition::needsEmptyHand,
            ByteBufCodecs.BOOL, ToggleCondition::allowScrollingWhenCrouching,
            ToggleCondition::new
    );

    public ToggleCondition(int cooldown, AbilityConditions.KeyType type, boolean needsEmptyHand, boolean allowScrollingWhenCrouching) {
        super(cooldown, type, needsEmptyHand, allowScrollingWhenCrouching);
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

        if (this.cooldown != 0 && abilityInstance.getCooldown() == 0) {
            abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
        }
        return abilityInstance.isKeyPressed();
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder) {
        abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), !abilityInstance.isKeyPressed());
    }

    @Override
    public CooldownType getCooldownType() {
        return CooldownType.DYNAMIC;
    }

    @Override
    public AbilityConditions.KeyPressType getKeyPressType() {
        return AbilityConditions.KeyPressType.TOGGLE;
    }

    @Override
    public ConditionSerializer<ToggleCondition> getSerializer() {
        return ConditionSerializers.TOGGLE.get();
    }

    public static class Serializer extends ConditionSerializer<ToggleCondition> {

        @Override
        public MapCodec<ToggleCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ToggleCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Toggles the ability on and off after a key press or mouse click.";
        }
    }
}
