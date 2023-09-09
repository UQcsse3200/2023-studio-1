package com.csse3200.game.ui.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import com.csse3200.game.entities.EntityService;
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

@ExtendWith(GameExtension.class)
class SetDayCommandTest {

	SetDayCommand command;
	ArrayList<String> args;
	@BeforeEach
	void beforeEach() {
		command = new SetDayCommand();
		args = new ArrayList<>();
	}

	@Test
	void testCommand() {

		ServiceLocator.registerTimeService(mock(TimeService.class));

		int day = 9;
		args.add(String.valueOf(day));

		command.action(args);
		verify(ServiceLocator.getTimeService(), times(1)).setDay(day);
	}

	@Test
	void testTooManyArgsValid() {
		args.add("9");
		args.add("9");
		assertFalse(command.isValid(args));
	}

	@Test
	void testTooManyArgsAction() {
		args.add("9");
		args.add("9");
		assertFalse(command.action(args));
	}

	@Test
	void invalidNumber() {
		args.add("wrong");
		assertFalse(command.isValid(args));
	}

	@Test
	void noArgs() {
		assertFalse(command.isValid(args));
	}
}