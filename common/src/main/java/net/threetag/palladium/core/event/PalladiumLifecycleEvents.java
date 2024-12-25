package net.threetag.palladium.core.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.Nullable;

public interface PalladiumLifecycleEvents {

    /**
     * @see DataPackSync#onDataPackSync(PlayerList, ServerPlayer)
     */
    Event<DataPackSync> DATA_PACK_SYNC = EventFactory.createLoop();

    @FunctionalInterface
    interface DataPackSync {

        /**
         * Fires when a player joins the server or when the reload command is ran, before tags and crafting recipes are sent to the client. Send datapack data to clients when this event fires.
         *
         * @param playerList List of players
         * @param player Player that the data is synced to. Null if reload was run
         */
        void onDataPackSync(PlayerList playerList, @Nullable ServerPlayer player);

    }

}
