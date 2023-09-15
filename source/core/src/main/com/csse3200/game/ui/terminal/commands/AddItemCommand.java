package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
		String itemName = args.get(0);
		Entity item;
		switch (itemName) {
			case "hoe" -> item = ItemFactory.createHoe();
			case "shovel" -> item = ItemFactory.createShovel();
			case "can" -> item = ItemFactory.createWateringcan();
			case "scythe" -> item = ItemFactory.createScythe();
			case "sprinkler" -> item = ItemFactory.createSprinklerItem();
			case "pump" -> item = ItemFactory.createPumpItem();
			default -> {
				logger.debug("The provided item name does not exist");
				return false;
			}
		}
		player.getComponent(InventoryComponent.class).addItem(item);
		return true;
	}

	boolean isValid(ArrayList<String> args) {
		return args.size() == 1;
	}

}
