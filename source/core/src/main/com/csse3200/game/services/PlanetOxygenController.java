package com.csse3200.game.services;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class PlanetOxygenController implements OxygenLevel{
    
    private static final Logger logger = LoggerFactory.getLogger(PlanetOxygenController.class);
    private static final float DEFAULT_MAX_OXYGEN = 10000;
    
    private float oxygenUpperLimit;
    private float oxygenPresent;
    private Map<EntityType, Float> entityOxygenUse;
    private float delta;
    
    public PlanetOxygenController() {
        oxygenUpperLimit = DEFAULT_MAX_OXYGEN;
        oxygenPresent = 0;
        entityOxygenUse = new HashMap();
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
    
    /** (OUTDATED)
     * Register an entity type for it to affect the planet's oxygen on
     * an hourly basis. For both entities which create or consume oxygen
     * at a constant rate, e.g. plants or animals or combustion-related
     * entities.
     * If the entity type has already been registered, calling the method
     * again for the registered type will not change the initial value to
     * prevent accidental overwriting.
     * Instead, call the updateEntityType() method.
     * @param entityType the type of entity being registered to consume or
     *                   create oxygen.
     * @param hourlyOxygen the hourly oxygen delta of the entity type.
     *                     Positive for entities creating oxygen, and
     *                     negative for those consuming oxygen.
     */
    public void registerEntityType(EntityType entityType, float hourlyOxygen) {
        if (!entityOxygenUse.containsKey(entityType)) {
            // If the hashmap does not already contain a value for the
            // type, then add the type, and it's value.
            entityOxygenUse.put(entityType, hourlyOxygen);
        }
    }
    
    /** (OUTDATED)
     * Update the oxygen value for an already registered entity type.
     * If entity is not already registered the call will do nothing.
     * To register a new entity type and value use the
     * registerEntityType() method.
     * @param entityType the previously registered type to have its
     *                   value updated.
     * @param newHourlyOxygen the new value.
     */
    public void updateEntityType(EntityType entityType, float newHourlyOxygen) {
        if (entityOxygenUse.containsKey(entityType)) {
            // If the hashmap contains the key for the
            // type, then update its value.
            entityOxygenUse.put(entityType, newHourlyOxygen);
        }
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
                if (type == enumType) {
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
