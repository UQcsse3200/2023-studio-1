package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.services.GameTimeDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Repsonsible for controlling and updating classes dependent on the time
 */
public class TimeController {

    /** The current registered entities */
    private List<Entity> entities = new ArrayList<>();

    /** The current GameTime service */
    private GameTime timeSource;

    /** Clock UI dependent on time*/
    private GameTimeDisplay timeDisplay;

    /** The current hour in the game*/
    private int hour;

    /** pause status */
    private boolean paused;

    /** Time that the game was last paused at */
    private long pausedAt;

    /**
     * Constructor used to define initial states for the TimeController class and link to parent GameTime service
     *
     * @param timeSource: The current GameTime service for this instance
     */
    public TimeController(GameTime timeSource) {
        this.timeSource = timeSource;
        // initally the game is not paused
        this.hour = 0;
        this.paused = false;
    }

    /**
     * Add an entity observer to the list of entities
     *
     * @param entity: entity that you want to add as an observer
     */
    public void register(Entity entity) {
        entities.add(entity);
    }

    /**
     * Remove an entity observer from the list of entities
     *
     * @param entity: entity that you want to remove from the list
     */
    public void remove(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Notify the entities to update
     */
    public void update() {

    }

    /**
     * Logs the current time UI under this TimeController
     *
     * @param timeDisplay: The class that controls the clock and time display
     */
    public void registerTimeDisplay(GameTimeDisplay timeDisplay) {
        this.timeDisplay = timeDisplay;
    }


    /**
     *  Updates the clock and time display by calling the update() method in GameTimeDisplay
     */
    public void updateDisplay() {
        // If the game is paused there is no reason to update the UI
        if (paused == false) {
            // Each day is 12minutes so 720000 milliseconds is one day
            int timeInDay = (int) timeSource.getActiveTime() % 720000;
            // 30 seconds is each hour so 30000 is one hour
            this.hour = (int) Math.floor(timeInDay / 30000);

            timeDisplay.update(this.hour);
        }
    }

    /**
     * @return the active time of the game in seconds
     */
    public int getTimeInSeconds() {
        return (int) timeSource.getActiveTime() / 1000;
    }

    /**
     * @return the time of the day in milliseconds
     */
    public long getTimeOfDay() {
        return timeSource.getActiveTime() % 720000;
    }

    /**
     * @return the current hour of the game
     */
    public int getHour() {

        this.hour = (int) Math.floor(timeSource.getActiveTime() / 30000);

        return this.hour;
    }

    /**
     * Pauses the game
     */
    public void pause() {
        // Wont pause if its already paused
        if (paused == false) {
            this.paused = true;
            this.pausedAt = timeSource.getTime();
        }
    }

    /**
     * Unpauses the game
     */
    public void unpause() {
        // Wont unpause if not paused
        if (paused == true) {
            this.paused = false;
            // Calculates the difference between the current time and the time that the game was paused at
            // to get the pause length.
            // Adds that to the pause offset in gameTime which will be subtracted from the total game time
            // to get active time.
            timeSource.addPauseOffset(timeSource.getTimeSince(this.pausedAt));
        }
    }

    // not finished
    // Sets the game time and display to a specific time
    public void setTime(int hour) {

        // calculates the current day as an integer using Math.floor(timeSource.getActiveTime() / 720000)
        // then calculates the time up until the current day by * 720000
        // then calculates the time up until the desired hour by doing hour * 30000
        long desiredTime = (long) Math.floor(timeSource.getActiveTime() / 720000) * 720000 + hour * 30000;

        this.hour = hour;

    }
}