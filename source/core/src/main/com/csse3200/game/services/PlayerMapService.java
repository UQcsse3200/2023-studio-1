package com.csse3200.game.services;

import com.csse3200.game.events.EventHandler;

public class PlayerMapService {
    private final EventHandler eventHandler;

    public PlayerMapService() {
        eventHandler = new EventHandler();
    }

    public EventHandler getEvents() {
        return eventHandler;
    }

}


