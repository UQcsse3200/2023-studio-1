package com.csse3200.game.ui.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;

@ExtendWith(GameExtension.class)
class SetDayCommandTest {

	SetDayCommand command;
	ArrayList<String> args;
	@BeforeEach
	void beforeEach() {
		ServiceLocator.clear();
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