package net.threetag.palladium.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerEventHandler;
import net.threetag.palladium.power.ability.AbilityInstance;

import java.util.Locale;

public abstract class ChatMessageCondition implements Condition {

    public final String chatMessage;
    public final int cooldown;

    public ChatMessageCondition(String chatMessage, int cooldown) {
        this.chatMessage = chatMessage;
        this.cooldown = cooldown;
        PowerEventHandler.CHECK_FOR_CHAT_MESSAGES.add(chatMessage.trim().toLowerCase(Locale.ROOT));
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public int getCooldown() {
        return cooldown;
    }

    public abstract void onChat(LivingEntity entity, AbilityInstance entry);

    @Override
    public boolean handlesCooldown() {
        return this.cooldown > 0;
    }

}
