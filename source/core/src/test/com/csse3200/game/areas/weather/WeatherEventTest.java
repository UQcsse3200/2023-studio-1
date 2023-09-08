package com.csse3200.game.areas.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WeatherEventTest {

    private WeatherEvent weatherEvent1, weatherEvent2, weatherEvent3, weatherEvent4, weatherEvent5;

    @BeforeEach
    public void setUp() {
        weatherEvent1 = new WeatherEvent(0, 9, 1, 1.2f) { };
        weatherEvent2 = new WeatherEvent(1, 2, 2, 1.4f) { };
        weatherEvent3 = new WeatherEvent(2, 4, 5, 1.0f) { };
        weatherEvent4 = new WeatherEvent(3, 3, 3, 1.3f) { };
        weatherEvent5 = new WeatherEvent(5, 5, 1, 1.1f) { };
    }

    @Test
    public void testUpdateTimeDecreasesCountdown() {
        WeatherEvent weatherEventSpy2 = spy(weatherEvent2);
        WeatherEvent weatherEventSpy3 = spy(weatherEvent3);
        WeatherEvent weatherEventSpy4 = spy(weatherEvent4);
        WeatherEvent weatherEventSpy5 = spy(weatherEvent5);
        weatherEventSpy2.updateTime();
        weatherEventSpy3.updateTime();
        weatherEventSpy4.updateTime();
        weatherEventSpy5.updateTime();
        assertTrue(weatherEventSpy2.isActive());
        verify(weatherEventSpy2, times(1)).isActive();
        verify(weatherEventSpy3, times(0)).isActive();
        weatherEventSpy3.updateTime();
        assertTrue(weatherEventSpy3.isActive());
        verify(weatherEventSpy3, times(1)).isActive();
        verify(weatherEventSpy4, times(0)).isActive();
        verify(weatherEventSpy5, times(0)).isActive();
    }
}
