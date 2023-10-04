package com.csse3200.game.entities;

public enum HungerEntityType {
    PLAYER(20),
    TRACTOR(0),
    PLANT(0),
    DECAYING_PLANT(0),
    TILE(0),  // This is team 7 stuff
    COW(0),
    CHICKEN(0),
    ASTROLOTL(0),
    OXYGEN_EATER(0),
    DRAGONFLY(0),
    BAT(0),
    ITEM(0),
    QUESTGIVER(0),
    QUESTGIVER_INDICATOR(0),
    CHEST(0),
    FENCE(0),
    GATE(0),
    SPRINKLER(0),
    PUMP(0),
    LIGHT(0),
    SHIP(0),
    SHIP_DEBRIS(0),
    SHIP_PART_TILE(0),
    DUMMY(0), // Used for testing
    FIRE_FLIES(0);


    // Negative rate for consumption, positive for production of hunger
    private final float hourlyHungerRate;

    HungerEntityType(float hourlyHungerRate) {
        this.hourlyHungerRate = hourlyHungerRate;
    }

    /**
     * Getter method for the hunger consumption/production rate
     * of a given entity type.
     *
     * @return the hourly oxygen rate of the entity type.
     */
    public float getHungerRate() {
        return hourlyHungerRate;
    }
}
