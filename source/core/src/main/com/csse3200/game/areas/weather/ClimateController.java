package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.function.Function;

public class ClimateController implements Json.Serializable {


	private static final Logger logger = LoggerFactory.getLogger(ClimateController.class);

	/**
	 * The weather event that is currently occurring in the game
	 */
	private WeatherEvent currentWeatherEvent;
	/**
	 * List of all weather events that are either occurring or about to occur
	 */
	private final ArrayList<WeatherEvent> weatherEvents = new ArrayList<>();
	/**
	 * Event handler that other entities can use to trigger weather-based events
	 */
	private final EventHandler events;

	/**
	 * The time specifying when the current lighting effect should stop
	 */
	private float currentLightingEffectEndPoint;
	/**
	 * The time specifying how far along the lighting effect is
	 */
	private float currentLightingEffectProgress;
	/**
	 * A function which returns the global colour offset for the lighting effect at some given t value between 0 and 1
	 * representing how close to finish the lighting effect is (0 being just started, 1 being just finished)
	 */
	private Function<Float, Color> currentLightingEffectGradient;

	/**
	 * Creates a new climate controller that listens to time events and maintains the in-game climate
	 */
	public ClimateController() {
		events = new EventHandler();
	}

	/**
	 * Initialises the events and listeners for this {@link ClimateController}.
	 */
	public void initialiseEvents() {
		ServiceLocator.getTimeService().getEvents().addListener("dayUpdate", this::addDailyEvent);
		ServiceLocator.getTimeService().getEvents().addListener("hourUpdate", this::updateWeatherEvent);
		ServiceLocator.getTimeService().getEvents().addListener("minuteUpdate", this::updateClimate);

		events.addListener("lightingEffect", this::setLightingEffect);
		currentLightingEffectProgress = 0.0f;
		currentLightingEffectEndPoint = ServiceLocator.getTimeSource().getTime();
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
	 * Updates the global lighting based on the current lighting effect
	 */
	private void updateLightingEffect() {
		currentLightingEffectProgress = (1000 - currentLightingEffectEndPoint + ServiceLocator.getTimeSource().getTime()) / 1000.0f;

		if (currentLightingEffectProgress > 1.0f) {
			// If the lighting effect has completed, clear any colour offsets
			ServiceLocator.getLightService().setColourOffset(Color.CLEAR);
			return;
		}

		// Apply lighting effect colour to global ambient lighting
		ServiceLocator.getLightService().setColourOffset(
				currentLightingEffectGradient.apply(currentLightingEffectProgress));
	}

	/**
	 * Sets the current lighting effect to have the specified duration and colourGradient.
	 *
	 * @param duration       The duration of the lighting effect in seconds.
	 * @param colourGradient The gradient of colour offsets. It should be a function which accepts a float, and returns
	 *                       a colour offset.
	 */
	private void setLightingEffect(float duration, Function<Float, Color> colourGradient) {
		currentLightingEffectEndPoint = ServiceLocator.getTimeSource().getTime() + duration * 1000.0f;
		currentLightingEffectProgress = 0.0f;
		currentLightingEffectGradient = colourGradient;
		updateLightingEffect();
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
	 * Methods that is run every day to possibly generate a random weather event
	 */
	private void addDailyEvent() {
		float eventPossibility = MathUtils.random();
		// 7% chance that no weather event is generated on a given day
		if (eventPossibility <= 0.07) {
			return;
		}
		int eventGenerated = MathUtils.random(0, 9);
		int numHoursUntil = MathUtils.random(1, 23);
		int duration = MathUtils.random(1, 8);
		int priority = MathUtils.random(0, 3);
		// Random weather events should have a severity between 0 and 1.2 (maximum severity is 1.5)
		float severity = MathUtils.random() * 1.2f;
		WeatherEvent event;
		switch (eventGenerated) {
			case 0, 1, 2, 3 -> event = new RainStormEvent(numHoursUntil, duration, priority, severity);
			case 4, 5, 6 -> event = new BlizzardEvent(numHoursUntil, duration, priority, severity);
			case 7, 8 -> event = new SolarSurgeEvent(numHoursUntil, duration, priority, severity);
			case 9 -> event = new AcidShowerEvent(numHoursUntil, duration, priority, severity);
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
		events.update();
		updateLightingEffect();
	}

	/**
	 * Updates all the weather events every hour
	 */
	private void updateWeatherEvent() {
		// Sets initial priority to current weather event or 0 if nothing is happening
		if (currentWeatherEvent != null) {
			currentWeatherEvent.stopEffect();
		}

		// Update weather event durations
		for (WeatherEvent event : weatherEvents) {
			event.updateTime();
		}
		// Remove inactive events
		weatherEvents.removeIf(WeatherEvent::isExpired);

		recalculateCurrentWeatherEvent();

		if (currentWeatherEvent != null) {
			currentWeatherEvent.startEffect();
		}
	}

	/**
	 * Recalculates which {@link WeatherEvent} should be the currentWeatherEvent
	 */
	private void recalculateCurrentWeatherEvent() {
		currentWeatherEvent = null;
		int priority = -1;
		// Updates every weather event
		for (WeatherEvent event : weatherEvents) {
			// Checks whether an event is active and is of higher priority
			if (event.isActive() && event.getPriority() > priority) {
				currentWeatherEvent = event;
				priority = currentWeatherEvent.getPriority();
			}
		}
	}

	@Override
	public void write(Json json) {
		json.writeObjectStart("Events");
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
		weatherEvents.clear();
		jsonData = jsonData.get("Events");
		if (jsonData.has("Event")) {
			jsonData.forEach(jsonValue -> {
				switch (jsonValue.getString("name")) {
					case ("AcidShowerEvent") -> addWeatherEvent(new AcidShowerEvent(jsonValue.getInt("hoursUntil"),
							jsonValue.getInt("duration"), jsonValue.getInt("priority"),
							jsonValue.getFloat("severity")));
					case ("RainStormEvent") -> addWeatherEvent(new RainStormEvent(jsonValue.getInt("hoursUntil"),
							jsonValue.getInt("duration"), jsonValue.getInt("priority"),
							jsonValue.getFloat("severity")));
					case ("BlizzardEvent") -> addWeatherEvent(new BlizzardEvent(jsonValue.getInt("hoursUntil"),
							jsonValue.getInt("duration"), jsonValue.getInt("priority"),
							jsonValue.getFloat("severity")));
					case ("SolarSurgeEvent") -> addWeatherEvent(new SolarSurgeEvent(jsonValue.getInt("hoursUntil"),
							jsonValue.getInt("duration"), jsonValue.getInt("priority"),
							jsonValue.getFloat("severity")));
					default -> logger.error("Invalid weather event type while loading");
				}
			});
		}
		recalculateCurrentWeatherEvent();
		if (currentWeatherEvent != null) {
			currentWeatherEvent.startEffect();
		}
	}
}
