package com.csse3200.game.ui.terminal.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;

import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;

@ExtendWith(GameExtension.class)
class SpawnCommandTest {

	SpawnCommand command;
	ArrayList<String> args;
	Entity player;
	EntityService entityService;
	Vector2 playerPosition;

	@BeforeEach
	void beforeEach() {
		command = new SpawnCommand();
		args = new ArrayList<>();

		GameArea gameArea = mock(SpaceGameArea.class);
		ServiceLocator.registerGameArea(gameArea);

		entityService = mock(EntityService.class);
		ServiceLocator.registerEntityService(entityService);

		player = mock(Entity.class);
		when(gameArea.getPlayer()).thenReturn(player);
		playerPosition = new Vector2(0, 0);
		when(player.getPosition()).thenReturn(playerPosition);
	}

	@Test
	void testCowOnPlayer() {
		args.add("cow");
		try (MockedStatic<NPCFactory> factory = mockStatic(NPCFactory.class)) {
			Entity cow = mock(Entity.class);
			factory.when(() -> NPCFactory.createCow(player)).thenReturn(cow);
			command.action(args);
			factory.verify(() -> NPCFactory.createCow(player), times(1));
			verify(player, times(1)).getPosition();
			verify(cow).setPosition(playerPosition);
			verify(entityService).register(cow);
		}
	}

	@Test
	void testCowAtPosition() {
		args.add("cow");
		args.add("1");
		args.add("1");
		Vector2 position = new Vector2(1, 1);
		try (MockedStatic<NPCFactory> factory = mockStatic(NPCFactory.class)) {
			Entity cow = mock(Entity.class);
			factory.when(() -> NPCFactory.createCow(player)).thenReturn(cow);
			command.action(args);
			factory.verify(() -> NPCFactory.createCow(player), times(1));
			verify(player, times(0)).getPosition();
			verify(cow).setPosition(position);
			verify(entityService).register(cow);
		}
	}

	@Test
	void testPositionIncorrect() {
		args.add("cow");
		args.add("1");
		args.add("NaN");
		Vector2 position = new Vector2(1, 1);
		try (MockedStatic<NPCFactory> factory = mockStatic(NPCFactory.class)) {
			Entity cow = mock(Entity.class);
			factory.when(() -> NPCFactory.createCow(player)).thenReturn(cow);
			assertFalse(command.action(args));
			factory.verify(() -> NPCFactory.createCow(player), times(1));
			verify(player, times(0)).getPosition();
			verify(cow,times(0)).setPosition(position);
			verify(entityService, times(0)).register(cow);
		}
	}

	@Test
	void testNoArgs() {
		assertFalse(command.isValid(args));
	}

	@Test
	void testTooManyArgs() {
		args.add("cow");
		args.add("cow");
		args.add("cow");
		args.add("cow");
		assertFalse(command.isValid(args));
	}

	@Test
	void testIncorrectAnimalName() {
		args.add("animal");
		assertFalse(command.action(args));
	}
}