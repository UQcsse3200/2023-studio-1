package com.csse3200.game.ui.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@ExtendWith(GameExtension.class)
class SetTimeScaleCommandTest {

	SetTimeScaleCommand command;
	ArrayList<String> args;

	@BeforeEach
	void beforeEach() {
		ServiceLocator.clear();
		command = new SetTimeScaleCommand();
		args = new ArrayList<>();
	}

	@Test
	void validTimeInput() {
		String timeArg = "10";
		args.add(timeArg);
		GameTime gameTime = mock(GameTime.class);
		ServiceLocator.registerTimeSource(gameTime);
		assertTrue(command.isValid(args));
		command.action(args);
		verify(gameTime, times(1)).setTimeScale(10);
	}

	@Test
	void tooManyArgs() {
		args.add("1.2");
		args.add("1.2");
		assertFalse(command.isValid(args));
		assertFalse(command.action(args));
	}

	@ParameterizedTest
	@ValueSource(strings = {"invalid", "0", "-1.1"})
	void invalidTimeScaleNumber() {
		args.add("invalid");
		assertFalse(command.isValid(args));
	}
}