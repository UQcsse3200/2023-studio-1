package com.csse3200.game.services;

/**
 * Used to adjust and check oxygen levels.
 */
public interface OxygenLevel {
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
    int getOxygenPercentage();
}
