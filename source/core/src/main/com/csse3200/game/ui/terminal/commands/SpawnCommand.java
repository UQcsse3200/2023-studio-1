package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SpawnCommand implements Command {
	private static final Logger logger = LoggerFactory.getLogger(SpawnCommand.class);
	/**
	 * Spawns an entity in the game world.
	 *
	 * @param args command args
	 * @return command was successful
	 */
	@Override
	public boolean action(ArrayList<String> args) {
		if (!correctNumArgs(args)) {
			logger.debug("Incorrect number of arguments provided");
			return false;
		}
		Entity entity;
		switch (args.get(0)) {
			case "cow":
				entity = NPCFactory.createCow();
				break;
		}
		return false;
	}

	boolean correctNumArgs(ArrayList<String> args) {
		return args.size() == 1 || args.size() == 3;
	}
}
