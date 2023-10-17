package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;

/**
 * Handles the events for terminal commands relating to plants.
 */
public class PlantCommandService {
    /**
     * Event handler to send and receive events between objects.
     */
    private final EventHandler events;

    /**
     * Constructor for the PlantCommandService.
     */
    public PlantCommandService() {
        this.events = new EventHandler();
    }

    /**
     * Getter for the event handler.
     * @return - event handler.
     */
    public EventHandler getEvents() {
        return this.events;
    }
}
