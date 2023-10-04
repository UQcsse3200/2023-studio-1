package com.csse3200.game.services;

import com.csse3200.game.events.EventHandler;


public class PlayerHungerService {
    private final EventHandler eventHandler;

    public PlayerHungerService() {
        eventHandler = new EventHandler();
    }

    public EventHandler getEvents() {
        return eventHandler;
    }

}
