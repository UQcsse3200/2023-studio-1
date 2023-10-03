package com.csse3200.game.services;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHungerService {
    private static final Logger logger = LoggerFactory.getLogger(PlayerHungerService.class);
    private float hungerGoal;
    private float hungerPresent;
    private float delta;
    private final EventHandler eventHandler;

    public PlayerHungerService() {
        eventHandler = new EventHandler();
    }


    public EventHandler getEvents() {
        return eventHandler;
    }


}
