package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanetOxygenLevel implements OxygenLevel{
    private static final Logger logger = LoggerFactory.getLogger(TimeService.class);
    
    @Override
    public void setUpperLimit(int kilograms) {
    
    }
    
    @Override
    public void addOxygen(float kilogramsToAdd) {
    
    }
    
    @Override
    public void removeOxygen(float kilogramsToRemove) {
    
    }
    
    @Override
    public float getOxygen() {
        return 0;
    }
    
    @Override
    public float getOxygenPercentage() {
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
