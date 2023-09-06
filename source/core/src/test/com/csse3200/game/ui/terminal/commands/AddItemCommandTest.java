package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class AddItemCommandTest {

	SetTimeCommand command;
	ArrayList<String> args;

	@BeforeEach
	void beforeEach() {
		command = new SetTimeCommand();
		args = new ArrayList<>();
		ServiceLocator.clear();
	}

	@Test
	public void tooManyArgs() {
		args.add("wrong");
		args.add("item");
		assertFalse(command.isValid(args));
	}

	@Test
	public void invalidToolName() {
		args.add("wrongName");
		assertFalse(command.action(args));
	}

}