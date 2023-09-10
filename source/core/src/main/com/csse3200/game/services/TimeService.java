package com.csse3200.game.services;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.services.ServiceLocator;

public class TimeService {
	private static final Logger logger = LoggerFactory.getLogger(TimeService.class);

	private static final int MS_IN_MINUTE = 50;
	private static final int LOSE_DAY = 30;
	private int minute;
	private int hour;
	private int day;
	private long timeBuffer;
	private long lastGameTime;
	private boolean paused;
	private final EventHandler events;


	/**
	 * Constructs a basic TimeService instance to track the in-game time
	 */
	public TimeService() {
		hour = 0;
		day = 0;
		minute = 0;
		paused = false;
		lastGameTime = ServiceLocator.getTimeSource().getTime();
		events = new EventHandler();
	}

	/**
	 * Returns whether the game is paused or not
	 * @return boolean value representing whether the game is paused or not
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Changes the pause state of the game
	 * @param state boolean value for whether the game is paused or not
	 */
	public void setPaused(boolean state) {
		paused = state;
		ServiceLocator.getTimeSource().setTimeScale(state ? 0 : 1);
	}

	/**
	 * Gets the current in-game hour
	 * @return in-game hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * Gets the current in-game day
	 * @return in-game day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Gets the current in-game minute
	 * @return in-game minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * Sets the in-game hour to a certain value. Also updates the time buffer and triggers any necessary events
	 * @param hour in-game hour
	 */
	public void setHour(int hour) {
		this.hour = hour;
		this.timeBuffer = 0;
		events.trigger("hourUpdate");
	}

	/**
	 * Sets the in-game day to a certain value. Also updates the time buffer and triggers any necessary events
	 * @param day in-game day
	 */
	public void setDay(int day) {
		this.day = day;
		this.timeBuffer= 0;
		events.trigger("dayUpdate");
	}

	/**
	 * Sets the in-game minute to a certain value. Also updates the time buffer and triggers any necessary events
	 * @param minute in-game minute
	 */
	public void setMinute(int minute) {
		this.minute = minute;
		this.timeBuffer = 0;
		events.trigger("minuteUpdate");
	}

	/**
	 * Gets the event handler for the TimeService
	 * @return event handler
	 */
	public EventHandler getEvents() {
		return events;
	}

	/**
	 * Tracks the in-game time stored in the time service. This method is called in the main game loop. It calculates
	 * the time that has passed since it last checked the time and calculates whether in-game time has elapsed.
	 */
	public void update() {
		// this time will be in ms
		long timePassed = ServiceLocator.getTimeSource().getTimeSince(lastGameTime);
		lastGameTime = ServiceLocator.getTimeSource().getTime();
		if (paused) {
			return;
		}
		timeBuffer += timePassed;

		if (timeBuffer < MS_IN_MINUTE) {
			return;
		}
		minute += 1;
		timeBuffer -= MS_IN_MINUTE;

		// If minute is between 0 and 59, hour hasn't elapsed - don't do anything
		if (minute < 60) {
			events.trigger("minuteUpdate");
			return;
		}
		hour += 1;
		minute -= 60;
		events.trigger("minuteUpdate");

		// If hour is between 0 and 23, day hasn't elapsed, do nothing
		if (hour < 24) {
			events.trigger("hourUpdate");
			return;
		}
		hour -= 24;
		day += 1;
		// This event has to be triggered after the hour is checked the hour isn't 24 when the event is sent
		events.trigger("hourUpdate");
		events.trigger("dayUpdate");

		// lose the game if the game reaches 30 days
		if (day >= LOSE_DAY) {
			ServiceLocator.getGameArea().getPlayer().getEvents().trigger("loseScreen");
		}
	}

}
