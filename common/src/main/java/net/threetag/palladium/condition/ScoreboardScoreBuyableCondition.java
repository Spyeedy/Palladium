package net.threetag.palladium.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.ScoreboardUtil;
import net.threetag.palladium.util.icon.Icon;

public class ScoreboardScoreBuyableCondition extends BuyableCondition {

    public static final MapCodec<ScoreboardScoreBuyableCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("objective").forGetter(ScoreboardScoreBuyableCondition::getObjective),
                    Codec.intRange(1, Integer.MAX_VALUE).fieldOf("score").forGetter(ScoreboardScoreBuyableCondition::getAmount),
                    Icon.CODEC.fieldOf("icon").forGetter(ScoreboardScoreBuyableCondition::getIcon),
                    ComponentSerialization.CODEC.fieldOf("description").forGetter(ScoreboardScoreBuyableCondition::getDescription)
            ).apply(instance, ScoreboardScoreBuyableCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ScoreboardScoreBuyableCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ScoreboardScoreBuyableCondition::getObjective,
            ByteBufCodecs.VAR_INT, ScoreboardScoreBuyableCondition::getAmount,
            Icon.STREAM_CODEC, ScoreboardScoreBuyableCondition::getIcon,
            ComponentSerialization.STREAM_CODEC, ScoreboardScoreBuyableCondition::getDescription,
            ScoreboardScoreBuyableCondition::new
    );

    private final String objective;
    private final int amount;
    private final Icon icon;
    private final Component description;

    public ScoreboardScoreBuyableCondition(String objective, int amount, Icon icon, Component description) {
        this.objective = objective;
        this.amount = amount;
        this.icon = icon;
        this.description = description;
    }

    public String getObjective() {
        return objective;
    }

    public int getAmount() {
        return amount;
    }

    public Icon getIcon() {
        return icon;
    }

    public Component getDescription() {
        return description;
    }

    @Override
    public AbilityConfiguration.UnlockData createData() {
        return new AbilityConfiguration.UnlockData(this.icon, this.amount, this.description);
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        if (entity instanceof Player player) {
            int score = ScoreboardUtil.getScore(player, this.objective);
            return score >= this.amount;
        }

        return false;
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        if (entity instanceof Player player) {
            int score = ScoreboardUtil.getScore(player, this.objective);

            if (score >= this.amount) {
                ScoreboardUtil.setScore(player, this.objective, score - this.amount);
                return true;
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer<ScoreboardScoreBuyableCondition> getSerializer() {
        return ConditionSerializers.SCOREBOARD_SCORE_BUYABLE.get();
    }

    public static class Serializer extends ConditionSerializer<ScoreboardScoreBuyableCondition> {

        @Override
        public MapCodec<ScoreboardScoreBuyableCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ScoreboardScoreBuyableCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "A buyable condition that requires a certain score for a scoreboard objective.";
        }
    }
}
