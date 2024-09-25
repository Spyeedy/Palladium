package net.threetag.palladium.neoforge;

import net.minecraft.world.level.storage.loot.LootPool;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.loot.LootTableModificationManager;

@EventBusSubscriber(modid = Palladium.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PalladiumEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLootTableLoad(LootTableLoadEvent e) {
        var mod = LootTableModificationManager.getInstance().getFor(e.getName());

        if (mod != null) {
            for (LootPool lootPool : mod.getLootPools()) {
                e.getTable().addPool(lootPool);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBreakSpeed(PlayerEvent.BreakSpeed e) {
        if (e.getEntity().getAttributes().hasAttribute(PalladiumAttributes.DESTROY_SPEED)) {
            e.setNewSpeed((float) (e.getNewSpeed() * e.getEntity().getAttributeValue(PalladiumAttributes.DESTROY_SPEED)));
        }
    }

}
