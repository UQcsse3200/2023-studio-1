package com.csse3200.game.services;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeService {
	private static final Logger logger = LoggerFactory.getLogger(TimeService.class);
	private static final int INITIAL_CAPACITY = 16;
	private static final String HOUR_UPDATE = "hourUpdate";
	private static final String DAY_UPDATE = "dayUpdate";
	private static final int MS_IN_HOUR = 1000; //30000;
	private final Array<Entity> hourUpdateEntities = new Array<>(false, INITIAL_CAPACITY);
	private final Array<Entity> dayUpdateEntities = new Array<>(false, INITIAL_CAPACITY);
	private int hour;
	private int day;
	private long timeBuffer;
	private long lastGameTime;
	private boolean paused;


	public TimeService() {
		hour = 0;
		day = 0;
		paused = false;
		lastGameTime = ServiceLocator.getTimeSource().getTime();
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean state) {
		paused = state;
		if (state == true) {
			ServiceLocator.getTimeSource().setTimeScale(0);
		} else {
			ServiceLocator.getTimeSource().setTimeScale(1);
		}
	}

	public int getHour() {
		return hour;
	}

	public int getDay() {
		return day;
	}
	public void setHour(int hour) {
		this.hour = hour;
		this.timeBuffer = 0;
		triggerHourUpdate();
	}

	public void setDay(int day) {
		this.day = day;
		this.timeBuffer= 0;
		triggerDayUpdate();
	}

	public void registerHourUpdate(Entity entity) {
		logger.debug("Registering {} for hourly updates", entity);
		hourUpdateEntities.add(entity);
	}
	public void registerDayUpdate(Entity entity) {
		logger.debug("Registering {} for daily updates", entity);
		dayUpdateEntities.add(entity);
	}
	public void unregisterHourUpdate(Entity entity) {
		logger.debug("Unregistering {} for hourly updates", entity);
		hourUpdateEntities.removeValue(entity, true);
	}
	public void unregisterDayUpdate(Entity entity) {
		logger.debug("Unregistering {} for daily updates", entity);
		dayUpdateEntities.removeValue(entity, true);
	}
	private void triggerHourUpdate() {
		for (Entity entity: hourUpdateEntities) {
			entity.getEvents().trigger(HOUR_UPDATE);
		}
	}

	private void triggerDayUpdate() {
		for (Entity entity: dayUpdateEntities) {
			entity.getEvents().trigger(DAY_UPDATE);
		}
	}
	public void update() {
		// this time will be in ms
		long timePassed = ServiceLocator.getTimeSource().getTimeSince(lastGameTime);
		lastGameTime = ServiceLocator.getTimeSource().getTime();
		if (paused) {
			return;
		}
		timeBuffer += timePassed;
		// If time elapsed isn't one hour in the game, do nothing
		if (timeBuffer < MS_IN_HOUR) {
			return;
		}
		hour += 1;
		timeBuffer -= MS_IN_HOUR;

		// If hour is between 0 and 23, day hasn't elapsed, do nothing
		if (hour < 24) {
			triggerHourUpdate();
			return;
		}
		hour -= 24;
		triggerHourUpdate();
		day += 1;
		triggerDayUpdate();
	}

}
