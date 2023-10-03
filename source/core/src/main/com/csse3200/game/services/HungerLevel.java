package com.csse3200.game.services;

/**
 * Used to adjust and check hunger levels of the player.
 */
public interface HungerLevel {

    /**
     * Add x hunger to the player in the game.
     */
    void addHunger();

    /**
     * Remove x Hunger from the player in the game.
     */
    void removeHunger();

    /**
     * Gets the amount of Hunger the player is at a current time in the game.
     */
    float getHunger();

    /**
     * Gets the Hunger level as a percentage of the upper limit (Float 0.0 - 100.0).
     */
    int getHungerPercentage();
}
