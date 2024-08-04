package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

import java.util.Objects;

public class ChatActionCondition extends ChatMessageCondition {

    public static final MapCodec<ChatActionCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("chat_message").forGetter(ChatActionCondition::getChatMessage),
                    Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("cooldown", 0).forGetter(ChatActionCondition::getCooldown)
            ).apply(instance, ChatActionCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ChatActionCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ChatActionCondition::getChatMessage,
            ByteBufCodecs.VAR_INT, ChatActionCondition::getCooldown,
            ChatActionCondition::new
    );

    public ChatActionCondition(String chatMessage, int cooldown) {
        super(chatMessage, cooldown);
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        var entry = context.get(DataContextType.ABILITY);

        if (entity == null || entry == null) {
            return false;
        }

        if (Objects.requireNonNull(entry).keyPressed) {
            entry.keyPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public void onChat(LivingEntity entity, AbilityInstance entry) {
        if (entry.cooldown == 0) {
            entry.keyPressed = true;

            if (this.cooldown != 0) {
                entry.startCooldown(entity, this.cooldown);
            }
        }
    }

    @Override
    public ConditionSerializer<ChatActionCondition> getSerializer() {
        return ConditionSerializers.CHAT_ACTION.get();
    }

    public static class Serializer extends ConditionSerializer<ChatActionCondition> {

        @Override
        public MapCodec<ChatActionCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChatActionCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "This condition will be active once, when a chat message has been sent.";
        }
    }
}
