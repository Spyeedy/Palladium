package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.data.DataContextType;

public class ChatToggleCondition extends ChatMessageCondition {

    public static final MapCodec<ChatToggleCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("chat_message").forGetter(ChatToggleCondition::getChatMessage),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("cooldown", 0).forGetter(ChatToggleCondition::getCooldown)
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
        return abilityInstance.getComponents().getOrDefault(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false);
    }

    @Override
    public void onChat(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        abilityInstance.set(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), !abilityInstance.getComponents().getOrDefault(PalladiumDataComponents.Abilities.KEY_PRESSED.get(), false));
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
