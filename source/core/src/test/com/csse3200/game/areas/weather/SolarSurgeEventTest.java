package com.csse3200.game.areas.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SolarSurgeEventTest {

    private SolarSurgeEvent solarSurgeEvent1, solarSurgeEvent2, solarSurgeEvent3, solarSurgeEvent4, solarSurgeEvent5;

    @BeforeEach
    public void setUp() {
        solarSurgeEvent1 = new SolarSurgeEvent(0, 9, 1, 1.2f);
        solarSurgeEvent2= new SolarSurgeEvent(1, 2, 2, 1.4f);
        solarSurgeEvent3 = new SolarSurgeEvent(2, 4, 5, 1.0f);
        solarSurgeEvent4= new SolarSurgeEvent(3, 3, 3, 1.3f);
        solarSurgeEvent5 = new SolarSurgeEvent(5, 5, 1, 1.1f);
    }

    @Test
    public void testIsExpired() {
        // case: numHoursUntil == 0 and duration > 0
        assertFalse(solarSurgeEvent1.isExpired());
        // case: numHoursUntil > 0 and duration > 0
        assertFalse(solarSurgeEvent2.isExpired());
        assertFalse(solarSurgeEvent3.isExpired());
        for (int i = 0; i < 10; i++) {
            solarSurgeEvent1.updateTime();
            solarSurgeEvent2.updateTime();
            solarSurgeEvent3.updateTime();
        }
        // case: numHoursUntil == 0 and duration == 0
        assertTrue(solarSurgeEvent1.isExpired());
        assertTrue(solarSurgeEvent2.isExpired());
        assertTrue(solarSurgeEvent3.isExpired());
    }

    @Test
    public void testIsActiveButNotIsExpired() {
        assertFalse(solarSurgeEvent1.isExpired());
        assertTrue(solarSurgeEvent1.isActive());
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
}