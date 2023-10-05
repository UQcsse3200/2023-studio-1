package com.csse3200.game.ui.terminal.commands;

import java.util.ArrayList;
import java.util.StringJoiner;

import com.badlogic.gdx.graphics.glutils.FacedCubemapData;
import com.csse3200.game.services.FactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.ServiceLocator;

public class AddItemCommand implements Command {

	/**
	 * Logger for this command
	 */
	private static final Logger logger = LoggerFactory.getLogger(AddItemCommand.class);

	/**
	 * Action a command to add an item to a player's inventory
	 *
	 * @param args command args
	 * @return command was successful
	 */
	@Override
	public boolean action(ArrayList<String> args) {
		if (!isValid(args)) {
			logger.debug("Invalid arguments received for 'addItem' command: {}", args);
			return false;
		}
		Entity player = ServiceLocator.getGameArea().getPlayer();
		StringJoiner str = new StringJoiner(" ");
		for (String partOfName : args) {
			str.add(partOfName);
		}
		String itemName = str.toString();
		try {
			Entity item = FactoryService.getItemFactories().get(itemName).get();
			ServiceLocator.getGameArea().spawnEntity(item);
			player.getComponent(InventoryComponent.class).addItem(item);
		} catch (Exception e) {
			logger.info("Incorrect item name given to addItem command.");
			return false;
		}
		return true;
	}

	boolean isValid(ArrayList<String> args) {
		return args.size() > 0;
	}

}
