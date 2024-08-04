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
import net.threetag.palladium.util.property.PropertyManager;

public class ChatToggleCondition extends ChatMessageCondition {

    public static final MapCodec<ChatToggleCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("chat_message").forGetter(ChatToggleCondition::getChatMessage),
                    Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("cooldown", 0).forGetter(ChatToggleCondition::getCooldown)
            ).apply(instance, ChatToggleCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ChatToggleCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ChatToggleCondition::getChatMessage,
            ByteBufCodecs.VAR_INT, ChatToggleCondition::getCooldown,
            ChatToggleCondition::new
    );

    public ChatToggleCondition(String chatMessage, int cooldown) {
        super(chatMessage, cooldown);
    }

    @Override
    public void init(LivingEntity entity, AbilityInstance entry, PropertyManager manager) {
        entry.startCooldown(entity, this.cooldown);
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        var entry = context.get(DataContextType.ABILITY);

        if (entity == null || entry == null) {
            return false;
        }

        if (this.cooldown != 0 && entry.cooldown == 0) {
            entry.keyPressed = false;
        }
        return entry.keyPressed;
    }

    @Override
    public void onChat(LivingEntity entity, AbilityInstance entry) {
        entry.keyPressed = !entry.keyPressed;
    }

    @Override
    public CooldownType getCooldownType() {
        return CooldownType.DYNAMIC;
    }

    @Override
    public ConditionSerializer<ChatToggleCondition> getSerializer() {
        return ConditionSerializers.CHAT_TOGGLE.get();
    }

    public static class Serializer extends ConditionSerializer<ChatToggleCondition> {

        @Override
        public MapCodec<ChatToggleCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChatToggleCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Toggles the ability on and off after a chat message was sent.";
        }
    }
}
