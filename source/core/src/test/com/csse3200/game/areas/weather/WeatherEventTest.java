package com.csse3200.game.areas.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherEventTest {

    private WeatherEvent weatherEvent1, weatherEvent2, weatherEvent3, weatherEvent4, weatherEvent5;
    private AcidShowerEvent acidShowerEvent1, acidShowerEvent2, acidShowerEvent3, acidShowerEvent4, acidShowerEvent5;
    private SolarSurgeEvent solarSurgeEvent1, solarSurgeEvent2, solarSurgeEvent3, solarSurgeEvent4, solarSurgeEvent5;

    @BeforeEach
    public void setUp() {
        weatherEvent1 = new WeatherEvent(0, 9, 1, 1.2f) { };
        weatherEvent2 = new WeatherEvent(1, 2, 2, 1.4f) { };
        weatherEvent3 = new WeatherEvent(2, 4, 5, 1.0f) { };
        weatherEvent4 = new WeatherEvent(3, 3, 3, 1.3f) { };
        weatherEvent5 = new WeatherEvent(5, 5, 1, 1.1f) { };
        acidShowerEvent1 = new AcidShowerEvent(0, 9, 1, 1.2f);
        acidShowerEvent2 = new AcidShowerEvent(1, 2, 2, 1.4f);
        acidShowerEvent3 = new AcidShowerEvent(2, 4, 5, 1.0f);
        acidShowerEvent4 = new AcidShowerEvent(3, 3, 3, 1.3f);
        acidShowerEvent5 = new AcidShowerEvent(5, 5, 1, 1.1f);
        solarSurgeEvent1 = new SolarSurgeEvent(0, 9, 1, 1.2f);
        solarSurgeEvent2= new SolarSurgeEvent(1, 2, 2, 1.4f);
        solarSurgeEvent3 = new SolarSurgeEvent(2, 4, 5, 1.0f);
        solarSurgeEvent4= new SolarSurgeEvent(3, 3, 3, 1.3f);
        solarSurgeEvent5 = new SolarSurgeEvent(5, 5, 1, 1.1f);
    }

    @Test
    public void testUpdateTime() {
        WeatherEvent weatherEventSpy1 = spy(weatherEvent1);
        WeatherEvent weatherEventSpy2 = spy(weatherEvent2);
        WeatherEvent weatherEventSpy3 = spy(weatherEvent3);
        WeatherEvent weatherEventSpy4 = spy(weatherEvent4);
        WeatherEvent weatherEventSpy5 = spy(weatherEvent5);
        //weatherEvent1 should be active as numHoursUntil = 0
        assertTrue(weatherEventSpy1.isActive());
        weatherEventSpy2.updateTime();
        weatherEventSpy3.updateTime();
        weatherEventSpy4.updateTime();
        weatherEventSpy5.updateTime();
        //weatherEvent2 should be active as numHoursUntil = 1
        assertTrue(weatherEventSpy2.isActive());
        verify(weatherEventSpy2, times(1)).isActive();
        verify(weatherEventSpy3, times(0)).isActive();
        //Updating time again so that weatherEvent3 becomes active
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
        //Events should expire after their duration ends
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

    @Test
    public void testWeatherEventGetHumidityModifier() {
        assertEquals(weatherEvent1.getHumidityModifier(), 1.0f);
        assertEquals(weatherEvent2.getHumidityModifier(), 1.0f);
        assertEquals(weatherEvent3.getHumidityModifier(), 1.0f);
        assertEquals(weatherEvent4.getHumidityModifier(), 1.0f);
        assertEquals(weatherEvent5.getHumidityModifier(), 1.0f);
    }

    @Test
    public void testWeatherEventGetTemperatureModifier() {
        assertEquals(weatherEvent1.getTemperatureModifier(), 1.0f);
        assertEquals(weatherEvent2.getTemperatureModifier(), 1.0f);
        assertEquals(weatherEvent3.getTemperatureModifier(), 1.0f);
        assertEquals(weatherEvent4.getTemperatureModifier(), 1.0f);
        assertEquals(weatherEvent5.getTemperatureModifier(), 1.0f);
    }

    @Test
    public void testAcidShowerEventGetHumidityModifier() {
        assertEquals(0.23f, acidShowerEvent1.getHumidityModifier(), 0.00001);
        assertEquals(0.26f, acidShowerEvent2.getHumidityModifier(), 0.00001);
        assertEquals(0.2f, acidShowerEvent3.getHumidityModifier(), 0.00001);
        assertEquals(0.245f, acidShowerEvent4.getHumidityModifier(), 0.00001);
        assertEquals(0.215f, acidShowerEvent5.getHumidityModifier(), 0.00001);
    }

    @Test
    public void testAcidShowerEventGetTemperatureModifier() {
        assertEquals(-11.0f, acidShowerEvent1.getTemperatureModifier(), 0.00001);
        assertEquals(-12.0f, acidShowerEvent2.getTemperatureModifier(), 0.00001);
        assertEquals(-10.0f, acidShowerEvent3.getTemperatureModifier(), 0.00001);
        assertEquals(-11.5f, acidShowerEvent4.getTemperatureModifier(), 0.00001);
        assertEquals(-10.5f, acidShowerEvent5.getTemperatureModifier(), 0.00001);
    }

    @Test
    public void testSolarSurgeEventGetHumidityModifier() {
        assertEquals(-0.44f, solarSurgeEvent1.getHumidityModifier(), 0.00001);
        assertEquals(-0.48f, solarSurgeEvent2.getHumidityModifier(), 0.00001);
        assertEquals(-0.4f, solarSurgeEvent3.getHumidityModifier(), 0.00001);
        assertEquals(-0.46f, solarSurgeEvent4.getHumidityModifier(), 0.00001);
        assertEquals(-0.42f, solarSurgeEvent5.getHumidityModifier(), 0.00001);
    }

    @Test
    public void testSolarSurgeEventGetTemperatureModifier() {
        assertEquals(33.0f, solarSurgeEvent1.getTemperatureModifier(), 0.00001);
        assertEquals(36.0f, solarSurgeEvent2.getTemperatureModifier(), 0.00001);
        assertEquals(30.0f, solarSurgeEvent3.getTemperatureModifier(), 0.00001);
        assertEquals(34.5f, solarSurgeEvent4.getTemperatureModifier(), 0.00001);
        assertEquals(31.5f, solarSurgeEvent5.getTemperatureModifier(), 0.00001);
    }

    @Test
    public void testConstructorWithNegativePriority() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(4, 5, -1, 1.1f) {
        }, "Priority cannot be less than 0");
    }

    @Test
    public void testConstructorWithZeroDuration() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(0, 0, 1, 1.2f) {
        }, "Duration cannot be 0");
    }

    @Test
    public void testConstructorWithNegativeDuration() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(5, -1, 3, 1.3f) { }, "Duration cannot be less than 0");
    }

    @Test
    public void testConstructorWithNegativeNumHoursUntil() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(-1, 5, 1, 1.2f) { }, "Number of hours until the event occurs cannot be less than 0");
    }

    @Test
    public void testConstructorWithInvalidSeverity() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(2, 3, 1, 0.7f) { }, "Severity can't be less than 1.0f");
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(3, 5, 4, 1.7f) { }, "Severity can't be more than 1.5f");
        assertThrows(IllegalArgumentException.class, () -> new AcidShowerEvent(3, 5, 4, 0.4f) { }, "Severity can't be less than 1.0f");

        assertThrows(IllegalArgumentException.class, () -> new AcidShowerEvent(3, 5, 4, 1.7f) { }, "Severity can't be more than 1.5f");
        assertThrows(IllegalArgumentException.class, () -> new SolarSurgeEvent(1, 2, 4, 0.4f) { }, "Severity can't be less than 1.0f");
        assertThrows(IllegalArgumentException.class, () -> new SolarSurgeEvent(1, 2, 4, 1.6f) { }, "Severity can't be more than 1.6f");
    }
}
