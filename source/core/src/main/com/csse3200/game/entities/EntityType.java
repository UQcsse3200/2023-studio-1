package com.csse3200.game.entities;

import com.csse3200.game.components.placeables.PlaceableCategory;

/**
 * An enum of all the entity types in the game.
 * Feel free to add yours here. Read Documentation for SaveLoad and follow procedure there
 */
public enum EntityType {
    Player(0),
    Tractor(0),
    Plant(10),
    DecayingPlant(-10),
    Tile(0),  // This is team 7 stuff
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
    Sprinkler(0),
    Pump(0),
    Light(0),
    Ship(0),
    ShipDebris(0),
    FireFlies(0);


    // Negative rate for consumption, positive for production of oxygen
    private final float hourlyOxygenRate;

    /**
     *  A parent like category for Placeable types.
     *  Allows other Placeable entities to query if this is of the same placeable category.
     */
    private PlaceableCategory placeableCategory;

    EntityType(float hourlyOxygenRate) {
        this.hourlyOxygenRate = hourlyOxygenRate;
    }

    /**
     * Getter method for the oxygen consumption/production rate
     * of a given entity type.
     *
     * @return the hourly oxygen rate of the entity type.
     */
    public float getOxygenRate() {
        return hourlyOxygenRate;
    }

    /**
     * Getter for the placeableCategory
     * @return this placeableCategory
     */
    public PlaceableCategory getPlaceableCategory() {
        return placeableCategory;
    }

    /**
     * Setter for the placeableCategory
     * @param placeableCategory A category that encompasses the Placeable type.
     */
    public void setPlaceableCategory(PlaceableCategory placeableCategory) {
        this.placeableCategory = placeableCategory;
    }
}


