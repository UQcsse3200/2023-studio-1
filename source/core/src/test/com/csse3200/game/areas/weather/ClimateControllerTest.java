package com.csse3200.game.areas.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.services.ParticleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;

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
		ServiceLocator.registerParticleService(mock(ParticleService.class));
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
	public void testAddedEvent1() {
		assertNull(controller.getCurrentWeatherEvent());
		try (MockedStatic<MathUtils> mathUtils = mockStatic(MathUtils.class)) {
			mathUtils.when(MathUtils::random).thenReturn(1f);
			mathUtils.when(() -> MathUtils.random(anyInt(), anyInt())).thenReturn(1);
			ServiceLocator.getTimeService().getEvents().trigger("dayUpdate");
			// Therefore event should be created
			ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
			assertNotNull(controller.getCurrentWeatherEvent());
			assertTrue(controller.getCurrentWeatherEvent() instanceof SolarSurgeEvent);
		}
	}

	@Test
	public void testAddDailyEventCase0() {
		assertNull(controller.getCurrentWeatherEvent());

		try (MockedStatic<MathUtils> mathUtils = mockStatic(MathUtils.class)) {
			mathUtils.when(MathUtils::random).thenReturn(0.5f);
			mathUtils.when(() -> MathUtils.random(0, 1)).thenReturn(0); //
			mathUtils.when(() -> MathUtils.random(1, 6)).thenReturn(1); // numHoursUntil
			mathUtils.when(() -> MathUtils.random(2, 5)).thenReturn(4); // duration
			mathUtils.when(() -> MathUtils.random(0, 3)).thenReturn(0); // priority
			mathUtils.when(MathUtils::random).thenReturn(0.7f);          // severity

			ServiceLocator.getTimeService().getEvents().trigger("dayUpdate");
			// Therefore event should be created
			ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
			assertNotNull(controller.getCurrentWeatherEvent());
			assertTrue(controller.getCurrentWeatherEvent() instanceof AcidShowerEvent);
		}
	}

	@Test
	public void testExpiringEvent() {
		WeatherEvent event = new SolarSurgeEvent(1, 1, 1, 1.4f);
		controller.addWeatherEvent(event);

		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertEquals(event, controller.getCurrentWeatherEvent());
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

	@Test
	public void testAddEventWithNull() {
		assertThrows(IllegalArgumentException.class, () -> controller.addWeatherEvent(null));
	}

	@Test
	public void testAddInstantEvent() {
		AcidShowerEvent event = new AcidShowerEvent(0, 1, 2, 1.5f);
		controller.addWeatherEvent(event);
		assertEquals(controller.getCurrentWeatherEvent(), event);
		ServiceLocator.getTimeService().getEvents().trigger("hourUpdate");
		assertNull(controller.getCurrentWeatherEvent());
	}

	@Test
	public void testOverridenEvent() {
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
	public void testSetValues() {
		Json json = new Json();
		JsonValue jsonData = new JsonValue(JsonValue.ValueType.object);
		jsonData.addChild("Temp", new JsonValue(26.0f));
		assertEquals(26.0f, jsonData.getFloat("Temp"));
		jsonData.addChild("Humidity", new JsonValue(1.2f));
		assertEquals(1.2f, jsonData.getFloat("Humidity"));
		JsonValue events = new JsonValue(JsonValue.ValueType.object);
		JsonValue event = new JsonValue(JsonValue.ValueType.object);
		event.addChild("name", new JsonValue("AcidShowerEvent"));
		event.addChild("hoursUntil", new JsonValue(1));
		event.addChild("duration", new JsonValue(2));
		event.addChild("priority", new JsonValue(1));
		event.addChild("severity", new JsonValue(1.5f));
		events.addChild("Event", event);
		jsonData.addChild("Events", events);
		assertEquals(events, jsonData.get("Events"));

		controller.setValues(json, jsonData);

		assertEquals(26.0f, controller.getTemperature());
		assertEquals(1.2f, controller.getHumidity());
	}

}