package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;

/**
 * Keeps track of all info and stats relating to plants.
 */
public class PlantInfoService {
    /**
     * Event handler to send and receive events between objects.
     */
    private final EventHandler events;

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
     * Count of the total number of cosmic cob seeds planted.
     */
    private int cosmicCobSeedsPlanted;

    /**
     * Count of the total number of aloe vera seeds planted.
     */
    private int aloeVeraSeedsPlanted;

    /**
     * Count of the total number of hammer plant seeds planted.
     */
    private int hammerPlantSeedsPlanted;

    /**
     * Count of the total number of space snapper seeds planted.
     */
    private int spaceSnapperSeedsPlanted;

    /**
     * Count of the total number of deadly nightshade seeds planted.
     */
    private int deadlyNightshadeSeedsPlanted;

    /**
     * Count of the total number of atomic algae seeds planted.
     */
    private int atomicAlgaeSeedsPlanted;

    /**
     * Constructor for the plant info service. Initialise all counts to zero.
     */
    public PlantInfoService() {
        events = new EventHandler();
        alivePlantCount = 0;
        totalSeedsPlanted = 0;
        cosmicCobSeedsPlanted = 0;
        aloeVeraSeedsPlanted = 0;
        hammerPlantSeedsPlanted = 0;
        spaceSnapperSeedsPlanted = 0;
        deadlyNightshadeSeedsPlanted = 0;
        atomicAlgaeSeedsPlanted = 0;
    }

    /**
     * Getter for the event handler.
     * @return the event handler.
     */
    public EventHandler getEvents() {
        return events;
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
            case "Cosmic Cob" -> cosmicCobSeedsPlanted += num;
            case "Aloe Vera" -> aloeVeraSeedsPlanted += num;
            case "Hammer Plant" -> hammerPlantSeedsPlanted += num;
            case "Space Snapper" -> spaceSnapperSeedsPlanted += num;
            case "Deadly Nightshade" -> deadlyNightshadeSeedsPlanted += num;
            case "Atomic Algae" -> atomicAlgaeSeedsPlanted += num;
        }

        totalSeedsPlanted = cosmicCobSeedsPlanted +
                            aloeVeraSeedsPlanted +
                            hammerPlantSeedsPlanted +
                            spaceSnapperSeedsPlanted +
                            deadlyNightshadeSeedsPlanted +
                            atomicAlgaeSeedsPlanted;
    }

    /**
     * Trigger the 'clearInfo' event to update the plant info ui.
     */
    private void updateClearInfo() {
        events.trigger("clearPlantInfo");
    }

    /**
     * Return a formatted string with all relevant information about the plants.
     * @return - formatted string with relevant information about plants.
     */
    public String plantInfoSummary() {
        String returnString = "Total Seeds Planted: " + String.valueOf(totalSeedsPlanted);

        if (alivePlantCount != 0) {
            returnString += "\nAlive Plants: " + String.valueOf(alivePlantCount);
        }

        if (decayingPlantCount != 0) {
            returnString += "\nDecaying Plants: " + String.valueOf(decayingPlantCount);
        }

        return returnString;
    }
}
