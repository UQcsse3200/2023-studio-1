package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SetTimeScaleCommand implements Command {

	private static final Logger logger = LoggerFactory.getLogger(SetTimeScaleCommand.class);

	/**
	 * Adjusts the current game time to the specified value
	 *
	 * @param args command args
	 * @return command was successful
	 */
	@Override
	public boolean action(ArrayList<String> args) {
		if (!isValid(args)) {
			logger.debug("Invalid arguments received for 'setTimeScale' command: {}", args);
			return false;
		}
		ServiceLocator.getTimeSource().setTimeScale(Float.parseFloat(args.get(0)));
		return true;
	}

	/**
	 * Checks whether the args given to the command are valid. There must only be one argument which is a valid float
	 * greater than 0
	 *
	 * @param args arguments given to command
	 * @return whether the arguments are valid
	 */
	boolean isValid(ArrayList<String> args) {
		if (args.size() != 1) {
			logger.warn("Only 1 argument is needed and {} were given", args.size());
			return false;
		}
		float timeScale;
		try {
			timeScale = Float.parseFloat(args.get(0));
		} catch (NumberFormatException e) {
			logger.warn("Argument provided was not a valid integer");
			return false;
		}
		if (timeScale <= 0) {
			logger.warn("Timescale value given must be greater than 0");
			return false;
		}
		return true;
	}
}
