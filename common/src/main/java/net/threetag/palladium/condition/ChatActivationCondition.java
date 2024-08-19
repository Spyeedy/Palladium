package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.context.DataContext;

import java.util.Objects;

public class ChatActivationCondition extends ChatMessageCondition {

    public static final MapCodec<ChatActivationCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("chat_message").forGetter(ChatActivationCondition::getChatMessage),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks").forGetter(ChatActivationCondition::getTicks),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("cooldown", 0).forGetter(ChatActivationCondition::getCooldown)
            ).apply(instance, ChatActivationCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ChatActivationCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ChatActivationCondition::getChatMessage,
            ByteBufCodecs.VAR_INT, ChatActivationCondition::getTicks,
            ByteBufCodecs.VAR_INT, ChatActivationCondition::getCooldown,
            ChatActivationCondition::new
    );

    public final int ticks;

    public ChatActivationCondition(String chatMessage, int ticks, int cooldown) {
        super(chatMessage, cooldown);
        this.ticks = ticks;
    }

    public int getTicks() {
        return ticks;
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
    public void onChat(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        if (abilityInstance.getCooldown() <= 0 && abilityInstance.getActivatedTime() == 0) {
            abilityInstance.startActivationTimer(entity, this.ticks);
        }
    }

    @Override
    public ConditionSerializer<ChatActivationCondition> getSerializer() {
        return ConditionSerializers.CHAT_ACTIVATION.get();
    }

    public static class Serializer extends ConditionSerializer<ChatActivationCondition> {

        @Override
        public MapCodec<ChatActivationCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChatActivationCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "This condition is used to activate the ability when a chat message was sent for a certain amount of ticks.";
        }
    }
}
