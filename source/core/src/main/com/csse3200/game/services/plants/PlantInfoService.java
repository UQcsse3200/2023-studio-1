package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;
import jdk.jfr.Event;

/**
 * Keeps track of all info and stats relating to plants.
 */
public class PlantInfoService {
    /**
     * Event handler to send and receive events between objects.
     */
    private EventHandler events;

    /**
     * Count of the total number of plants that are currently alive.
     */
    private int alivePlantCount;

    /**
     * Count of the total number of plants that are currently decaying.
     */
    private int decayingPlantCount;

    /**
     * Count of the total number of seeds planted.
     */
    private int totalSeedsPlanted;

    /**
     * Count of the total number of plant seeds counted.
     * Indexes 0-5 correspond to: Cosmic Cob, Aloe Vera, Hammer Plant, Space Snapper, Deadly Nightshade, Atomic Algae.
     */
    private int[] seedPlantCount = {0, 0, 0, 0, 0, 0};

    /**
     * The total number of plants that have been harvested.
     */
    private int totalPlantHarvestCount;

    /**
     * Number of times each plant type has been harvested.
     * Indexes 0-5 correspond to: Cosmic Cob, Aloe Vera, Hammer Plant, Space Snapper, Deadly Nightshade, Atomic Algae.
     */
    private int[] plantHarvestCount = {0, 0, 0, 0, 0, 0};

    /**
     * Constructor for the plant info service. Initialise all counts to zero.
     */
    public PlantInfoService() {
        this.events = new EventHandler();
        this.alivePlantCount = 0;
        this.totalSeedsPlanted = 0;
        this.totalPlantHarvestCount = 0;
        this.decayingPlantCount = 0;
    }

    /**
     * Getter for the event handler.
     * @return the event handler.
     */
    public EventHandler getEvents() {
        return this.events;
    }

    /**
     * Setter for the event handler. Used for testing
     * @param events the event handler.
     */
    public void setEvents(EventHandler events) {
        this.events = events;
    }

    /**
     * Gets the number of alive plants
     * @return the number of alive plants
     */
    public int getAlivePlantCount() {
        return this.alivePlantCount;
    }

    /**
     * Gets the total number of seeds planted
     * @return the total number of seeds planted
     */
    public int getTotalSeedsPlanted() {
        return this.totalSeedsPlanted;
    }

    /**
     * Gets the total number plants harvested
     * @return the total number of plants harvested
     */
    public int getTotalPlantHarvestCount() {
        return this.totalPlantHarvestCount;
    }

    /**
     * Gets the total number of decaying plants
     * @return the total number of decaying plants
     */
    public int getDecayingPlantCount() {
        return this.decayingPlantCount;
    }

    /**
     * Sets the decaying plant counter to the number provided
     * @param num the number to set the decaying plant counter to
     */
    public void setDecayingPlantCount(int num) {
        decayingPlantCount = num;
    }

    /**
     * Increase the number of plants at a certain growth stage.
     * @param num - Integer value.
     * @param growthStage - the growth stage of the plant.
     */
    public void increasePlantGrowthStageCount(int num, String growthStage) {
        switch (growthStage) {
            case "alive" -> alivePlantCount += num;
            case "decay" -> decayingPlantCount += num;
            default -> {
                // Default case should do nothing
            }
        }
        updateClearInfo();
    }

    /**
     * Increase the number of seeds planted by an integer value. Also takes the name of the plant that
     * has been planted.
     * @param num - Integer value.
     * @param plantName - Name of a valid plant.
     */
    public void increaseSeedsPlanted(int num, String plantName) {
        switch (plantName) {
            case "Cosmic Cob" -> seedPlantCount[0] += num;
            case "Aloe Vera" -> seedPlantCount[1] += num;
            case "Hammer Plant" -> seedPlantCount[2] += num;
            case "Space Snapper" -> seedPlantCount[3] += num;
            case "Deadly Nightshade" -> seedPlantCount[4] += num;
            case "Atomic Algae" -> seedPlantCount[5] += num;
            default -> {
                // Default case should do nothing
            }
        }
        int sum = 0;
        for (int i : seedPlantCount) {
            sum += i;
        }
        totalSeedsPlanted = sum;
        updateClearInfo();
    }

    /**
     * Increase the total count of plants harvested by an integer value. Also keeps track of how many of each type of
     * plant have been harvested.
     * @param num - Integer value.
     * @param plantName - Name of the plant that has been harvested.
     */
    public void increasePlantsHarvested(int num, String plantName) {
        switch (plantName) {
            case "Cosmic Cob" -> plantHarvestCount[0] += num;
            case "Aloe Vera" -> plantHarvestCount[1] += num;
            case "Hammer Plant" -> plantHarvestCount[2] += num;
            case "Space Snapper" -> plantHarvestCount[3] += num;
            case "Deadly Nightshade" -> plantHarvestCount[4] += num;
            case "Atomic Algae" -> plantHarvestCount[5] += num;
            default -> {
                // Default case should do nothing
            }
        }
        int sum = 0;
        for (int i : plantHarvestCount) {
            sum += i;
        }
        totalPlantHarvestCount = sum;
        updateClearInfo();
    }

    /**
     * Trigger the 'clearInfo' event to update the plant info ui.
     */
    public void updateClearInfo() {
        events.trigger("clearPlantInfo");
    }

    /**
     * Return a formatted string with all relevant information about the plants.
     * @return - formatted string with relevant information about plants.
     */
    public String plantInfoSummary() {
        String returnString = "Total Seeds Planted: " + totalSeedsPlanted;

        if (totalPlantHarvestCount != 0) {
            returnString += "\nPlants Harvested: " + totalPlantHarvestCount;
         }

        if (alivePlantCount != 0) {
            returnString += "\nAlive Plants: " + alivePlantCount;
        }

        if (decayingPlantCount != 0) {
            returnString += "\nDecaying Plants: " + decayingPlantCount;
        }

        return returnString;
    }
}
