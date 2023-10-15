package com.csse3200.game.areas.weather;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.*;
import com.csse3200.game.services.sound.EffectsMusicService;
import com.csse3200.game.services.sound.SoundService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class ClimateControllerTest {

	ClimateController controller;

	@BeforeEach
	public void setUp() {
		GameTime gameTime = mock(GameTime.class);
		TimeService timeService = mock(TimeService.class);
		LightService lightService = mock(LightService.class);
		ServiceLocator.registerTimeSource(gameTime);
		ServiceLocator.registerTimeService(timeService);
		ServiceLocator.registerParticleService(mock(ParticleService.class));
		ServiceLocator.registerLightService(lightService);
		EventHandler handler = new EventHandler();
		when(timeService.getEvents()).thenReturn(handler);
		controller = new ClimateController();
		controller.initialiseEvents();
	}

	@AfterEach
	public void clear() {
		ServiceLocator.clear();
	}

	// These tests will require dealing with the lighting system
	@Test
	void testAddingEvent() {
		GameArea gameArea = mock(GameArea.class);
		when(gameArea.getClimateController()).thenReturn(controller);
		ServiceLocator.registerGameArea(gameArea);

		SoundService soundService = mock(SoundService.class);
		when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
		ServiceLocator.registerSoundService(soundService);

		WeatherEvent event = new AcidShowerEvent(1, 1, 1, 1.3f);
		controller.addWeatherEvent(event);

		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertEquals(controller.getCurrentWeatherEvent(), event);

		// Removing the event
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
	}

	@Test
	void testNoEventAdded() {
		assertNull(controller.getCurrentWeatherEvent());
		try (MockedStatic<MathUtils> mathUtils = mockStatic(MathUtils.class)) {
			mathUtils.when(MathUtils::random).thenReturn(0f);
			ServiceLocator.getTimeService().getEvents().trigger("dayUpdate");
			for (int i = 0; i < 50; i++) {
				ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
				assertNull(controller.getCurrentWeatherEvent());
			}
		}
	}

	// This test has been removed as it will require updates to deal with lighting effects
	@Test
	void testAddedEvent1() {
		GameArea gameArea = mock(GameArea.class);
		when(gameArea.getClimateController()).thenReturn(controller);
		ServiceLocator.registerGameArea(gameArea);

		SoundService soundService = mock(SoundService.class);
		when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
		ServiceLocator.registerSoundService(soundService);

		assertNull(controller.getCurrentWeatherEvent());
		try (MockedStatic<MathUtils> mathUtils = mockStatic(MathUtils.class)) {
			mathUtils.when(MathUtils::random).thenReturn(1f);
			mathUtils.when(() -> MathUtils.random(anyInt(), anyInt())).thenReturn(1);
			ServiceLocator.getTimeService().getEvents().trigger("dayUpdate");
			// Therefore event should be created
			ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
			assertNotNull(controller.getCurrentWeatherEvent());
			assertTrue(controller.getCurrentWeatherEvent() instanceof RainStormEvent);
		}
	}

//	 These tests will require dealing with the lighting system
	@Test
	void testAddDailyEventCase0() {
		assertNull(controller.getCurrentWeatherEvent());

		GameArea gameArea = mock(GameArea.class);
		when(gameArea.getClimateController()).thenReturn(controller);
		ServiceLocator.registerGameArea(gameArea);

		SoundService soundService = mock(SoundService.class);
		when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
		ServiceLocator.registerSoundService(soundService);

		try (MockedStatic<MathUtils> mathUtils = mockStatic(MathUtils.class)) {
			mathUtils.when(MathUtils::random).thenReturn(0.5f);
			mathUtils.when(() -> MathUtils.random(0, 9)).thenReturn(0); // weatherEvent - RainStorm
			mathUtils.when(() -> MathUtils.random(1, 6)).thenReturn(1); // numHoursUntil
			mathUtils.when(() -> MathUtils.random(2, 5)).thenReturn(4); // duration
			mathUtils.when(() -> MathUtils.random(0, 3)).thenReturn(0); // priority
			mathUtils.when(MathUtils::random).thenReturn(0.7f);          // severity

			ServiceLocator.getTimeService().getEvents().trigger("dayUpdate");
			// Therefore event should be created
			ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
			assertNotNull(controller.getCurrentWeatherEvent());
			assertTrue(controller.getCurrentWeatherEvent() instanceof RainStormEvent);
		}
	}

	@Test
	void testExpiringEvent() {
		GameArea gameArea = mock(GameArea.class);
		when(gameArea.getClimateController()).thenReturn(controller);
		ServiceLocator.registerGameArea(gameArea);

		SoundService soundService = mock(SoundService.class);
		when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
		ServiceLocator.registerSoundService(soundService);

		WeatherEvent event = new SolarSurgeEvent(1, 1, 1, 1.4f);
		controller.addWeatherEvent(event);

		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertEquals(event, controller.getCurrentWeatherEvent());
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

	@Test
	void testAddEventWithNull() {
		assertThrows(IllegalArgumentException.class, () -> controller.addWeatherEvent(null));
	}

	// These tests will require dealing with the lighting system
	@Test
	void testAddInstantEvent() {
		GameArea gameArea = mock(GameArea.class);
		when(gameArea.getClimateController()).thenReturn(controller);
		ServiceLocator.registerGameArea(gameArea);

		SoundService soundService = mock(SoundService.class);
		when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
		ServiceLocator.registerSoundService(soundService);

		AcidShowerEvent event = new AcidShowerEvent(0, 1, 2, 1.5f);
		controller.addWeatherEvent(event);
		assertEquals(controller.getCurrentWeatherEvent(), event);
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

	@Test
	void testOverridenEvent() {
		GameArea gameArea = mock(GameArea.class);
		when(gameArea.getClimateController()).thenReturn(controller);
		ServiceLocator.registerGameArea(gameArea);

		SoundService soundService = mock(SoundService.class);
		when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
		ServiceLocator.registerSoundService(soundService);

		AcidShowerEvent event = new AcidShowerEvent(0, 1, 2, 1.5f);
		controller.addWeatherEvent(event);
		assertEquals(controller.getCurrentWeatherEvent(), event);
		AcidShowerEvent event2 = new AcidShowerEvent(0, 1, 10, 1.2f);
		controller.addWeatherEvent(event2);
		assertEquals(controller.getCurrentWeatherEvent(), event2);
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

	@Test
	void testSetValues() {
		GameArea gameArea = mock(GameArea.class);
		when(gameArea.getClimateController()).thenReturn(controller);
		ServiceLocator.registerGameArea(gameArea);

		SoundService soundService = mock(SoundService.class);
		when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
		ServiceLocator.registerSoundService(soundService);

		JsonValue jsonData = new JsonValue(JsonValue.ValueType.object);
		JsonValue events = new JsonValue(JsonValue.ValueType.object);
		JsonValue event = new JsonValue(JsonValue.ValueType.object);
		event.addChild("name", new JsonValue("AcidShowerEvent"));
		event.addChild("hoursUntil", new JsonValue(1));
		event.addChild("duration", new JsonValue(2));
		event.addChild("priority", new JsonValue(1));
		event.addChild("severity", new JsonValue(1.5f));
		events.addChild("Event", event);
		jsonData.addChild("Events", events);

		assertNull(controller.getCurrentWeatherEvent());

		controller.setValues(jsonData);
		assertNull(controller.getCurrentWeatherEvent());

		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertTrue(controller.getCurrentWeatherEvent() instanceof AcidShowerEvent);

		AcidShowerEvent currentEvent = (AcidShowerEvent) controller.getCurrentWeatherEvent();
		assertEquals(0, currentEvent.getNumHoursUntil());
		assertEquals(2, currentEvent.getDuration());
		assertEquals(1, currentEvent.getPriority());
		assertEquals(1.5f, currentEvent.getSeverity(), 0.001f);
	}
}