package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;

public class PlanetOxygenService implements OxygenLevel {

	private static final Logger logger = LoggerFactory.getLogger(PlanetOxygenService.class);
	private static final float DEFAULT_OXYGEN_GOAL = 10000;
	private static final float DEFAULT_INITIAL_OXYGEN = 1000;
	private static final String OXYGEN_UPDATE = "oxygenUpdate";

	private float oxygenGoal;
	private float oxygenPresent;
	private float delta;
	private final EventHandler eventHandler;

	public PlanetOxygenService() {
		logger.debug("Setting oxygen goal to {}", DEFAULT_OXYGEN_GOAL);
		oxygenGoal = DEFAULT_OXYGEN_GOAL;
		logger.debug("Setting initial oxygen level to {}", DEFAULT_INITIAL_OXYGEN);
		oxygenPresent = DEFAULT_INITIAL_OXYGEN;
		delta = 0;
		logger.debug("Adding oxygen listener to hourUpdate event");
		ServiceLocator.getTimeService().getEvents()
				.addListener("hourUpdate", this::update);
		eventHandler = new EventHandler();
	}

	@Override
	public void addOxygen(float kilogramsToAdd) {
		logger.debug("Adding {} kilograms to {}", kilogramsToAdd, oxygenPresent);
		oxygenPresent += kilogramsToAdd;
	}

	@Override
	public void removeOxygen(float kilogramsToRemove) {
		logger.debug("Removing {} kilograms from {}", kilogramsToRemove, oxygenPresent);
		oxygenPresent -= kilogramsToRemove;
	}

	@Override
	public float getOxygen() {
		return oxygenPresent;
	}

	@Override
	public void setOxygen(float oxygen) {
		this.oxygenPresent = oxygen;
		eventHandler.trigger(OXYGEN_UPDATE);
	}

	@Override
	public int getOxygenPercentage() throws IllegalArgumentException {
		if (oxygenGoal > 0) {
			logger.debug("Calculating {} as a percentage of the oxygen goal", oxygenPresent);
			return (int) ((oxygenPresent / oxygenGoal) * 100);
		}
		// Error, should not occur
		throw new IllegalArgumentException("<=0 oxygen goal set");
	}

	/**
	 * Set the maximum/goal amount of oxygen to be present on the planet
	 *
	 * @param kilograms the number of kilograms the goal is set to.
	 */
	public void setOxygenGoal(int kilograms) throws IllegalArgumentException {
		if (kilograms <= 0) {
			throw new IllegalArgumentException("Goal cannot be 0 or negative");
		} else {
			logger.debug("Setting oxygen goal to {}", kilograms);
			oxygenGoal = kilograms;
		}
	}

	/**
	 * Gets the maximum/goal amount of oxygen to be present on the planet.
	 *
	 * @return kilograms of oxygen
	 */
	public float getOxygenGoal() {
		return oxygenGoal;
	}

	/**
	 * Gets the PlanetOxygenService's event handler
	 *
	 * @return the event handler
	 */
	public EventHandler getEvents() {
		return eventHandler;
	}

	/**
	 * Getter for the default initial oxygen value.
	 *
	 * @return the default initial/'starting' oxygen value.
	 */
	public float getDefaultInitialOxygen() {
		return DEFAULT_INITIAL_OXYGEN;
	}

	/**
	 * Perform the update on the oxygen present by applying the recalculated
	 * hour delta. If level reaches 0, trigger lose screen. Triggers an event to
	 * update the oxygen display.
	 */
	public void update() {
		logger.debug("Call private calculateDelta() method in oxygen update");
		delta = calculateDelta();

		if (oxygenPresent + delta <= 0) {
			// No oxygen left - trigger lose screen.
			logger.debug("No oxygen left, triggering final oxygenUpdate and loseScreen event");
			oxygenPresent = 0;
			eventHandler.trigger(OXYGEN_UPDATE);
			ServiceLocator.getMissionManager().getEvents().trigger("loseScreen", "oxygen");
		} else if (oxygenPresent + delta > oxygenGoal) {
			// Limit the present oxygen to not surpass the oxygen goal.
			logger.debug("Setting oxygen present to oxygen goal");
			oxygenPresent = oxygenGoal;
		} else {
			logger.debug("Adding delta of {} to oxygen present", delta);
			oxygenPresent += delta;
		}
		logger.debug("Calling oxygenUpdate event");
		eventHandler.trigger(OXYGEN_UPDATE);
	}

	/**
	 * Iterate through the existing entity array and matches up each entity with
	 * its corresponding oxygen value acquired from the EntityType enum. Then sums
	 * the hourly oxygen rate of all existing entities to provide the hourly delta.
	 *
	 * @return The calculated oxygen change for the hour.
	 */
	private float calculateDelta() {
		// Calculated change in oxygen for the hour
		float calculatedDelta = 0;
		EntityType type;
		// Loop through existing entities in the game to sum their oxygen values.
		for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
			logger.debug("Getting the entity type in delta calculation");
			type = entity.getType();
			if (type != null) {
				// Loop through registered entity types for a matching type.
				for (EntityType enumType : EntityType.values()) {
					if (type.equals(enumType)) {
						logger.debug("Adding entity type {}'s oxygen value to the " +
								"hourly delta", type);
						calculatedDelta += entity.getType().getOxygenRate();
						// Break from inner loop after first match as an entity
						// should only have one type.
						break;
					}
				}
			}
		}
		return calculatedDelta;
	}
}
