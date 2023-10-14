package com.csse3200.game.areas.weather;

import com.badlogic.gdx.utils.Json;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

/**
 * Represents a Weather Event like Acid Shower or Solar Surge. These are updated based on in-game hours.
 */
public abstract class WeatherEvent {

	/**
	 * Number of in-game hours until the {@link WeatherEvent} is able to be triggered and affects the in-game climate
	 */
	protected int numHoursUntil;

	/**
	 * Number of in-game hours that the {@link WeatherEvent} can be active for
	 */
	protected int duration;

	/**
	 * The priority of the {@link WeatherEvent} so that some weather can override others if they are important
	 */
	protected final int priority;

	/**
	 * The severity of the {@link WeatherEvent} which can affect the climate modifiers depending on the implementation
	 * of the {@link WeatherEvent}. This ranges from 1.0 to 1.5
	 */
	protected final float severity;

	protected final EventHandler climateControllerEvents;

	/**
	 * Constructs an {@link WeatherEvent} with a given duration, priority and countdown
	 *
	 * @param numHoursUntil number of in-game hours until the weather event can occur
	 * @param duration      number of in-game hours that the event can occur for
	 * @param priority      priority of the weather event
	 */
	protected WeatherEvent(int numHoursUntil, int duration, int priority, float severity) throws IllegalArgumentException {
		if (priority < 0) {
			throw new IllegalArgumentException("Priority cannot be less than 0");
		} else if (duration <= 0) {
			throw new IllegalArgumentException("Duration cannot be less than 0");
		} else if (numHoursUntil < 0) {
			throw new IllegalArgumentException("Number of hours until the event occurs cannot be less than 0");
		} else if (severity < 0f) {
			throw new IllegalArgumentException("Severity must be greater than 0.");
		}

		this.numHoursUntil = numHoursUntil;
		this.duration = duration;
		this.priority = priority;
		this.severity = severity;

		this.climateControllerEvents = ServiceLocator.getGameArea().getClimateController().getEvents();
	}

	/**
	 * Updates the duration or countdown timer for the weather event. This is called by the {@link ClimateController}
	 * every in-game hour
	 */
	public void updateTime() {
		if (numHoursUntil > 0) {
			numHoursUntil--;
		} else if (duration > 0) {
			duration--;
		}
	}

	/**
	 * Gets the duration of the weather event
	 *
	 * @return the amount of time the weather event has left
	 */
	public int getDuration() {
		return duration;
	}

	public int getNumHoursUntil() {
		return numHoursUntil;
	}

	/**
	 * Returns the priority of the weather event
	 *
	 * @return weather event priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Determines whether the weather event is active based on the countdown timer and the duration
	 *
	 * @return boolean value representing
	 */
	public boolean isActive() {
		return numHoursUntil == 0 && duration > 0;
	}

	/**
	 * Determines whether the weather event has finished
	 *
	 * @return boolean value representing whether the weather event is finished or not
	 */
	public boolean isExpired() {
		return numHoursUntil == 0 && duration <= 0;
	}

	/**
	 * Gets the severity of this weather event
	 *
	 * @return weather event severity
	 */
	public float getSeverity() {
		return severity;
	}

	/**
	 * Starts the visual effect for the weather event
	 */
	public abstract void startEffect();

	/**
	 * Stops the visual effect for the weather event
	 */
	public abstract void stopEffect();

	public void write(Json json) {
		json.writeObjectStart("Event");
		json.writeValue("name", getClass().getSimpleName());
		json.writeValue("severity", severity);
		json.writeValue("duration", duration);
		json.writeValue("hoursUntil", numHoursUntil);
		json.writeValue("priority", priority);
		json.writeObjectEnd();
	}
}
