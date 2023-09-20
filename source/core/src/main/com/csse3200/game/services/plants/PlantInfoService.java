package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;

public class PlantInfoService {
    private final EventHandler events;

    public PlantInfoService() {
        events = new EventHandler();
    }

    public EventHandler getEvents() {
        return events;
    }
}
