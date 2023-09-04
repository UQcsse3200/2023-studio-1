package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ServiceLocator;

public class ClimateController {

	private float humidity;
	private float temperature;

	private static final float DEFAULT_HUMIDITY = 0.5f;
	private static final float DEFAULT_TEMPERATURE = 26.0f;

	public ClimateController() {
		humidity = DEFAULT_HUMIDITY;
		temperature = DEFAULT_TEMPERATURE;
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::UpdateWeatherEvent);
	}

	/**
	 * Gets the current humidity of the game world
	 * @return current humidity value between 0 and 1
	 */
	public float getHumidity() {
		return humidity;
	}

	/**
	 * Gets the current temperature of the game world
	 * @return current game temperature
	 */
	public float getTemperature() {
		return temperature;
	}

	/**
	 * Updates the values of the game's climate based on current weather events
	 */
	private void updateClimate() {
		return;
	}

	/**
	 * Updates all the weather events every hour
	 */
	private void updateWeatherEvent() {
		return;
	}


}
