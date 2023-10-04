package com.csse3200.game.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeServiceTest {

	private GameTime gameTime;
	private TimeService timeService;

	@BeforeEach
	void setup() {
		gameTime = mock(GameTime.class);
		ServiceLocator.registerTimeSource(gameTime);
		timeService = new TimeService();
	}

	@AfterEach
	void clear() {
		ServiceLocator.clear();
	}

	@Test
	void testConstructor() {
		assertEquals(6, timeService.getHour());
		assertEquals(0, timeService.getDay());
		assertEquals(0, timeService.getMinute());
		assertFalse(timeService.isPaused());
	}

	@Test
	void testIsPaused() {
		timeService.setPaused(false);
		assertFalse(timeService.isPaused());
	}

	@Test
	void testPause() {
		timeService.setPaused(true);
		verify(gameTime, times(1)).setTimeScale(0);
	}

	@Test
	void testUnpause() {
		timeService.setPaused(false);
		verify(gameTime, times(1)).setTimeScale(1);
	}

	@Test
	void testSetDay() {
		int time = 10;
		timeService.setDay(time);
		assertEquals(time, timeService.getDay());

		timeService.setDay(-1);
		assertEquals(time, timeService.getDay());
	}

	@Test
	void testSetHour() {
		int time = 10;
		timeService.setHour(time);
		assertEquals(time, timeService.getHour());

		timeService.setHour(-1);
		assertEquals(time, timeService.getHour());

		timeService.setHour(25);
		assertEquals(time, timeService.getHour());
	}

	@Test
	void testSetMinute() {
		int time = 10;
		timeService.setMinute(time);
		assertEquals(time, timeService.getMinute());

		timeService.setMinute(-10);
		assertEquals(time, timeService.getMinute());

		timeService.setMinute(70);
		assertEquals(time, timeService.getMinute());
	}

	@Test
	void testUpdate() {
		when(gameTime.getDeltaTime()).thenReturn(0.50f);
		assertEquals(6, timeService.getHour());
		assertEquals(0, timeService.getMinute());
		timeService.update();
		assertEquals(1, timeService.getMinute());
		timeService.update();
		assertEquals(2, timeService.getMinute());

		timeService.setMinute(59);
		timeService.update();
		assertEquals(0, timeService.getMinute());
		assertEquals(7, timeService.getHour());

		timeService.setMinute(59);
		timeService.setHour(23);
		timeService.update();
		assertEquals(0, timeService.getMinute());
		assertEquals(0, timeService.getHour());
		assertEquals(1, timeService.getDay());

	}

	@Test
	void testIsDay() {
		timeService.setHour(2);
		System.out.println(timeService.getHour());
		assertFalse(timeService.isDay());
		assertTrue(timeService.isNight());
		timeService.setHour(11);
		assertTrue(timeService.isDay());
		assertFalse(timeService.isNight());
	}

}