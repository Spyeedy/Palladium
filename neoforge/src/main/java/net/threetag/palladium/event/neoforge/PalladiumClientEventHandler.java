package net.threetag.palladium.event.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;

@EventBusSubscriber(modid = Palladium.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class PalladiumClientEventHandler {

    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent e) {
        if (AbilityUtil.isTypeEnabled(e.getEntity(), AbilitySerializers.INVISIBILITY.get())) {
            e.modifyVisibility(0);
        }
    }

}
