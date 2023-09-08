package com.csse3200.game.areas.weather;

import java.util.Random;

public abstract class WeatherEvent {

	/**
	 * Number of in-game hours until the {@link WeatherEvent} is able to be triggered and affects the in-game climate
	 */
	private int numHoursUntil;

	/**
	 * Number of in-game hours that the {@link WeatherEvent} can be active for
	 */
	private int duration;

	/**
	 * The priority of the {@link WeatherEvent} so that some weather can override others if they are important
	 */
	private final int priority;

	/**
	 * The severity of the {@link WeatherEvent} which can affect the climate modifiers depending on the implementation
	 * of the {@link WeatherEvent}. This ranges from 1.0 to 1.5
	 */
	private final float severity;

	/**
	 * Modifier that is used to calculate the humidity in game
	 */
	private final float humidityModifier;

	/**
	 * Modifier used to calculate the temperature in game
	 */
	private final float temperatureModifier;

	private static final float MIN_HUMIDITY_MODIFIER = 1.0f;
	private static final float MAX_HUMIDITY_MODIFIER = 1.0f;
	private static final float MIN_TEMPERATURE_MODIFIER = 1.0f;
	private static final float MAX_TEMPERATURE_MODIFIER = 1.0f;

	/**
	 * Constructs an {@link WeatherEvent} with a given duration, priority and countdown
	 * @param numHoursUntil number of in-game hours until the weather event can occur
	 * @param duration number of in-game hours that the event can occur for
	 * @param priority priority of the weather event
	 */
	public WeatherEvent(int numHoursUntil, int duration, int priority, float severity) {
		this.numHoursUntil = numHoursUntil;
		this.duration = duration;
		this.priority = priority;
		this.severity = severity;
		this.humidityModifier = MIN_HUMIDITY_MODIFIER + (MAX_HUMIDITY_MODIFIER - MIN_HUMIDITY_MODIFIER) * severity;
		this.temperatureModifier = MIN_TEMPERATURE_MODIFIER +
				(MAX_TEMPERATURE_MODIFIER - MIN_TEMPERATURE_MODIFIER) * severity;
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
	 * @return boolean value representing
	 */
	public boolean isActive() {
		return numHoursUntil == 0 && duration > 0;
	}

	/**
	 * Determines whether the weather event has finished
	 * @return boolean value representing whether the weather event is finished or not
	 */
	public boolean isExpired() {
		return numHoursUntil == 0 && duration <= 0;
	}

	// TODO Figure out how to render screen effects and therefore fix method siganture
	public abstract void getEffect();

	/**
	 * Generates a random humidity modifier based on the min and max values defined
	 * @return humidity modifier
	 */
	protected float generateRandomHumidityModifier() {
		return (new Random().nextFloat() * (MAX_HUMIDITY_MODIFIER - MIN_HUMIDITY_MODIFIER)) + MIN_HUMIDITY_MODIFIER;
	}

	/**
	 * Generates a random temperature humidity modifier based on the min and max values defined
	 * @return temperature modifier
	 */
	protected float generateRandomTemperatureModifier() {
		return (new Random().nextFloat() * (MAX_TEMPERATURE_MODIFIER - MIN_TEMPERATURE_MODIFIER)) + MIN_TEMPERATURE_MODIFIER;
	}

	/**
	 * Generates a severity between 1 and 1.5
	 * @return weather event severity
	 */
	protected float generateRandomSeverity() {
		return (float) ((new Random().nextFloat() * 0.5) + 1);
	}

	/**
	 * Gets the humidity modifier of this weather event
	 * @return humidity modifier
	 */
	public float getHumidityModifier() {
		return humidityModifier;
	}

	/**
	 * Gets the temperature modifier of this weather event
	 * @return temperature modifier
	 */
	public float getTemperatureModifier() {
		return temperatureModifier;
	}

	/**
	 * Gets the severity of this weather event
	 * @return weather event severity
	 */
	public float getSeverity() {
		return severity;
	}
}
