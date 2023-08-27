package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.services.GameTimeDisplay;
import com.csse3200.game.services.TimeController;

/** Controls the game time */
public class GameTime {
  private static Logger logger = LoggerFactory.getLogger(GameTime.class);
  private final long startTime;
  private float timeScale = 1f;

  private long pausedTime;
  public TimeController timeController;

  public GameTime() {

    this.timeController = new TimeController(this);
    //timeController.setTimeSource(this);
    this.pausedTime = 0;

    startTime = TimeUtils.millis();
    logger.debug("Setting game start time to {}", startTime);
  }

  public TimeController getTimeController() {
    return this.timeController;
  }

  public void addPauseOffset(long pauseDuration) {
    this.pausedTime += pauseDuration;
  }
  /**
   * Set the speed of time passing. This affects getDeltaTime()
   *
   * @param timeScale Time scale, where normal speed is 1.0, no time passing is 0.0
   */
  public void setTimeScale(float timeScale) {
    logger.debug("Setting time scale to {}", timeScale);
    this.timeScale = timeScale;
  }

  /** @return time passed since the last frame in seconds, scaled by time scale. */
  public float getDeltaTime() {
    return Gdx.graphics.getDeltaTime() * timeScale;
  }

  /** @return time passed since the last frame in seconds, not affected by time scale. */
  public float getRawDeltaTime() {
    return Gdx.graphics.getDeltaTime();
  }

  /** @return time passed since the game started in milliseconds */
  public long getTime() {
    return TimeUtils.timeSinceMillis(startTime);
  }

  public long getActiveTime() {
    return TimeUtils.timeSinceMillis(startTime) - pausedTime;
  }

  public long getTimeSince(long lastTime) {
    return getTime() - lastTime;
  }
}

