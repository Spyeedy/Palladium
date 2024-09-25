package net.threetag.palladium.power.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class AbilityEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> e) {
        if (AbilityUtil.isTypeEnabled(e.getEntity(), AbilitySerializers.INVISIBILITY.get())) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent e) {
        if (AbilityUtil.isTypeEnabled(e.getEntity(), AbilitySerializers.INVISIBILITY.get())) {
            e.modifyVisibility(0);
        }
    }

}
