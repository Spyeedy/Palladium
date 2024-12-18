package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.icon.ExperienceIcon;
import net.threetag.palladium.power.ability.Ability;

public class ExperienceLevelBuyableCondition extends BuyableCondition {

    public static final MapCodec<ExperienceLevelBuyableCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(ExtraCodecs.POSITIVE_INT.fieldOf("xp_level").forGetter(ExperienceLevelBuyableCondition::getXpLevel)
            ).apply(instance, ExperienceLevelBuyableCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ExperienceLevelBuyableCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ExperienceLevelBuyableCondition::getXpLevel, ExperienceLevelBuyableCondition::new
    );

    private final int xpLevel;

    public ExperienceLevelBuyableCondition(int xpLevel) {
        this.xpLevel = xpLevel;
    }

    public int getXpLevel() {
        return xpLevel;
    }

    @Override
    public Ability.UnlockData createData() {
        return new Ability.UnlockData(new ExperienceIcon(1, true), this.xpLevel, Component.translatable("gui.palladium.powers.buy_ability.experience_level" + (this.xpLevel > 1 ? "_plural" : "")));
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        if (entity instanceof Player player) {
            return player.experienceLevel >= this.xpLevel;
        }

        return false;
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.experienceLevel >= this.xpLevel) {
                player.giveExperienceLevels(-this.xpLevel);
                return true;
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer<ExperienceLevelBuyableCondition> getSerializer() {
        return ConditionSerializers.EXPERIENCE_LEVEL_BUYABLE.get();
    }

    public static class Serializer extends ConditionSerializer<ExperienceLevelBuyableCondition> {

        @Override
        public MapCodec<ExperienceLevelBuyableCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExperienceLevelBuyableCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "A condition that makes the ability buyable for a certain amount of xp levels.";
        }
    }
}
