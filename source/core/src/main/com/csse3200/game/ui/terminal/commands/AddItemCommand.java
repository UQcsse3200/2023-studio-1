package com.csse3200.game.ui.terminal.commands;

import java.util.*;
import java.util.function.Supplier;

import com.csse3200.game.entities.factories.ItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

public class AddItemCommand implements Command {

	/**
	 * Logger for this command
	 */
	private static final Logger logger = LoggerFactory.getLogger(AddItemCommand.class);

	private static final Map<String, Supplier<Entity>> itemFactories = Map.ofEntries(
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("shovel", ItemFactory::createShovel),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("hoe", ItemFactory::createHoe),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("watering can", ItemFactory::createWateringcan),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("scythe", ItemFactory::createScythe),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("sword", ItemFactory::createSword),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("gun", ItemFactory::createGun),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("milk", ItemFactory::createMilk),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("egg", ItemFactory::createEgg),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("golden egg", ItemFactory::createGoldenEgg),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("clue", ItemFactory::createClueItem),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("fertiliser", ItemFactory::createFertiliser),

			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("aloe vera seeds", ItemFactory::createAloeVeraSeed),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("aloe vera leaf", ItemFactory::createAloeVeraLeaf),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("atomic algae seeds", ItemFactory::createAtomicAlgaeSeed),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("cosmic cob seeds", ItemFactory::createCosmicCobSeed),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("ear of cosmic cob", ItemFactory::createCosmicCobEar),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("deadly nightshade seeds", ItemFactory::createDeadlyNightshadeSeed),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("nightshade berry", ItemFactory::createDeadlyNightshadeBerry),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("hammer plant seeds", ItemFactory::createHammerPlantSeed),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("hammer flower", ItemFactory::createHammerFlower),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("space snapper seeds", ItemFactory::createSpaceSnapperSeed),

			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("beef", ItemFactory::createBeef),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("chicken", ItemFactory::createChickenMeat),

			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("fence", ItemFactory::createFenceItem),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("gate", ItemFactory::createGateItem),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("sprinkler", ItemFactory::createSprinklerItem),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("pump", ItemFactory::createPumpItem),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("chest", ItemFactory::createChestItem),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("light", ItemFactory::createLightItem),

			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("ship part", ItemFactory::createShipPart),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("teleport device", ItemFactory::createTeleportDevice),

			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("fishing rod", ItemFactory::createFishingRod),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("lava eel", ItemFactory::createLavaEel),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("salmon", ItemFactory::createSalmon),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("yak3", ItemFactory::createYak3),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("netty", ItemFactory::createNetty),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("lola", ItemFactory::createLola),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("larry", ItemFactory::createLarry),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("braydan", ItemFactory::createBraydan),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("harry", ItemFactory::createHarry),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("mr krabs", ItemFactory::createMrKrabs),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("phar lap", ItemFactory::createPharLap),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("bryton", ItemFactory::createBryton),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("sanders", ItemFactory::createSanders),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("churchill", ItemFactory::createChurchill),
			new AbstractMap.SimpleEntry<String, Supplier<Entity>>("golden statue", ItemFactory::createGoldenFish));

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

		int numItems = 1;
		try {
			numItems = Integer.parseInt(args.get(0));
			args.remove(0);
		} catch (Exception e) {
			// number of items not included
			// prettiest code ever hey
		}

		for (String partOfName : args) {
			str.add(partOfName.toLowerCase());
		}

		for (int i = 0; i < numItems; i++) {
			Entity item;
			try {
				item = itemFactories.get(str.toString()).get();
			} catch (Exception e) {
				logger.info("Incorrect item name given to addItem command.");
				return false;
			}
			ServiceLocator.getGameArea().spawnEntity(item);
			player.getComponent(InventoryComponent.class).addItem(item);
		}

		return true;
	}

	boolean isValid(ArrayList<String> args) {
		return !args.isEmpty();
	}

}
