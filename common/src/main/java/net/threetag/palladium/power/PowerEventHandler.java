package net.threetag.palladium.power;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.condition.ChatMessageCondition;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladiumcore.event.ChatEvents;
import net.threetag.palladiumcore.event.EntityEvents;
import net.threetag.palladiumcore.event.EventResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PowerEventHandler implements EntityEvents.Tick, ChatEvents.ServerSubmitted {

    public static final List<String> CHECK_FOR_CHAT_MESSAGES = new ArrayList<>();

    public static void init() {
        var instance = new PowerEventHandler();

        EntityEvents.TICK_POST.register(instance);
        ChatEvents.SERVER_SUBMITTED.register(instance);
    }

    @Override
    public void entityTick(Entity entity) {
        if (entity instanceof LivingEntity living) {
            PowerUtil.getPowerHandler(living).ifPresent(IPowerHandler::tick);
        }
    }

    @Override
    public EventResult chatMessageSubmitted(ServerPlayer player, String rawMessage, Component message) {
        if (CHECK_FOR_CHAT_MESSAGES.contains(rawMessage.trim().toLowerCase(Locale.ROOT))) {
            for (AbilityInstance entry : AbilityUtil.getInstances(player)) {
                for (Condition condition : entry.getConfiguration().getUnlockingConditions()) {
                    if (condition instanceof ChatMessageCondition chat && chat.chatMessage.trim().equalsIgnoreCase(rawMessage.trim())) {
                        chat.onChat(player, entry);
                    }
                }
                for (Condition condition : entry.getConfiguration().getEnablingConditions()) {
                    if (condition instanceof ChatMessageCondition chat && chat.chatMessage.trim().equalsIgnoreCase(rawMessage.trim())) {
                        chat.onChat(player, entry);
                    }
                }
            }
        }
        return EventResult.pass();
    }
}
