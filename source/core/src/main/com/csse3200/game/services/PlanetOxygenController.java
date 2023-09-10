package com.csse3200.game.services;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanetOxygenController implements OxygenLevel{
    
    private static final Logger logger = LoggerFactory.getLogger(PlanetOxygenController.class);
    private static final float DEFAULT_MAX_OXYGEN = 10000;
    
    private float oxygenUpperLimit;
    private float oxygenPresent;
    private float delta;
    
    public PlanetOxygenController() {
        oxygenUpperLimit = DEFAULT_MAX_OXYGEN;
        oxygenPresent = 0;
        delta = 0;
        ServiceLocator.getTimeService().getEvents()
                .addListener("hourUpdate", this::update);
    }
    
    @Override
    public void setUpperLimit(int kilograms) {
        oxygenUpperLimit = kilograms;
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
    public float getOxygenPercentage() {
        if (oxygenUpperLimit > 0) {
            return oxygenPresent / oxygenUpperLimit;
        }
        return -1;
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
            // TODO update oxygen display and maybe wait 1 second before triggering losescreen
            oxygenPresent = 0;
            ServiceLocator.getGameArea().getPlayer().getEvents().trigger("loseScreen");
        } else {
            oxygenPresent += delta;
        }
        // TODO update oxygen display
    }
    
    /**
     * Iterated through the existing entity array and matches up each entity with
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
        return calculatedDelta;
    }
}
