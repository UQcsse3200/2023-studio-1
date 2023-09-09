package com.csse3200.game.areas.weather;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;

import java.util.ArrayList;

public class ClimateController {

	private float humidity;
	private float temperature;

	private static final float DEFAULT_HUMIDITY = 0.5f;
	private static final float MIN_TEMPERATURE = 0f;
	private static final float MAX_TEMPERATURE = 30f;

	private static final float MIN_HUMIDITY = 0f;
	private static final float MAX_HUMIDITY = 1.0f;

	private static WeatherEvent currentWeatherEvent;
	private static final ArrayList<WeatherEvent> weatherEvents = new ArrayList<>();
	private final EventHandler events;

	public ClimateController() {
		events = new EventHandler();
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateWeatherEvent);
		ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::updateClimate);
		updateClimate();
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
	 *
	 * @return Event handler
	 */
	public EventHandler getEvents() {
		return events;
	}

	/**
	 * Adds a weather event to the list of weather events stored in the climate controller class
	 *
	 * @param event Weather event
	 */
	public void addWeatherEvent(WeatherEvent event) {
		weatherEvents.add(event);
	}

	/**
	 * Gets the current weather event that is occurring
	 *
	 * @return current weather event, null if not event is occurring
	 */
	public WeatherEvent getCurrentWeatherEvent() {
		return currentWeatherEvent;
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
	 * Updates the values of the game's climate based on current weather events. Factoring in the in-game time and
	 * the weather event modifiers.
	 */
	private void updateClimate() {
		float time = ServiceLocator.getTimeService().getDay() * 24
				+ ServiceLocator.getTimeService().getHour()
				+ (float) ServiceLocator.getTimeService().getMinute() / 60;


		float humidityModifier = currentWeatherEvent == null ? 0 : currentWeatherEvent.getHumidityModifier();
		float temperatureModifier = currentWeatherEvent == null ? 0 : currentWeatherEvent.getTemperatureModifier();

		temperature = (MAX_TEMPERATURE - MIN_TEMPERATURE) * generateClimate(time, 4, 8, 0.24f, 2.8f)
				+ MIN_TEMPERATURE + temperatureModifier;

		humidity = generateClimate(time, -4, 8, 0.14f, 3.5f) + humidityModifier;
	}

	/**
	 * Climate generation algorithm that is inspired by the Perlin noise algorithm.
	 * @param time in-game time value
	 * @param offset function offset
	 * @param octaves number of noise functions used in the calculation
	 * @param persistence how much each octave/function contributes to the noise generated
	 * @param lacunarity how much each octave increases in frequency
	 * @return generated noise value used in calculating climate values
	 */
	private float generateClimate(
			float time, float offset, int octaves, float persistence, float lacunarity) {
		float maxAmplitude = 0f;
		float amplitude = 1.0f;
		float frequency = 1.0f;

		float temperatureNormalised = 0.0f;

		for (int i = 0; i < octaves; i++) {
			temperatureNormalised += amplitude * getTempSin((time - offset) * frequency);
			maxAmplitude += amplitude;

			amplitude *= persistence;
			frequency *= lacunarity;
		}

		return temperatureNormalised / maxAmplitude;
	}

	/**
	 * Sin function used for calculating climate in the generateClimate() method
	 * @param x function variable
	 * @return function result
	 */
	private float getTempSin(float x) {
		return 0.5f * MathUtils.sin((float) ((x - 6) * Math.PI / 12)) + 0.5f;
	}

	/**
	 * Updates all the weather events every hour
	 */
	private void updateWeatherEvent() {
		// Sets initial priority to current weather event or 0 if nothing is happening
		currentWeatherEvent = null;
		int priority = -1;
		// Updates every weather event
		for (WeatherEvent event : weatherEvents) {
			event.updateTime();
			// Checks whether an event is active and is of higher priority
			if (event.isActive() && event.getPriority() > priority) {
				currentWeatherEvent = event;
				priority = currentWeatherEvent.getPriority();
				// If the event is expired, remove it from the list
			} else if (event.isExpired()) {
				weatherEvents.remove(event);
			}
		}
	}
}
