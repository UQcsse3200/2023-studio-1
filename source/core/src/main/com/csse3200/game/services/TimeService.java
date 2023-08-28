package com.csse3200.game.services;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeService {
	private static final Logger logger = LoggerFactory.getLogger(TimeService.class);
	private static final int INITIAL_CAPACITY = 16;
	private static final String HOUR_UPDATE = "hourUpdate";
	private static final String DAY_UPDATE = "dayUpdate";
	private final Array<Entity> hourUpdateEntities = new Array<>(false, INITIAL_CAPACITY);
	private final Array<Entity> dayUpdateEntities = new Array<>(false, INITIAL_CAPACITY);

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

}
