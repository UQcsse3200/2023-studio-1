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
        ServiceLocator.getTimeService().getEvents()
                .addListener("hourUpdate", this::update);
        eventHandler = new EventHandler();
    }

    @Override
    public void addHunger(float addFoodHunger) {
        hungerPresent += addFoodHunger;
    }

    @Override
    public void removeHunger(float removeFoodHunger) {
        hungerPresent -= removeFoodHunger;
    }

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

    public void setHungerGoal(int foodHunger) throws IllegalArgumentException {
        if (foodHunger <= 0) {
            throw new IllegalArgumentException("Goal cannot be less than or equal to 0");
        } else {
            hungerGoal = foodHunger;
        }
    }

    public float getHungerGoal() { return hungerGoal; }


    public EventHandler getEvents() {
        return eventHandler;
    }

    /**
     * Getter for the default initial hunger value.
     * @return the default initial/'starting' hunger value.
     */
    public float getDefaultInitialHunger() {
        return DEFAULT_INITIAL_HUNGER;
    }

    /**
     * Perform the update on the hunger present by applying the recalculated
     * hour delta. If level reaches 0, trigger lose screen. Triggers an event to
     * update the hunger display.
     */
    public void update() {
        delta = calculateDelta();

        if (hungerPresent + delta <= 0) {

            hungerPresent = 0;
            eventHandler.trigger("hungerUpdate");
            ServiceLocator.getGameArea().getPlayer().getEvents().trigger("loseScreen");
        } else if (hungerPresent + delta > hungerGoal) {
            hungerPresent += hungerGoal;
        } else {
            hungerPresent += delta;
        }

        eventHandler.trigger("hungerUpdate");
    }

    private float calculateDelta() {
        float calculatedDelta = 0;
        EntityType type;

        for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
            type = entity.getType();
            if (type != null) {
                for (EntityType enumType : EntityType.values()) {
                    if (type.equals(enumType)) {
                        calculatedDelta += entity.getType().getOxygenRate();  // I am not able to change it even though I tried on making a whole new file
                        // Break from inner loop after first match as an entity
                        // should only have one type.
                        break;
                    }
                }
            }
        }
        return calculatedDelta;
    }
}
