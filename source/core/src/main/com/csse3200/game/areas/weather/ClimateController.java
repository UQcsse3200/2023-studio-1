package com.csse3200.game.areas.weather;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;

public class ClimateController {

	private float humidity;
	private float temperature;

	private static final float DEFAULT_HUMIDITY = 0.5f;
	private static final float DEFAULT_TEMPERATURE = 26.0f;
	private static WeatherEvent currentWeatherEvent;
	private static final ArrayList<WeatherEvent> weatherEvents = new ArrayList<>();
	private final EventHandler events;

	public ClimateController() {
		humidity = DEFAULT_HUMIDITY;
		temperature = DEFAULT_TEMPERATURE;
		events = new EventHandler();
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateWeatherEvent);
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::determineActiveWeatherEvent);
	}

	/**
	 * Gets the current humidity of the game world
	 *
	 * @return current humidity value between 0 and 1
	 */
	public float getHumidity() {
		return humidity;
	}

	/**
	 * Returns the event handler for the Climate controller class
	 * @return Event handler
	 */
	public EventHandler getEvents() {
		return events;
	}

	/**
	 * Gets the current temperature of the game world
	 *
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

	private void determineActiveWeatherEvent() {
		int priority = 0;
		for (WeatherEvent event : weatherEvents) {
			if (event.isActive() && event.getPriority() > priority) {
				currentWeatherEvent = event;
				priority = event.getPriority();
			}
		}
	}


}
