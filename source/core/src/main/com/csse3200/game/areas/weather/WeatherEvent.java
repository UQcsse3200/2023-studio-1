package com.csse3200.game.areas.weather;

import java.util.Random;

public abstract class WeatherEvent {

	private int numHoursUntil;
	private int duration;
	private int priority;
	private float humidityModifier;
	private float temperatureModifier;
	private static final float MIN_HUMIDITY_MODIFIER = 0.4f;
	private static final float MAX_HUMIDITY_MODIFIER = 1.6f;
	private static final float MIN_TEMPERATURE_MODIFIER = 0.8f;
	private static final float MAX_TEMPERATURE_MODIFIER = 1.2f;

	public WeatherEvent(int numHoursUntil, int duration, int priority) {
		this.numHoursUntil = numHoursUntil;
		this.duration = duration;
		this.priority = priority;
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

	private static float generateRandomHumidityModifier() {
		return (new Random().nextFloat() * (MAX_HUMIDITY_MODIFIER - MIN_HUMIDITY_MODIFIER)) + MIN_HUMIDITY_MODIFIER;
	}

	private static float generateRandomTemperatureModifier() {
		return (new Random().nextFloat() * (MAX_TEMPERATURE_MODIFIER - MIN_TEMPERATURE_MODIFIER)) + MIN_TEMPERATURE_MODIFIER;
	}

}
