package net.threetag.palladium.core.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.Entity;

public interface PalladiumEntityEvents {

    /**
     * @see Tick#entityTick(Entity)
     */
    Event<Tick> TICK_PRE = EventFactory.createLoop();

    /**
     * @see Tick#entityTick(Entity)
     */
    Event<Tick> TICK_POST = EventFactory.createLoop();

    @FunctionalInterface
    interface Tick {

        /**
         * Called during every tick of an entity, duh
         *
         * @param entity The entity.
         */
        void entityTick(Entity entity);

    }

}
