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
     * Constructor for MultiDropComponent
     *
     * @param singleDropHandlers Handlers for each individual item being dropped
     */
    public MultiDropComponent(List<SingleDropHandler> singleDropHandlers) {
        this.singleDropHandlers = singleDropHandlers;
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
