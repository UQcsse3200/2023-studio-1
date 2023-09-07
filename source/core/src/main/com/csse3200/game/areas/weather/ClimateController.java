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
		events.addListener("startEvent", this::determineActiveEvent);
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
		if (currentWeatherEvent.isActive()) {
			humidity *= currentWeatherEvent.getHumidityModifier();
			temperature *= currentWeatherEvent.getTemperatureModifier();

			humidity = Math.min(Math.max(humidity, 0.2f), 1.0f);
			temperature = Math.min(Math.max(temperature, 10.0f), 35.0f);
		}
	}

	/**
	 * Updates all the weather events every hour
	 */
	private void updateWeatherEvent() {
		// Sets initial priority to current weather event or 0 if nothing is happening
		currentWeatherEvent = null;
		int priority = -1;
		// Updates every weather event
		for (WeatherEvent event: weatherEvents) {
			event.updateTime();
			// Checks whether an event is active and is of higher priority
			if (event.isActive() && event.getPriority() > priority){
				currentWeatherEvent = event;
				priority = currentWeatherEvent.getPriority();
			}
		}
		updateClimate();
	}

	private void determineActiveEvent(WeatherEvent event) {
		int priority = -1;
		if (currentWeatherEvent != null) {
			priority = currentWeatherEvent.getPriority();
		}
		if (priority > event.getPriority()) {
			currentWeatherEvent = event;
		}

	}
}
