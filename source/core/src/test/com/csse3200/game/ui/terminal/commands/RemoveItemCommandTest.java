package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class RemoveItemCommandTest {

	RemoveItemCommand command;
	ArrayList<String> args;
	InventoryComponent inventoryComponent;

	@BeforeEach
	void beforeEach() {
		ServiceLocator.clear();
		command = new RemoveItemCommand();
		args = new ArrayList<>();
		GameArea gameArea = mock(SpaceGameArea.class);
		ServiceLocator.registerGameArea(gameArea);
		Entity player = mock(Entity.class);
		inventoryComponent = mock(InventoryComponent.class);
		player.addComponent(inventoryComponent);
		when(ServiceLocator.getGameArea().getPlayer()).thenReturn(player);
		when(ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class)).thenReturn(inventoryComponent);
	}

	@Test
	void tooManyArgs() {
		args.add("wrong");
		args.add("item");
		assertFalse(command.isValid(args));
	}

	@Test
	void removeHoe() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("hoe");
			Entity hoe = new Entity();
            inventoryComponent.addItem(hoe);
			factory.when(ItemFactory::createHoe).thenReturn(hoe);
			command.action(args);
			verify(inventoryComponent).removeItem(hoe);
		}
	}

	@Test
	void removeCan() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("can");
			Entity can = new Entity();
            inventoryComponent.addItem(can);
            factory.when(ItemFactory::createWateringcan).thenReturn(can);
			command.action(args);
			verify(inventoryComponent).removeItem(can);
		}
	}

	@Test
	void removeShovel() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("shovel");
			Entity shovel = new Entity();
            inventoryComponent.addItem(shovel);
            factory.when(ItemFactory::createShovel).thenReturn(shovel);
			command.action(args);
			verify(inventoryComponent).removeItem(shovel);
		}
	}

	@Test
	void removeScythe() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("scythe");
			Entity scythe = new Entity();
            inventoryComponent.addItem(scythe);
            factory.when(ItemFactory::createScythe).thenReturn(scythe);
			command.action(args);
			verify(inventoryComponent).removeItem(scythe);
		}
	}

	@Test
	void invalidToolName() {
		args.add("wrongName");
		assertFalse(command.action(args));
	}

}