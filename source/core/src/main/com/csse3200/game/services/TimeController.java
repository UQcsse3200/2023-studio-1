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

    /** The service for the game time*/
    private GameTime timeSource;

    /** The UI implementation of the clock */
    private GameTimeDisplay timeDisplay;

    /** The current hour in the game*/
    private int hour;

    private boolean paused;
    private long pausedAt;

    public TimeController() {
        this.hour = 0;
        this.paused = false;
    }

    public void setTimeSource(GameTime timeSource) {
        this.timeSource = timeSource;
    }

    public void setTimeDisplay(GameTimeDisplay timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    public int getTimeInSeconds() {
        return (int) timeSource.getActiveTime() / 1000;
    }

    public int getTimeOfDay() {
        return (int) timeSource.getActiveTime() % 720000;
    }

    public int getHour() {
        return (int) Math.floor(getTimeOfDay() / 30000);
    }

    public void update() {
        if (paused == false) {
            /** Each day is 12minutes so 720000 milliseconds is one day */
            int timeInDay = (int) timeSource.getActiveTime() % 720000;

            /** 30 seconds is each hour so 30000 is one hour */
            this.hour = (int) Math.floor(timeInDay / 30000);

            timeDisplay.update(this.hour);
        }
    }

    public void pause() {
        if (paused == false) {
            this.paused = true;
            this.pausedAt = timeSource.getTime();
        }
    }

    public void unpause() {
        if (pausede == true) {
            this.paused = false;
            timeSource.addPauseOffset(timeSource.getTimeSince(this.pausedAt));
        }
    }

    public void setTime(int hour) {
        this.hour = hour;
        timeDisplay.update(this.hour);
    }
}