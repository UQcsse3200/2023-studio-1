package com.csse3200.game.entities;

import com.csse3200.game.components.placeables.PlaceableCategory;

/**
 * An enum of all the entity types in the game.
 * Feel free to add yours here. Read Documentation for SaveLoad and follow procedure there
 *
 * Changing order of EntityTypes will cause a failure in ManageHostilesQuestTest as the type
 * order is reliant on the enum class.
 */
public enum EntityType {
    PLAYER(0),
    TRACTOR(0),
    PLANT(10),
    DECAYING_PLANT(-10),
    TILE(0),  // This is team 7 stuff
    COW(0),
    CHICKEN(0),
    ASTROLOTL(0),
    OXYGEN_EATER(-10),
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


