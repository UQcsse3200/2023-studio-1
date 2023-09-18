package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;

public class PlanetOxygenService implements OxygenLevel{
    
    private static final Logger logger = LoggerFactory.getLogger(PlanetOxygenService.class);
    private static final float DEFAULT_OXYGEN_GOAL = 1000;
    private static final float DEFAULT_INITIAL_OXYGEN = 100;
    
    private float oxygenGoal;
    private float oxygenPresent;
    private float delta;
    private final EventHandler eventHandler;
    
    public PlanetOxygenService() {
        oxygenGoal = DEFAULT_OXYGEN_GOAL;
        oxygenPresent = DEFAULT_INITIAL_OXYGEN;
        delta = 0;
        ServiceLocator.getTimeService().getEvents()
                .addListener("hourUpdate", this::update);
        eventHandler = new EventHandler();
    }
    
    @Override
    public void addOxygen(float kilogramsToAdd) {
        oxygenPresent += kilogramsToAdd;
    }
    
    @Override
    public void removeOxygen(float kilogramsToRemove) {
        oxygenPresent -= kilogramsToRemove;
    }
    
    @Override
    public float getOxygen() {
        return oxygenPresent;
    }
    
    @Override
    public int getOxygenPercentage() throws IllegalArgumentException {
        if (oxygenGoal > 0) {
            return (int) ((oxygenPresent / oxygenGoal) * 100);
        }
        // Error, should not occur
        return -1;
    }
    
    /**
     * Set the maximum/goal amount of oxygen to be present on the planet
     * @param kilograms
     */
    public void setOxygenGoal(int kilograms) throws IllegalArgumentException {
        if (kilograms <= 0) {
            throw new IllegalArgumentException("Goal cannot be 0 or negative");
        } else {
            oxygenGoal = kilograms;
        }
    }

    /**
     * Gets the maximum/goal amount of oxygen to be present on the planet.
     * @return kilograms of oxygen
     */
    public float getOxygenGoal() { return oxygenGoal; }
    
    /**
     * Gets the PlanetOxygenService's event handler
     * @return the event handler
     */
    public EventHandler getEvents() {
        return eventHandler;
    }
    
    /**
     * Getter for the default initial oxygen value.
     * @return the default initial/'starting' oxygen value.
     */
    public float getDefaultInitialOxygen() {
        return DEFAULT_INITIAL_OXYGEN;
    }
    
    /**
     * Perform the update on the oxygen present by applying the recalculated
     * hour delta. If level reaches 0, trigger lose screen. Triggers an event to
     * update the oxygen display.
     */
    public void update() {
        delta = calculateDelta();
        
        if (oxygenPresent + delta <= 0) {
            // No oxygen left - trigger lose screen.
            oxygenPresent = 0;
            eventHandler.trigger("oxygenUpdate");
            ServiceLocator.getGameArea().getPlayer().getEvents().trigger("loseScreen");
        } else if (oxygenPresent + delta > oxygenGoal) {
            // Limit the present oxygen to not surpass the oxygen goal.
            oxygenPresent = oxygenGoal;
        } else {
            oxygenPresent += delta;
        }
        eventHandler.trigger("oxygenUpdate");
    }
    
    /**
     * Iterate through the existing entity array and matches up each entity with
     * its corresponding oxygen value acquired from the EntityType enum. Then sums
     * the hourly oxygen rate of all existing entities to provide the hourly delta.
     * @return The calculated oxygen change for the hour.
     */
    private float calculateDelta() {
        // Calculated change in oxygen for the hour
        float calculatedDelta = 0;
        EntityType type;
        // Loop through existing entities in the game to sum their oxygen values.
        for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
            type = entity.getType();
            if (type != null) {
                // Loop through registered entity types for a matching type.
                for (EntityType enumType : EntityType.values()) {
                    if (type.equals(enumType)) {
                        calculatedDelta += entity.getType().getOxygenRate();
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
