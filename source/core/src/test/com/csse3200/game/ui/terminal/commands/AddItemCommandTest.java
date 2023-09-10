package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class AddItemCommandTest {

	AddItemCommand command;
	ArrayList<String> args;
	InventoryComponent inventoryComponent;

	@BeforeEach
	void beforeEach() {
		ServiceLocator.clear();
		command = new AddItemCommand();
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
	public void tooManyArgs() {
		args.add("wrong");
		args.add("item");
		assertFalse(command.isValid(args));
	}

	@Test
	public void addHoe() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("hoe");
			Entity hoe = new Entity();
			factory.when(ItemFactory::createHoe).thenReturn(hoe);
			command.action(args);
			factory.verify(ItemFactory::createHoe);
			verify(inventoryComponent).addItem(hoe);
		}
	}

	@Test
	public void addCan() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("can");
			Entity can = new Entity();
			factory.when(ItemFactory::createWateringcan).thenReturn(can);
			command.action(args);
			factory.verify(ItemFactory::createWateringcan);
			verify(inventoryComponent).addItem(can);
		}
	}

	@Test
	public void addShovel() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("shovel");
			Entity shovel = new Entity();
			factory.when(ItemFactory::createShovel).thenReturn(shovel);
			command.action(args);
			factory.verify(ItemFactory::createShovel);
			verify(inventoryComponent).addItem(shovel);
		}
	}

	@Test
	public void addScythe() {
		try (MockedStatic<ItemFactory> factory = mockStatic(ItemFactory.class)) {
			args.add("scythe");
			Entity scythe = new Entity();
			factory.when(ItemFactory::createScythe).thenReturn(scythe);
			command.action(args);
			factory.verify(ItemFactory::createScythe);
			verify(inventoryComponent).addItem(scythe);
		}
	}

	@Test
	public void invalidToolName() {
		args.add("wrongName");
		assertFalse(command.action(args));
	}

}