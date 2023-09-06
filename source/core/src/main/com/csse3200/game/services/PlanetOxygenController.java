package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanetOxygenController implements OxygenLevel{
    
    private static final Logger logger = LoggerFactory.getLogger(PlanetOxygenController.class);
    
    private float oxygenUpperLimit;
    private float oxygenPresent = 0;
    private float delta;
    
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
            return oxygenPresent/oxygenUpperLimit;
        }
        return 0;
    }
    
    @Override
    public void registerContinuousAdd(float kilogramsPerHour) {
    
    
    }
    
    @Override
    public void registerContinuousConsumption(float kilogramsPerHour) {
    
    }
    
    @Override
    public void removeContinuousAdd(float kilogramsPerHour) {
    
    }
    
    @Override
    public void removeContinuousConsumption(float kilogramsPerHour) {
    
    }
}
