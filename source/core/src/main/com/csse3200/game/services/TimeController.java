package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.services.GameTimeDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameTime;

/** Class that is repsonsible updating classes depedent on the time*/
public class TimeController {

    /** Placeholder for when plants will eventually be added. Plant growth will be dependent on time*/
    // private List<Plant> activePlants = new ArrayList<>

    private GameTimeDisplay TimeDisplay;

    public void setGameTimeDisplay(GameTimeDisplay timeDisplay) {
        this.TimeDisplay = timeDisplay;
    }

    public void removeGameTimeDisplay() {
        this.TimeDisplay = null;
    }

    public void updateDisplay() {
        /** Will update the */
        this.TimeDisplay.update();
    }

    public void addPlant(/** Plant plant */) {
        // activePlants.add(Plant);
    }

    public void removePlant(/** Plant plant */) {
        // activePlants.remove(Plant)
    }

    public void notfiyPlants() {
        /**
         for(Plant plant : activePlants)
         {
            plant.update();
         }
         */
    }
}