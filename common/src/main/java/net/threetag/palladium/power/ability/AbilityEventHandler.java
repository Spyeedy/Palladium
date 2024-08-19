package net.threetag.palladium.power.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import net.threetag.palladiumcore.event.PlayerEvents;

import java.util.concurrent.atomic.AtomicReference;

public class AbilityEventHandler implements PlayerEvents.NameFormat, LivingEntityEvents.IncomingDamage, LivingEntityEvents.DamagePost {

    public static void init() {
        AbilityEventHandler handler = new AbilityEventHandler();
        LivingEntityEvents.INCOMING_DAMAGE.register(handler);
        LivingEntityEvents.DAMAGE_POST.register(handler);
        PlayerEvents.NAME_FORMAT.register(handler);
    }

    @Override
    public void playerNameFormat(Player player, Component username, AtomicReference<Component> displayName) {
        AbilityUtil.getEnabledInstances(player, AbilitySerializers.NAME_CHANGE.get()).stream().filter(ab -> ab.get(PalladiumDataComponents.Abilities.NAME_CHANGE_ACTIVE.get())).findFirst().ifPresent(ability -> {
            displayName.set(ability.getAbility().name);
        });
    }

    @Override
    public EventResult livingIncomingDamage(LivingEntity entity, DamageSource damageSource, AtomicReference<Float> amount) {
        if (damageSource.is(DamageTypes.IN_WALL) && AbilityUtil.isTypeEnabled(entity, AbilitySerializers.INTANGIBILITY.value())) {
            return EventResult.cancel();
        }

        for (AbilityInstance<DamageImmunityAbility> instance : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.DAMAGE_IMMUNITY.get())) {
            if (DamageImmunityAbility.isImmuneAgainst(instance, damageSource)) {
                return EventResult.cancel();
            }
        }

        return EventResult.pass();
    }

    @Override
    public void livingDamagePost(LivingEntity entity, DamageSource damageSource, float amount) {
        if (damageSource.getEntity() instanceof LivingEntity sourceEntity && AbilityUtil.isTypeEnabled(sourceEntity, AbilitySerializers.FIRE_ASPECT.value())) {
            boolean hasAddedExistingFire = false;
            int fireSeconds = 0;
            for (AbilityInstance<FireAspectAbility> instance : AbilityUtil.getEnabledInstances(sourceEntity, AbilitySerializers.FIRE_ASPECT.get())) {
                int time = Math.max(instance.getAbility().time, 0);
                if (!hasAddedExistingFire && instance.getAbility().shouldStackTime) {
                    fireSeconds = Math.min(fireSeconds + (entity.getRemainingFireTicks() / 20), instance.getAbility().maxTime);
                    hasAddedExistingFire = true;
                }
                fireSeconds += time;
            }
            entity.setRemainingFireTicks(fireSeconds);
        }
    }
}
