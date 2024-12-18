package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityConditions;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.data.DataContext;

import java.util.Objects;

public class ActivationCondition extends KeyCondition {

    public static final MapCodec<ActivationCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.INT.optionalFieldOf("ticks", 60).forGetter(ActivationCondition::getTicks),
                    Codec.INT.optionalFieldOf("cooldown", 0).forGetter(ActivationCondition::getCooldown),
                    AbilityConditions.KeyType.CODEC.optionalFieldOf("key_type", AbilityConditions.KeyType.KEY_BIND).forGetter(ActivationCondition::getKeyType),
                    Codec.BOOL.optionalFieldOf("needs_empty_hand", false).forGetter(ActivationCondition::needsEmptyHand),
                    Codec.BOOL.optionalFieldOf("allow_scrolling_when_crouching", true).forGetter(ActivationCondition::allowScrollingWhenCrouching)

            ).apply(instance, ActivationCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ActivationCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ActivationCondition::getTicks,
            ByteBufCodecs.VAR_INT, ActivationCondition::getCooldown,
            AbilityConditions.KeyType.STREAM_CODEC, ActivationCondition::getKeyType,
            ByteBufCodecs.BOOL, ActivationCondition::needsEmptyHand,
            ByteBufCodecs.BOOL, ActivationCondition::allowScrollingWhenCrouching,
            ActivationCondition::new
    );

    public final int ticks;

    public ActivationCondition(int ticks, int cooldown, AbilityConditions.KeyType type, boolean needsEmptyHand, boolean allowScrollingWhenCrouching) {
        super(cooldown, type, needsEmptyHand, allowScrollingWhenCrouching);
        this.ticks = ticks;
    }

    public int getTicks() {
        return this.ticks;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var entry = context.getAbility();

        if (entity == null || entry == null) {
            return false;
        }

        if (this.cooldown != 0 && Objects.requireNonNull(entry).getActivatedTime() == 1) {
            entry.startCooldown(context.getLivingEntity(), this.cooldown);
        }
        return Objects.requireNonNull(entry).getActivatedTime() > 0;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityInstance<?> abilityInstance, PowerHolder holder) {
        if (abilityInstance.getCooldown() <= 0 && abilityInstance.getActivatedTime() == 0) {
            abilityInstance.startActivationTimer(entity, this.ticks);
        }
    }

    @Override
    public ConditionSerializer<ActivationCondition> getSerializer() {
        return ConditionSerializers.ACTIVATION.get();
    }

    @Override
    public AbilityConditions.KeyPressType getKeyPressType() {
        return AbilityConditions.KeyPressType.ACTIVATION;
    }

    public static class Serializer extends ConditionSerializer<ActivationCondition> {

        @Override
        public MapCodec<ActivationCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ActivationCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "This condition is used to activate the ability when a key is pressed or a mouse button is clicked for a certain amount of ticks.";
        }
    }
}
