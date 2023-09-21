package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;

public class PlantCommandService {
    private final EventHandler events;

    public PlantCommandService() {
        events = new EventHandler();
    }

    public EventHandler getEvents() {
        return events;
    }
}
