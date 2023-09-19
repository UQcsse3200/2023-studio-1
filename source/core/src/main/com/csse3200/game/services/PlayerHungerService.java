package com.csse3200.game.services;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHungerService implements HungerLevel {
    private static final Logger logger = LoggerFactory.getLogger(PlayerHungerService.class);
    private static final float DEFAULT_HUNGER_GOAL = 1000;
    private static final float DEFAULT_INITIAL_HUNGER = 100;

    private float hungerGoal;
    private float hungerPresent;
    private float delta;
    private final EventHandler eventHandler;

    public PlayerHungerService() {
        hungerGoal = DEFAULT_HUNGER_GOAL;
        hungerPresent = DEFAULT_INITIAL_HUNGER;
        delta = 0;
//        ServiceLocator.getTimeService().getEvents()
//                .addListener("hourUpdate", this::update);
        eventHandler = new EventHandler();
    }

    @Override
    public void addHunger() {}

    @Override
    public void removeHunger() {}

    @Override
    public float getHunger() {return hungerPresent;}

    @Override
    public int getHungerPercentage() throws IllegalArgumentException {
        if (hungerGoal > 0) {
            return (int) ((hungerPresent / hungerGoal) * 100);
        }
        // Error, should not occur
        return -1;
    }

    public void setHungerGoal() throws IllegalArgumentException {}

    public float getOxygenGoal() { return hungerGoal; }


    public EventHandler getEvents() {
        return eventHandler;
    }

    /**
     * Getter for the default initial oxygen value.
     * @return the default initial/'starting' oxygen value.
     */
    public float getDefaultInitialHunger() {
        return DEFAULT_INITIAL_HUNGER;
    }

    /**
     * Perform the update on the oxygen present by applying the recalculated
     * hour delta. If level reaches 0, trigger lose screen. Triggers an event to
     * update the oxygen display.
     */
    public void update() {}

}
