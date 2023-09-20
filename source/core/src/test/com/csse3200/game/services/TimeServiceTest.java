package com.csse3200.game.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;

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
	}

	@Test
	public void testSetHour() {
		int time = 10;
		timeService.setHour(time);
		assertEquals(time, timeService.getHour());
	}

	@Test
	public void testSetMinute() {
		int time = 10;
		timeService.setMinute(time);
		assertEquals(time, timeService.getMinute());
	}

	@Test
	public void testSleep() {
		timeService.speedUpSleep();
		verify(gameTime, times(0)).setTimeScale(15);
		timeService.setHour(11);
		timeService.speedUpSleep();
		verify(gameTime, times(1)).setTimeScale(15);
	}

	@Test
	public void testNormalSpeed() {
		timeService.getEvents().trigger("morningTime");
		verify(gameTime, times(1)).setTimeScale(1);
	}

	@Test
	public void testIsDay() {
		timeService.setHour(2);
		assertFalse(timeService.isDay());
		assertTrue(timeService.isNight());
		timeService.setHour(11);
		assertTrue(timeService.isDay());
		assertFalse(timeService.isNight());
	}

}