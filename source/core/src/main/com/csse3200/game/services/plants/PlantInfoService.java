package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;

public class PlantInfoService {
    private final EventHandler events;

    private int alivePlantCount;
    private int decayingPlantCount;
    private int totalSeedsPlanted;
    private int cosmicCobSeedsPlanted;
    private int aloeVeraSeedsPlanted;
    private int hammerPlantSeedsPlanted;
    private int spaceSnapperSeedsPlanted;
    private int deadlyNightshadeSeedsPlanted;

    public PlantInfoService() {
        events = new EventHandler();
        alivePlantCount = 0;
        totalSeedsPlanted = 0;
        cosmicCobSeedsPlanted = 0;
        aloeVeraSeedsPlanted = 0;
        hammerPlantSeedsPlanted = 0;
        spaceSnapperSeedsPlanted = 0;
        deadlyNightshadeSeedsPlanted = 0;
    }

    public EventHandler getEvents() {
        return events;
    }

    public void increaseAlivePlantCount(int num) {
        alivePlantCount += num;
    }

    public void increaseDecayingPlantCount(int num) {
        decayingPlantCount += num;
    }

    public void increaseSeedsPlanted(int num, String plantName) {
        switch (plantName) {
            case "Cosmic Cob" -> cosmicCobSeedsPlanted += num;
            case "Aloe Vera" -> aloeVeraSeedsPlanted += num;
            case "Hammer Plant" -> hammerPlantSeedsPlanted += num;
            case "Space Snapper" -> spaceSnapperSeedsPlanted += num;
            case "Deadly Nightshade" -> deadlyNightshadeSeedsPlanted += num;
        }

        totalSeedsPlanted = cosmicCobSeedsPlanted +
                            aloeVeraSeedsPlanted +
                            hammerPlantSeedsPlanted +
                            spaceSnapperSeedsPlanted +
                            deadlyNightshadeSeedsPlanted;
    }

    public String plantInfoSummary() {
        String returnString = "";

        if (alivePlantCount != 0) {
            returnString += "\nAlive Plants: " + String.valueOf(alivePlantCount);
        }

        if (decayingPlantCount != 0) {
            returnString += "\nDecaying Plants: " + String.valueOf(decayingPlantCount);
        }

        return returnString;
    }
}
