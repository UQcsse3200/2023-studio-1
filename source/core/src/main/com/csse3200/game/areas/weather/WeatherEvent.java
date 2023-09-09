package com.csse3200.game.areas.weather;

import java.util.Random;

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

	/**
	 * Modifier that is used to calculate the humidity in game
	 */
	protected float humidityModifier;

	/**
	 * Modifier used to calculate the temperature in game
	 */
	protected float temperatureModifier;

	/**
	 * Constructs an {@link WeatherEvent} with a given duration, priority and countdown
	 *
	 * @param numHoursUntil number of in-game hours until the weather event can occur
	 * @param duration      number of in-game hours that the event can occur for
	 * @param priority      priority of the weather event
	 */
	public WeatherEvent(int numHoursUntil, int duration, int priority, float severity) {
		this.numHoursUntil = numHoursUntil;
		this.duration = duration;
		this.priority = priority;
		this.severity = severity;
		this.humidityModifier = 1.0f;
		this.temperatureModifier = 1.0f;
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
	 * Gets the humidity modifier of this weather event
	 *
	 * @return humidity modifier
	 */
	public float getHumidityModifier() {
		return humidityModifier;
	}

	/**
	 * Gets the temperature modifier of this weather event
	 *
	 * @return temperature modifier
	 */
	public float getTemperatureModifier() {
		return temperatureModifier;
	}

	/**
	 * Gets the severity of this weather event
	 *
	 * @return weather event severity
	 */
	public float getSeverity() {
		return severity;
	}
}
