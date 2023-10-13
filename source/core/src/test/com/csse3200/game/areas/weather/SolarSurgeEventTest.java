package com.csse3200.game.areas.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolarSurgeEventTest {

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
    void testIsExpired() {
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
    void testIsActiveButNotIsExpired() {
        assertFalse(solarSurgeEvent1.isExpired());
        assertTrue(solarSurgeEvent1.isActive());
    }

}