package com.csse3200.game.areas.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testUpdateTime() {
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

    @Test
    public void testGetPriority() {
        assertEquals(weatherEvent1.getPriority(), 1);
        assertEquals(weatherEvent2.getPriority(), 2);
        assertEquals(weatherEvent3.getPriority(), 5);
        assertEquals(weatherEvent4.getPriority(), 3);
        assertEquals(weatherEvent5.getPriority(), 1);
    }

    @Test
    public void testIsExpired() {
        for (int i = 0; i < 10; i++) {
            weatherEvent1.updateTime();
            weatherEvent2.updateTime();
            weatherEvent3.updateTime();
            weatherEvent4.updateTime();
            weatherEvent5.updateTime();
        }
        assertTrue(weatherEvent1.isExpired());
        assertTrue(weatherEvent2.isExpired());
        assertTrue(weatherEvent3.isExpired());
        assertTrue(weatherEvent4.isExpired());
        assertTrue(weatherEvent5.isExpired());
    }

    @Test
    public void testGetSeverity() {
        assertEquals(weatherEvent1.getSeverity(), 1.2f);
        assertEquals(weatherEvent2.getSeverity(), 1.4f);
        assertEquals(weatherEvent3.getSeverity(), 1.0f);
        assertEquals(weatherEvent4.getSeverity(), 1.3f);
        assertEquals(weatherEvent5.getSeverity(), 1.1f);
    }
}
