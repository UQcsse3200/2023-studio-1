package com.csse3200.game.areas.weather;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;

public class ClimateController {


	/**
	 * In-game humidity
	 */
	private float humidity;
	/**
	 * In-game temperature
	 */
	private float temperature;

	/**
	 * Minimum temperature possible in the game without the effects of weather events
	 */
	private static final float MIN_TEMPERATURE = 0f;
	/**
	 * Maximum temperature possible in the game without the effects of weather events
	 */
	private static final float MAX_TEMPERATURE = 40f;

	/**
	 * The weather event that is currently occurring in the game
	 */
	private static WeatherEvent currentWeatherEvent;
	/**
	 * List of all weather events that are either occurring or about to occur
	 */
	private static final ArrayList<WeatherEvent> weatherEvents = new ArrayList<>();
	/**
	 * Event handler that other entities can use to trigger weather-based events
	 */
	private final EventHandler events;

	/**
	 * Creates a new climate controller that listens to time events and maintains the in-game climate
	 */
	public ClimateController() {
		events = new EventHandler();
		ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::addDailyEvent);
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
	public void addWeatherEvent(WeatherEvent event) throws IllegalArgumentException {
		if (event == null) {
			throw new IllegalArgumentException("Null cannot be added as a weather event");
		}
		weatherEvents.add(event);
		if (!event.isActive()) {
			return;
		}
		if (currentWeatherEvent == null) {
			currentWeatherEvent = event;
		} else if (event.getPriority() > currentWeatherEvent.getPriority()) {
			currentWeatherEvent = event;
		}
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
	 * Methods that is run every day to possibly generate a random weather event
	 */
	private void addDailyEvent() {
		float eventPossibility = MathUtils.random();
		// 25% that no weather event is generated
		if (eventPossibility <= 0.25) {
			return;
		}
		int eventGenerated = MathUtils.random(0, 1);
		int numHoursUntil = MathUtils.random(1, 6);
		int duration = MathUtils.random(2, 5);
		int priority = MathUtils.random(0, 3);
		float severity = MathUtils.random();
		WeatherEvent event;
		switch (eventGenerated) {
			case 0 -> event = new AcidShowerEvent(numHoursUntil, duration, priority, severity);
			case 1 -> event = new SolarSurgeEvent(numHoursUntil, duration, priority, severity);
			default -> {
				return;
			}
		}
		addWeatherEvent(event);
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
	 * Climate generation algorithm that is inspired by the Perlin noise algorithm. Credit to @Tom-Strooper for the Java
	 * implementation.
	 * Inspiration from this <a href="https://github.com/SebLague/Procedural-Landmass-Generation">GitHub Repo</a>
	 *
	 * @param time        in-game time value
	 * @param offset      function offset
	 * @param octaves     number of noise functions used in the calculation
	 * @param persistence how much each octave/function contributes to the noise generated
	 * @param lacunarity  how much each octave increases in frequency
	 * @return generated noise value used in calculating climate values
	 */
	private float generateClimate(
			float time, float offset, int octaves, float persistence, float lacunarity) {
		float maxAmplitude = 0f;
		float amplitude = 1.0f;
		float frequency = 1.0f;

		float temperatureNormalised = 0.0f;

		for (int i = 0; i < octaves; i++) {
			temperatureNormalised += amplitude * getClimateSin((time - offset) * frequency);
			maxAmplitude += amplitude;

			amplitude *= persistence;
			frequency *= lacunarity;
		}

		return temperatureNormalised / maxAmplitude;
	}

	/**
	 * Sin function used for calculating climate in the generateClimate() method
	 *
	 * @param x function variable
	 * @return function result
	 */
	private float getClimateSin(float x) {
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
			}
		}
		weatherEvents.removeIf(WeatherEvent::isExpired);
	}
}
