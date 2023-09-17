package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.areas.weather.AcidShowerEvent;
import com.csse3200.game.areas.weather.SolarSurgeEvent;
import com.csse3200.game.areas.weather.WeatherEvent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class AddWeatherCommand implements Command {

	/**
	 * Logger for this command
	 */
	private static final Logger logger = LoggerFactory.getLogger(AddWeatherCommand.class);

	/**
	 * Action a command to add a weather event to the game
	 *
	 * @param args command args
	 * @return command was successful
	 */
	@Override
	public boolean action(ArrayList<String> args) {
		if (!isValid(args)) {
			logger.error("Invalid arguments received for 'addWeather' command: {}", args);
			return false;
		}
		int numHoursUntil;
		int duration;
		int priority;
		float severity;

		try {
			numHoursUntil = Integer.parseInt(args.get(1));
			duration = Integer.parseInt(args.get(2));
			priority = Integer.parseInt(args.get(3));
			severity = Float.parseFloat(args.get(4));
		} catch (NumberFormatException e) {
			logger.error("Arguments provided were not valid numbers: {}", args);
			return false;
		}

		if (numHoursUntil < 0) {
			logger.error("Number of hours until weather event cannot be less than 0");
			return false;
		}

		if (duration <= 0) {
			logger.error("Duration of weather event cannot be less than or equal to 0");
			return false;
		}

		if (priority < 0) {
			logger.error("Priority of weather event cannot be less than 0");
			return false;
		}

		if (severity < 0f) {
			logger.error("Severity of weather event cannot be less than 0");
			return false;
		}

		String weatherName = args.get(0);

		WeatherEvent weatherEvent;

		switch (weatherName) {
			case "acidShower" -> weatherEvent = new AcidShowerEvent(numHoursUntil, duration, priority, severity);
			case "solarSurge" -> weatherEvent = new SolarSurgeEvent(numHoursUntil, duration, priority, severity);
			default -> {
				logger.error("Valid weather event name was not entered");
				return false;
			}
		}
		logger.debug("Adding {} weather event", weatherName);

		ServiceLocator.getGameArea().getClimateController().addWeatherEvent(weatherEvent);
		return true;
	}

	/**
	 * Determines whether the correct number of arguments were given
	 * @param args commands arguments
	 * @return whether the number of arguments is valid or not
	 */
	boolean isValid(ArrayList<String> args) {
		return args.size() == 5;
	}

}
