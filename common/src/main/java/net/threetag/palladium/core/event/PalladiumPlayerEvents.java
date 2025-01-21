package net.threetag.palladium.core.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface PalladiumPlayerEvents {

    /**
     * Fired when an Entity is started to be "tracked" by this player, usually when an entity enters a player's view distance.
     */
    Event<Tracking> START_TRACKING = EventFactory.createLoop();

    /**
     * Fired when an Entity is stopped to be "tracked" by this player, usually the client was sent a packet to destroy the entity at this point
     */
    Event<Tracking> STOP_TRACKING = EventFactory.createLoop();

    @FunctionalInterface
    interface Tracking {

        /**
         * @param tracker       Player that is tracking the given entity
         * @param trackedEntity Entity being tracked
         */
        void playerTracking(Player tracker, Entity trackedEntity);

    }

}
