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
    
    public PlanetOxygenController() {
        oxygenUpperLimit = DEFAULT_MAX_OXYGEN;
        oxygenPresent = 0;
        entityOxygenUse = new HashMap();
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
    
    /**
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
}
