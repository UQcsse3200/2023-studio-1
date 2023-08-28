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
	private final GameTime gameTime;
	private boolean paused;


	public TimeService() {
		hour = 0;
		day = 0;
		paused = false;
		gameTime = new GameTime();
		lastGameTime = gameTime.getTime();
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean state) {
		paused = state;
	}

	public int getHour() {
		return hour;
	}

	public int getDay() {
		return day;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setDay(int day) {
		this.day = day;
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
			entity.getEvents().trigger(HOUR_UPDATE, getHour());
		}
	}

	private void triggerDayUpdate() {
		for (Entity entity: dayUpdateEntities) {
			entity.getEvents().trigger(DAY_UPDATE, getDay());
		}
	}
	public void update() {
		// this time will be in ms
		long timePassed = gameTime.getTimeSince(lastGameTime);
		lastGameTime = gameTime.getTime();
		if (paused) {
			return;
		}
		timeBuffer += timePassed;
		// If time elapsed isn't one hour in the game, do nothing
		if (timeBuffer < MS_IN_HOUR) {
			return;
		}
		timeBuffer -= MS_IN_HOUR;
		hour += 1;
		System.out.printf("ADVANCE AN HOUR %s%n", getHour());
		timeBuffer -= MS_IN_HOUR;
		triggerHourUpdate();

		// If hour is between 0 and 23, day hasn't elapsed, do nothing
		if (hour < 24) {
			return;
		}
		hour -= 24;
		day += 1;
		System.out.printf("ADVANCE A DAY %s%n", getDay());
		triggerDayUpdate();
	}

}
