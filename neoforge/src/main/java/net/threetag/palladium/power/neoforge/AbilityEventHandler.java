package net.threetag.palladium.power.neoforge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID)
public class AbilityEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> e) {
        if (!AbilityUtil.getEnabledEntries(e.getEntity(), AbilitySerializers.INVISIBILITY.get()).isEmpty()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent e) {
        if (!AbilityUtil.getEnabledEntries(e.getEntity(), AbilitySerializers.INVISIBILITY.get()).isEmpty()) {
            e.modifyVisibility(0);
        }
    }

}
