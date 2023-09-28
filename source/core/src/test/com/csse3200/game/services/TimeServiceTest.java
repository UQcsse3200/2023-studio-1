package com.csse3200.game.services;

import box2dLight.RayHandler;
import com.csse3200.game.events.EventHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimeServiceTest {

	private GameTime gameTime;
	private TimeService timeService;

	@BeforeEach
	public void setup() {
		gameTime = mock(GameTime.class);
		ServiceLocator.registerTimeSource(gameTime);
		timeService = new TimeService();
	}

	@AfterEach
	public void clear() {
		ServiceLocator.clear();
	}

	@Test
	public void testConstructor() {
		assertEquals(6, timeService.getHour());
		assertEquals(0, timeService.getDay());
		assertEquals(0, timeService.getMinute());
		assertFalse(timeService.isPaused());
	}

	@Test
	public void testIsPaused() {
		timeService.setPaused(false);
		assertFalse(timeService.isPaused());
	}

	@Test
	public void testPause() {
		timeService.setPaused(true);
		verify(gameTime, times(1)).setTimeScale(0);
	}

	@Test
	public void testUnpause() {
		timeService.setPaused(false);
		verify(gameTime, times(1)).setTimeScale(1);
	}

	@Test
	public void testSetDay() {
		int time = 10;
		timeService.setDay(time);
		assertEquals(time, timeService.getDay());

		timeService.setDay(-1);
		assertEquals(time, timeService.getDay());
	}

	@Test
	public void testSetHour() {
		int time = 10;
		timeService.setHour(time);
		assertEquals(time, timeService.getHour());

		timeService.setHour(-1);
		assertEquals(time, timeService.getHour());

		timeService.setHour(25);
		assertEquals(time, timeService.getHour());
	}

	@Test
	public void testSetMinute() {
		int time = 10;
		timeService.setMinute(time);
		assertEquals(time, timeService.getMinute());

		timeService.setMinute(-10);
		assertEquals(time, timeService.getMinute());

		timeService.setMinute(70);
		assertEquals(time, timeService.getMinute());
	}

	@Test
	public void testUpdate() {
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
	public void testIsDay() {
		timeService.setHour(2);
		System.out.println(timeService.getHour());
		assertFalse(timeService.isDay());
		assertTrue(timeService.isNight());
		timeService.setHour(11);
		assertTrue(timeService.isDay());
		assertFalse(timeService.isNight());
	}

}