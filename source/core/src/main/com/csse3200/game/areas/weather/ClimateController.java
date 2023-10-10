package com.csse3200.game.areas.weather;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ClimateController implements Json.Serializable {


	private static final Logger logger = LoggerFactory.getLogger(ClimateController.class);

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
	private WeatherEvent currentWeatherEvent;
	/**
	 * List of all weather events that are either occurring or about to occur
	 */
	private static final ArrayList<WeatherEvent> weatherEvents = new ArrayList<>();
	/**
	 * Event handler that other entities can use to trigger weather-based events
	 */
	private final EventHandler events;
	private final EntityService entityService;

	/**
	 * Creates a new climate controller that listens to time events and maintains the in-game climate
	 */
	public ClimateController() {
		events = new EventHandler();
  		entityService = ServiceLocator.getEntityService();
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
	 * Sets the current humidity of the game world
	 */
	public void setHumidity(float humidity) {
		this.humidity = humidity;
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
		if (currentWeatherEvent == null || event.getPriority() > currentWeatherEvent.getPriority()) {
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
	 * Gets the current weather event that is occurring
	 */
	public void setCurrentWeatherEvent(WeatherEvent event) {
		currentWeatherEvent = event;
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
	 * Sets the temperature to a given temperature
	 *
	 * @param temperature the temperature to set it to
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
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
	 * @param octaves     number of noise functions used in the calculation (must be greater than 0)
	 * @param persistence how much each octave/function contributes to the noise generated
	 * @param lacunarity  how much each octave increases in frequency
	 * @return generated noise value used in calculating climate values
	 */
	private float generateClimate(
			float time, float offset, int octaves, float persistence, float lacunarity) {
		if (octaves <= 0) {
			throw new IllegalArgumentException("Number of noise functions must be greater than 0");
		}
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

		// To avoid divide by zero
		if (maxAmplitude == 0) {
			return 0.5f;
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
		if (currentWeatherEvent != null) {
			currentWeatherEvent.stopEffect();
		}
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
		if (currentWeatherEvent != null) {
			if (currentWeatherEvent instanceof AcidShowerEvent) {
				if (entityService != null) {
					Array<Entity> entities = entityService.getEntities();
					for (Entity entity : entities) {
						PlantComponent plantComponent = entity.getComponent(PlantComponent.class);
						if (plantComponent != null) {
							entity.getEvents().trigger("acidShower");
						}
					}
				}
			}
			currentWeatherEvent.startEffect();
		}
	}

	@Override
	public void write(Json json) {
		json.writeValue("Temp", getTemperature());
		json.writeValue("Humidity", getHumidity());
		json.writeObjectStart("Events");
		updateWeatherEvent();
		for (WeatherEvent event : weatherEvents) {
			event.write(json);
		}
		json.writeObjectEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		ServiceLocator.getGameArea().getClimateController().setValues(jsonData);
	}


	public void setValues(JsonValue jsonData) {
		temperature = jsonData.getFloat("Temp");
		humidity = jsonData.getFloat("Humidity");
		jsonData = jsonData.get("Events");
		jsonData.forEach(jsonValue -> {
			if (jsonValue.get("Event") != null) {
				switch (jsonValue.getString("name")) {
					case ("AcidShowerEvent") -> addWeatherEvent(new AcidShowerEvent(jsonValue.getInt("hoursUntil"),
								jsonValue.getInt("duration"), jsonValue.getInt("priority"),
								jsonValue.getFloat("severity")));
					case ("SolarSurgeEvent") -> addWeatherEvent(new SolarSurgeEvent(jsonValue.getInt("hoursUntil"),
								jsonValue.getInt("duration"), jsonValue.getInt("priority"),
								jsonValue.getFloat("severity")));
					default -> logger.error("Invalid weather event type while loading");
				}
			}
		});
		updateWeatherEvent();
	}
}
