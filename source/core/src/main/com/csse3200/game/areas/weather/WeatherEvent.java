package com.csse3200.game.areas.weather;

import java.util.Random;

public abstract class WeatherEvent {

	private int numHoursUntil;
	private int duration;
	private final int priority;
	private final float severity;
	private final float humidityModifier;
	private final float temperatureModifier;
	private static final float MIN_HUMIDITY_MODIFIER = 1.0f;
	private static final float MAX_HUMIDITY_MODIFIER = 1.0f;
	private static final float MIN_TEMPERATURE_MODIFIER = 1.0f;
	private static final float MAX_TEMPERATURE_MODIFIER = 1.0f;

	public WeatherEvent(int numHoursUntil, int duration, int priority) {
		this.numHoursUntil = numHoursUntil;
		this.duration = duration;
		this.priority = priority;
		this.severity = generateRandomSeverity();
		this.humidityModifier = generateRandomHumidityModifier();
		this.temperatureModifier = generateRandomTemperatureModifier();
	}

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

	public boolean isActive() {
		return numHoursUntil == 0 && duration > 0;
	}

	// TODO Figure out how to render screen effects and therefore fix method siganture
	public abstract void getEffect();

	protected float generateRandomHumidityModifier() {
		return (new Random().nextFloat() * (MAX_HUMIDITY_MODIFIER - MIN_HUMIDITY_MODIFIER)) + MIN_HUMIDITY_MODIFIER;
	}

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

	public float getHumidityModifier() {
		return humidityModifier;
	}

	public float getTemperatureModifier() {
		return temperatureModifier;
	}

	public float getSeverity() {
		return severity;
	}
}
