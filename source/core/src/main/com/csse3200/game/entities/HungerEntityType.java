package com.csse3200.game.entities;

public enum HungerEntityType {
    Player(20),
    Tractor(0),
    Plant(0),
    Tile(0),
    Cow(0),
    Chicken(0),
    Astrolotl(0),
    OxygenEater(0),
    Item(0),
    Questgiver(0),
    QuestgiverIndicator(0),
    Chest(0),
    Fence(0),
    Gate(0),
    Sprinkler(0);


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
