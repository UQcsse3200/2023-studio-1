package com.csse3200.game.components.npc;

import java.util.List;
import java.util.function.BiConsumer;

import com.csse3200.game.components.Component;
import com.csse3200.game.events.listeners.EventListener0;

/**
 * Component that enables animals to drop different items on different triggers.
 * Each item can be dropped at a different rate.
 */
public class MultiDropComponent extends Component {
    /**
     * List of handlers for dropping individual items
     */
    private final List<SingleDropHandler> singleDropHandlers;
    /**
     * True iff this component should be notified to despawn the entity
     * for death. False otherwise.
     */
    private final boolean handlesDeath;

    /**
     * Constructor for MultiDropComponent
     *
     * @param singleDropHandlers Handlers for each individual item being dropped
     * @param handlesDeath True iff this singleDropHandlers contains a 'death' trigger
     */
    public MultiDropComponent(List<SingleDropHandler> singleDropHandlers, boolean handlesDeath) {
        this.singleDropHandlers = singleDropHandlers;
        this.handlesDeath = handlesDeath;
    }

    /**
     * Returns whether this component handles death
     *
     * @return True iff the component handles death, false otherwise.
     */
    public boolean getHandlesDeath() {
        return handlesDeath;
    }


    @Override
    public void create() {
        for (SingleDropHandler singleDropHandler : singleDropHandlers) {
            singleDropHandler.setEntity(this.entity);
            BiConsumer<String, EventListener0> listener = singleDropHandler.getListener();
            String trigger = singleDropHandler.getTrigger();

            listener.accept(trigger, singleDropHandler::dropItem);
        }
    }
}
