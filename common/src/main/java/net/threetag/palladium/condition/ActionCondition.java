package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.ability.AbilityConditions;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

import java.util.Objects;

public class ActionCondition extends KeyCondition {

    public static final MapCodec<ActionCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.INT.optionalFieldOf("cooldown", 0).forGetter(ActionCondition::getCooldown),
                    AbilityConditions.KeyType.CODEC.optionalFieldOf("key_type", AbilityConditions.KeyType.KEY_BIND).forGetter(ActionCondition::getKeyType),
                    Codec.BOOL.optionalFieldOf("needs_empty_hand", false).forGetter(ActionCondition::needsEmptyHand),
                    Codec.BOOL.optionalFieldOf("allow_scrolling_when_crouching", true).forGetter(ActionCondition::allowScrollingWhenCrouching)

            ).apply(instance, ActionCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ActionCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ActionCondition::getCooldown,
            AbilityConditions.KeyType.STREAM_CODEC, ActionCondition::getKeyType,
            ByteBufCodecs.BOOL, ActionCondition::needsEmptyHand,
            ByteBufCodecs.BOOL, ActionCondition::allowScrollingWhenCrouching,
            ActionCondition::new
    );

    public ActionCondition(int cooldown, AbilityConditions.KeyType type, boolean needsEmptyHand, boolean allowScrollingWhenCrouching) {
        super(cooldown, type, needsEmptyHand, allowScrollingWhenCrouching);
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var ability = context.get(DataContextType.ABILITY);

        if (entity == null || ability == null) {
            return false;
        }

        if (Objects.requireNonNull(ability).keyPressed) {
            ability.keyPressed = false;
            if (ability.getEnabledTicks() == 0 && this.cooldown != 0) {
                ability.startCooldown(entity, this.cooldown);
            }
            return true;
        }

        return false;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityInstance entry, Power power, PowerHolder holder) {
        if (entry.cooldown == 0) {
            entry.keyPressed = true;
        }
    }

    @Override
    public ConditionSerializer<ActionCondition> getSerializer() {
        return ConditionSerializers.ACTION.get();
    }

    public static class Serializer extends ConditionSerializer<ActionCondition> {

        @Override
        public MapCodec<ActionCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ActionCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "This condition is used to activate the power when a key is pressed or a mouse button is clicked.";
        }
    }
}
