package com.csse3200.game.components.items;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.Component;

public class WateringCanLevelComponent extends Component {
    private float capacity;             //This will hold the limit value of the watering can
    private float currentLevel;         //This will hold the current amount of water in the watering can


    /**
     * Constructor that will set the watering can level values to default values
     *      -   The max the watering can will be set to 50
     *      -   The current amount of water in the can will be 0
     */
    public WateringCanLevelComponent() {
        capacity = 50; 
        currentLevel = capacity;
    }

    /**
     * Constructor with parameter to change the max level capacity
     * The current ammount will still begin at 0
     * 
     * @param newCapacity      Max capacity the water can will hold as a float
     */
    public WateringCanLevelComponent(float newCapacity) {
        capacity = newCapacity; 
        currentLevel = capacity;
    }

    /**
     * Get the private variable that represent the Max amount of water the 
     *  WateringCan can hold and return it
     * 
     * @return capacity
     */
    public float getCapacity(){
        return capacity;
    }

    /**
     * Update the max the WateringCan can hold to the parameter
     *  and keep the current level of water
     *  but adjust current level to max if the water in the can is greater than max capacity
     *
     * @param newCapacity
     */
    public void setCapacity(float newCapacity){
        capacity = newCapacity;

        if (currentLevel >= capacity) {
            currentLevel = capacity;    //if the current level is greater than the new capacity
                                        //then lower the current level to capacity
        }
    }

    /**
     * Return the current amount of water within the WateringCan
     * 
     * @return currentLevel
     */
    public float getCurrentLevel(){
        return currentLevel;
    }

    /**
     * Set the value that stores the current amount of water to the parameter 
     *    ---  e.g.    Set to Max,   or Reset back to 0
     * 
     * @param newAmount
     */
    public void setCurrentLevel(float newAmount){
        if (newAmount >= capacity){
            //if the given amount is greater than the capcity
            //then set the current level to capacity
            currentLevel = capacity;
            return;
        }

        //check the new value isn't less then zero
        // if it is then set the current level to 0
        if (newAmount < 0){
            currentLevel = 0;
            return;
        }

        currentLevel = newAmount;
    }

    /**
     * increment the current amount of water by the given amount of water
     *  check to see if the can is not at capacity
     * 
     * @param increment
     */
    public void incrementLevel(float increment){
        if ((currentLevel + increment) >= capacity){
            //if the increment will go over the capacity 
            //then set the current level to max and return
            currentLevel = capacity;
            return;
        }
        
        currentLevel += increment;

        //check to is the increment has set the water below 0
        //e.g. currentLevel = 5   and   increment = -10   ->   newLevel = -5
        //if this is the case set the value to 0
        if (currentLevel < 0){
            currentLevel = 0;
        }
    }

    /**
     * Fill the current level of water to the max that can be stored
     */
    public void fillToMax(){
        currentLevel = capacity;
    }

    /**
     * Remove all water from the watering can
     */
    public void empty(){
        currentLevel = 0;
    }
    
    /**
     * Check and return whether the wateringCan is full
     * @return
     */
    public boolean isFull() {
        if (currentLevel >= capacity) {
            return true;
        }
        return false;
    }
    
    /**
     * check and return whether the wateringCan is empty
     * @return 
     */
    public boolean isEmpty() {
        if (currentLevel <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void write(Json json) {
		json.writeObjectStart(this.getClass().getSimpleName());
        json.writeValue("level", getCurrentLevel());
    }

    @Override
    public void read(Json json, JsonValue jsonMap) {
		jsonMap = jsonMap.get(this.getClass().getSimpleName());
        currentLevel = jsonMap.getFloat("level");
    }
}