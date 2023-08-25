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

    private GameTime timeSource;

    private GameTimeDisplay timeDisplay;

    /*
    public TimeController() {
        this.timeSource = ServiceLocator.getTimeSource();
    }
    */

    public void setTimeSource(GameTime timeSource) {
        this.timeSource = timeSource;
    }

    public void setTimeDisplay(GameTimeDisplay timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    public void update() {
        timeDisplay.update(5);
    }
}