package com.csse3200.game.services;

import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHungerService {
    private final EventHandler eventHandler;

    public PlayerHungerService() {
        eventHandler = new EventHandler();
    }

    public EventHandler getEvents() {
        return eventHandler;
    }

}
