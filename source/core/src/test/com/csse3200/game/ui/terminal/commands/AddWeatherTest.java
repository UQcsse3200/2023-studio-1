package com.csse3200.game.ui.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.weather.AcidShowerEvent;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.areas.weather.SolarSurgeEvent;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class AddWeatherTest {

	AddWeatherCommand command;
	ArrayList<String> args;

	@BeforeEach
	void beforeEach() {
		ServiceLocator.clear();
		command = new AddWeatherCommand();
		args = new ArrayList<>();
	}

	@Test
	void addSolarSurge() {
		GameArea gameArea = mock(GameArea.class);
		ClimateController climateController = spy(new ClimateController());

		ServiceLocator.registerGameArea(gameArea);
		when(gameArea.getClimateController()).thenReturn(climateController);
		args.add("solarSurge");
		args.add("1");
		args.add("2");
		args.add("1");
		args.add("1");

		command.action(args);
		verify(climateController, times(1)).addWeatherEvent(any(SolarSurgeEvent.class));
		assertTrue(command.action(args));
	}

	@Test
	void addAcidShower() {
		GameArea gameArea = mock(GameArea.class);
		ClimateController climateController = spy(new ClimateController());

		ServiceLocator.registerGameArea(gameArea);
		when(gameArea.getClimateController()).thenReturn(climateController);
		args.add("acidShower");
		args.add("1");
		args.add("2");
		args.add("1");
		args.add("1");

		command.action(args);
		verify(climateController, times(1)).addWeatherEvent(any(AcidShowerEvent.class));
		assertTrue(command.action(args));
	}

	@Test
	void invalidWeatherName() {
		args.add("invalidWeather");
		args.add("1");
		args.add("2");
		args.add("1");
		args.add("1");

		assertFalse(command.action(args));
	}

	@Test
	void invalidNumbers() {
		args.add("solarSurge");
		args.add("invalid");
		args.add("invalid");
		args.add("invalid");
		args.add("invalid");
		assertFalse(command.action(args));
	}

	@Test
	void invalidDuration() {
		args.add("solarSurge");
		args.add("-1");
		args.add("2");
		args.add("1");
		args.add("1");
		assertFalse(command.action(args));
	}

	@Test
	void invalidNumHoursUntil() {
		args.add("solarSurge");
		args.add("2");
		args.add("-1");
		args.add("1");
		args.add("1");
		assertFalse(command.action(args));
	}

	@Test
	void invalidPriority() {
		args.add("solarSurge");
		args.add("2");
		args.add("1");
		args.add("-1");
		args.add("1");
		assertFalse(command.action(args));
	}

	@Test
	void invalidSeverity() {
		args.add("solarSurge");
		args.add("2");
		args.add("1");
		args.add("1");
		args.add("-1");
		assertFalse(command.action(args));
	}

	@Test
	void invalidNumArgs() {
		args.add("solarSurge");
		args.add("solarSurge");
		args.add("1");
		args.add("2");
		args.add("1");
		args.add("1");
		assertFalse(command.isValid(args));
		assertFalse(command.action(args));
	}

	@Test
	void validNumArgs() {
		args.add("solarSurge");
		args.add("1");
		args.add("2");
		args.add("1");
		args.add("1");
		assertTrue(command.isValid(args));
	}

	@Test
	void noArgs() {
		assertFalse(command.isValid(args));
		assertFalse(command.action(args));
	}
}