package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.Objects;

public class ActivationCondition extends KeyCondition {

    public final int ticks;

    public ActivationCondition(int ticks, int cooldown, AbilityConfiguration.KeyType type, boolean needsEmptyHand, boolean allowScrollingWhenCrouching) {
        super(cooldown, type, needsEmptyHand, allowScrollingWhenCrouching);
        this.ticks = ticks;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var entry = context.getAbility();

        if (entity == null || entry == null) {
            return false;
        }

        if (this.cooldown != 0 && Objects.requireNonNull(entry).activationTimer == 1) {
            entry.startCooldown(context.getLivingEntity(), this.cooldown);
        }
        return Objects.requireNonNull(entry).activationTimer > 0;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityInstance entry, Power power, IPowerHolder holder) {
        if (entry.cooldown <= 0 && entry.activationTimer == 0) {
            entry.startActivationTimer(entity, this.ticks);
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ACTIVATION.get();
    }

    @Override
    public AbilityConfiguration.KeyPressType getKeyPressType() {
        return AbilityConfiguration.KeyPressType.ACTIVATION;
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> TICKS = new IntegerProperty("ticks").configurable("The amount of ticks the ability will be active for");

        public Serializer() {
            this.withProperty(ActionCondition.Serializer.COOLDOWN, 0);
            this.withProperty(TICKS, 60);
            this.withProperty(KeyCondition.KEY_TYPE_WITH_SCROLLING, AbilityConfiguration.KeyType.KEY_BIND);
            this.withProperty(KeyCondition.NEEDS_EMPTY_HAND, false);
            this.withProperty(KeyCondition.ALLOW_SCROLLING_DURING_CROUCHING, true);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ActivationCondition(this.getProperty(json, TICKS), this.getProperty(json, ActionCondition.Serializer.COOLDOWN), this.getProperty(json, KeyCondition.KEY_TYPE_WITH_SCROLLING), this.getProperty(json, KeyCondition.NEEDS_EMPTY_HAND), this.getProperty(json, KeyCondition.ALLOW_SCROLLING_DURING_CROUCHING));
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
