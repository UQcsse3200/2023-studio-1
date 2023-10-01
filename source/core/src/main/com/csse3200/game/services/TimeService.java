package com.csse3200.game.services;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.events.EventHandler;

public class TimeService {
	private static final Logger logger = LoggerFactory.getLogger(TimeService.class);
	private static final int MS_IN_MINUTE = 500;
	private static final int MORNING_HOUR = 6;
	private static final int NIGHT_HOUR = 20;

	private int minute;
	private int hour;
	private int day;

	private long timeBuffer;
	private boolean paused;
	private final EventHandler events;



	/**
	 * Constructs a basic TimeService instance to track the in-game time
	 */
	public TimeService() {
		hour = MORNING_HOUR;
		day = 0;
		minute = 0;
		paused = false;
		events = new EventHandler();
	}

	/**
	 * Returns whether the game is paused or not
	 *
	 * @return boolean value representing whether the game is paused or not
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Changes the pause state of the game
	 *
	 * @param state boolean value for whether the game is paused or not
	 */
	public void setPaused(boolean state) {
		paused = state;
		logger.debug("Setting paused state to: {}", paused);
		ServiceLocator.getTimeSource().setTimeScale(state ? 0 : 1);
	}

	/**
	 * Gets the current in-game hour
	 *
	 * @return in-game hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * Gets the current in-game day
	 *
	 * @return in-game day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Gets the current in-game minute
	 *
	 * @return in-game minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * Determines whether it is day or not
	 *
	 * @return whether it is day or not
	 */
	public boolean isDay() {
		return (hour >= MORNING_HOUR) && (hour < NIGHT_HOUR);
	}

	/**
	 * Determines whether it is night or not
	 *
	 * @return whether it is night or not
	 */
	public boolean isNight() {
		return !isDay();
	}

	/**
	 * Sets the in-game day to a certain value. Also updates the time buffer and triggers any necessary events
	 *
	 * @param day in-game day
	 */
	public void setDay(int day) {
		if (day < 0) {
			logger.warn("Incorrect day value given: {}", day);
			return;
		}
		logger.debug("Day is being set to: {}", this.day);
		this.day = day;
		this.timeBuffer = 0;
		events.trigger("dayUpdate");
	}

	/**
	 * Sets the in-game hour to a certain value. Also updates the time buffer and triggers any necessary events
	 *
	 * @param hour in-game hour
	 */
	public void setHour(int hour) {
		if (hour < 0 || hour > 23) {
			logger.warn("Incorrect hour value given: {}", hour);
			return;
		}
		logger.debug("Hour is being set to: {}", this.hour);
		this.hour = hour;
		this.timeBuffer = 0;
		events.trigger("hourUpdate");
	}

	/**
	 * Sets the in-game minute to a certain value. Also updates the time buffer and triggers any necessary events
	 *
	 * @param minute in-game minute
	 */
	public void setMinute(int minute) {
		if (minute < 0 || minute > 59) {
			logger.warn("Incorrect minute value given: {}", minute);
			return;
		}
		logger.debug("Minute is being set to: {}", this.minute);
		this.minute = minute;
		this.timeBuffer = 0;
		events.trigger("minuteUpdate");
	}

	/**
	 * Sets the in-game hour to the nearest future hour passed in, rounded to 0 minutes.
	 * Increments the day if necessary, updates the time buffer, and triggers any necessary events.
	 *
	 * @param hour in-game hour
	 */
	public void setNearestTime(int hour) {
		setNearestTime(hour, 0);
	}

	/**
	 * Sets the in-game hour and minute to the nearest future value passed in.
	 * Increments the day if necessary, updates the time buffer, and triggers any necessary events.
	 *
	 * @param hour in-game hour
	 * @param minute in-game minute
 	*/
	public void setNearestTime(int hour, int minute) {
		if (this.minute > minute) {
			this.hour += 1;
		}
		this.minute = minute;
		events.trigger("minuteUpdate");

		if (this.hour > hour) {
			this.day += 1;
			events.trigger("dayUpdate");
		}
		this.hour = hour;
		events.trigger("hourUpdate");

		this.timeBuffer = 0;

		logger.debug("Time is being set to: {}d, {}h, {}m", this.day, this.hour, this.minute);
	}

	/**
	 * Gets the event handler for the TimeService
	 *
	 * @return event handler
	 */
	public EventHandler getEvents() {
		return events;
	}

	/**
	 * Trigger relevant events once the hour has been updated.
	 */
	private void triggerHourEvents() {
		if (hour == MORNING_HOUR) {
			// Made this an event so other entities can listen
			logger.debug("Now night time");
			events.trigger("morningTime");
		} else if (hour == NIGHT_HOUR) {
			logger.debug("Now morning time");
			events.trigger("nightTime");
		}

		// Always trigger an hour update event
		events.trigger("hourUpdate");
	}

	/**
	 * Tracks the in-game time stored in the time service. This method is called in the main game loop. It calculates
	 * the time that has passed since it last checked the time and calculates whether in-game time has elapsed.
	 */
	public void update() {
		// this time will be in ms
		float timePassed = ServiceLocator.getTimeSource().getDeltaTime() * 1000;
		if (paused) {
			return;
		}
		timeBuffer += (long) timePassed;

		if (timeBuffer < MS_IN_MINUTE) {
			return;
		}

		int minutesPassed = (int) (timeBuffer / MS_IN_MINUTE);
		minute += minutesPassed;
		timeBuffer -= ((long) minutesPassed * MS_IN_MINUTE);

		// If minute is between 0 and 59, hour hasn't elapsed - don't do anything
		if (minute < 60) {
			events.trigger("minuteUpdate");
			return;
		}

		logger.debug("In-game hour has updated");

		int hoursPassed = minute / 60;
		hour += hoursPassed;
		minute -= (hoursPassed * 60);
		events.trigger("minuteUpdate");

		// If hour is between 0 and 23, day hasn't elapsed, do nothing
		if (hour < 24) {
			triggerHourEvents();
			return;
		}

		logger.debug("In-game day has updated");

		int daysPassed = hour / 24;
		day += daysPassed;
		hour -= (daysPassed * 24);
		triggerHourEvents();

		// This event has to be triggered after the hour is checked the hour isn't 24 when the event is sent
		events.trigger("dayUpdate");
	}

	public void loadTime(int day, int hour, int minute) {
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}
}
