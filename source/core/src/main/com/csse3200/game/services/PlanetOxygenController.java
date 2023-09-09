package com.csse3200.game.services;

import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class PlanetOxygenController implements OxygenLevel{
    
    private static final Logger logger = LoggerFactory.getLogger(PlanetOxygenController.class);
    
    private float oxygenUpperLimit;
    private float oxygenPresent = 0;
    private Map<Entity, >entityOxygenValues = new HashMap();
    
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
    public void registerEntityOxygen(Entity entity, float hourlyOxygen) {
    
    }
    /**
     * Register a set amount of oxygen to be added to the object hourly.
     * @param kilogramsPerHour the number of kilograms to be added per hour.
     */
    public void registerContinuousAdd(float kilogramsPerHour) {
    
    
    }
    
    /**
     * Register a set amount of oxygen to be removed/consumed from the object, hourly.
     * @param kilogramsPerHour the number of kilograms to be consumed per hour.
     */
    public void registerContinuousConsumption(float kilogramsPerHour) {
    
    }
    
    /**
     * Remove/stop a set amount of oxygen being added to the object hourly.
     * @param kilogramsPerHour the number of kilograms to stop adding per hour.
     */
    public void removeContinuousAdd(float kilogramsPerHour) {
    
    }
    
    /**
     * Remove/stop a set amount of oxygen being consumed from the object hourly.
     * @param kilogramsPerHour the number of kilograms to stop consuming per hour.
     */
    public void removeContinuousConsumption(float kilogramsPerHour) {
    
    }
}
