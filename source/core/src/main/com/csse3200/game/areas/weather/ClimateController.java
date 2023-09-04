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

	public float GetHumidity() {
		return humidity;
	}

	public float GetTemperature() {
		return temperature;
	}

	private void UpdateClimate() {
		return;
	}

	private void UpdateWeatherEvent() {
		return;
	}


}
