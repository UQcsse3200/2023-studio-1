package com.csse3200.game.areas.weather;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class ClimateControllerTest {

	ClimateController controller;

	@BeforeEach
	public void setUp() {
		GameTime gameTime = mock(GameTime.class);
		TimeService timeService = mock(TimeService.class);
		ServiceLocator.registerTimeSource(gameTime);
		ServiceLocator.registerTimeService(timeService);
		EventHandler handler = new EventHandler();
		when(timeService.getEvents()).thenReturn(handler);
		controller = new ClimateController();
	}

	@AfterEach
	public void clear() {
		ServiceLocator.clear();
	}

	@Test
	public void testAddingEvent() {
		WeatherEvent event = new AcidShowerEvent(1, 1, 1, 0.5f);
		controller.addWeatherEvent(event);

		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertEquals(controller.getCurrentWeatherEvent(), event);

		// Removing the event
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
	}

	@Test
	public void testValidTemperature() {
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		float temperature = controller.getTemperature();

		// Temperature has to be within max and min which is 0 and 40 degrees
		assertTrue(temperature >= 0);
		assertTrue(temperature <= 40);
	}

	@Test
	public void testValidHumidity() {
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		float humidity = controller.getHumidity();

		// Temperature has to be within max and min which is 0 and 40 degrees
		assertTrue(humidity >= 0f);
		assertTrue(humidity <= 1f);
	}

	@Test
	public void testNoEventAdded() {
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

	@Test
	public void testAddedEvent() {
		assertNull(controller.getCurrentWeatherEvent());
		try (MockedStatic<MathUtils> mathUtils = mockStatic(MathUtils.class)) {
			mathUtils.when(MathUtils::random).thenReturn(1f);
			mathUtils.when(() -> MathUtils.random(anyInt(), anyInt())).thenReturn(1);
			ServiceLocator.getTimeService().getEvents().trigger("dayUpdate");
			// Therefore event should be created
			ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
			assertNotNull(controller.getCurrentWeatherEvent());
		}
	}

	@Test
	public void testExpiringEvent() {
		WeatherEvent event = new SolarSurgeEvent(1, 1, 1, 0.5f);
		controller.addWeatherEvent(event);

		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertEquals(event, controller.getCurrentWeatherEvent());
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

	@Test
	public void testAddEventWithNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			controller.addWeatherEvent(null);
		});
	}

	@Test
	public void testAddInstantEvent() {
		AcidShowerEvent event = new AcidShowerEvent(0, 1, 2, 0.5f);
		controller.addWeatherEvent(event);
		assertEquals(controller.getCurrentWeatherEvent(), event);
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

	@Test
	public void testOverridenEvent() {
		AcidShowerEvent event = new AcidShowerEvent(0, 1, 2, 0.5f);
		controller.addWeatherEvent(event);
		assertEquals(controller.getCurrentWeatherEvent(), event);
		AcidShowerEvent event2 = new AcidShowerEvent(0, 1, 10, 0.5f);
		controller.addWeatherEvent(event2);
		assertEquals(controller.getCurrentWeatherEvent(), event2);
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

}