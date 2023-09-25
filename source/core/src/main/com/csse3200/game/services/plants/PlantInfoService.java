package com.csse3200.game.services.plants;

import com.csse3200.game.events.EventHandler;

public class PlantInfoService {
    private final EventHandler events;

    private int alivePlantCount;

    public PlantInfoService() {
        events = new EventHandler();
        alivePlantCount = 0;
    }

    public EventHandler getEvents() {
        return events;
    }

    public void increaseAlivePlantCount(int num) {
        alivePlantCount += num;
    }

    public String plantInfoSummary() {
        String returnString = "";

        if (alivePlantCount != 0) {
            returnString += "\nAlive Plants: " + String.valueOf(alivePlantCount);
        }

        return returnString;
    }
}
