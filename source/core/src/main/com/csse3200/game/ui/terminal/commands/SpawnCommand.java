package com.csse3200.game.ui.terminal.commands;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
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
		Entity player = ServiceLocator.getGameArea().getPlayer();
		switch (args.get(0)) {
			case "cow" -> entity = NPCFactory.createCow(player);
			case "chicken" -> entity = NPCFactory.createChicken(player);
			default -> {
				logger.debug("Entity argument is not valid");
				return false;
			}
		}
		Vector2 position;
		if (args.size() == 3) {
			try {
				float x = Integer.parseInt(args.get(1));
				float y = Integer.parseInt(args.get(2));
				position = new Vector2(x, y);
			} catch (NumberFormatException e) {
				logger.debug("Arguments provided were not a valid integer");
				return false;
			}
		} else {
			position = ServiceLocator.getGameArea().getPlayer().getPosition();
		}
		entity.setPosition(position);
		ServiceLocator.getEntityService().register(entity);
		return true;
	}

	boolean correctNumArgs(ArrayList<String> args) {
		return args.size() == 1 || args.size() == 3;
	}
}
