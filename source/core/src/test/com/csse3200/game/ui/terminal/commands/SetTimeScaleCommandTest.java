package com.csse3200.game.ui.terminal.commands;

import com.badlogic.gdx.Game;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


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
	public void validTimeInput() {
		String timeArg = "10";
		args.add(timeArg);
		GameTime gameTime = mock(GameTime.class);
		ServiceLocator.registerTimeSource(gameTime);
		assertTrue(command.isValid(args));
		command.action(args);
		verify(gameTime, times(1)).setTimeScale(10);
	}

	@Test
	public void tooManyArgs() {
		args.add("1.2");
		args.add("1.2");
		assertFalse(command.isValid(args));
		assertFalse(command.action(args));
	}

	@Test
	public void invalidTimeScaleNumber() {
		args.add("invalid");
		assertFalse(command.isValid(args));
	}

	@Test
	public void invalidTimeScaleValue1() {
		args.add("0");
		assertFalse(command.isValid(args));
	}

	@Test
	public void invalidTimeScaleValue2() {
		args.add("-1.1");
		assertFalse(command.isValid(args));
	}
}