package com.csse3200.game.services;

/**
 * Used to adjust and check oxygen levels.
 */
public interface OxygenLevel {
    
    /**
     * Sets the maximum amount of oxygen that can be present.
     */
    void setUpperLimit(int kilograms);
    
    /**
     * Add x kilograms of oxygen to the object in question.
     * @param kilogramsToAdd the float number of kilograms to add.
     */
    void addOxygen(float kilogramsToAdd);
    
    /**
     * Remove x kilograms of oxygen from the object in question.
     * @param kilogramsToRemove the float number of kilograms to remove.
     */
    void removeOxygen(float kilogramsToRemove);
    
    /**
     * Gets the amount of oxygen present in the object in kilos.
     * @return the amount of oxygen present in kilos.
     */
    float getOxygen();
    
    /**
     * Gets the oxygen present as a percentage of the upper limit (Float 0.0 - 100.0).
     * @return The oxygen present as a percentage of the upper limit.
     */
    float getOxygenPercentage();
    
    /**
     * Register a set amount of oxygen to be added to the object hourly.
     * @param kilogramsPerHour the number of kilograms to be added per hour.
     */
    void registerContinuousAdd(float kilogramsPerHour);
    
    /**
     * Register a set amount of oxygen to be removed/consumed from the object, hourly.
     * @param kilogramsPerHour the number of kilograms to be consumed per hour.
     */
    void registerContinuousConsumption(float kilogramsPerHour);
    
    /**
     * Remove/stop a set amount of oxygen being added to the object hourly.
     * @param kilogramsPerHour the number of kilograms to stop adding per hour.
     */
    void removeContinuousAdd(float kilogramsPerHour);
    
    /**
     * Remove/stop a set amount of oxygen being consumed from the object hourly.
     * @param kilogramsPerHour the number of kilograms to stop consuming per hour.
     */
    void removeContinuousConsumption(float kilogramsPerHour);
}
