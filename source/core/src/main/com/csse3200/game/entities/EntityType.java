package com.csse3200.game.entities;

public enum EntityType {
    Player(0),
    Tractor(0),
    Plant(1),
    Tile(0),  // This is team 7 stuff
    Cow(-2),
    Chicken(-1),
    Axolotl(10),
    OxygenEater(-4),
    Item(0);
    
    // Negative rate for consumption, positive for production of oxygen
    private final float hourlyOxygenRate;
    
    EntityType(float hourlyOxygenRate) {
        this.hourlyOxygenRate = hourlyOxygenRate;
    }
    
    /**
     * Getter method for the oxygen consumption/production rate
     * of a given entity type.
     * @return the hourly oxygen rate of the entity type.
     */
    public float getOxygenRate() {
        return hourlyOxygenRate;
    }
}

