package com.csse3200.game.areas.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherEventTest {

    private WeatherEvent weatherEvent1, weatherEvent2, weatherEvent3, weatherEvent4, weatherEvent5;

    @BeforeEach
    public void setUp() {
        weatherEvent1 = new WeatherEvent(0, 10, 1, 1.2f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        };
        weatherEvent2 = new WeatherEvent(1, 2, 2, 1.4f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        };
        weatherEvent3 = new WeatherEvent(2, 4, 5, 1.0f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        };
        weatherEvent4 = new WeatherEvent(3, 3, 3, 1.3f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        };
        weatherEvent5 = new WeatherEvent(5, 5, 1, 1.1f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        };
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
        // case: numHoursUntil == 0 and duration > 0
        assertFalse(weatherEvent1.isExpired());
        // case: numHoursUntil > 0 and duration > 0
        assertFalse(weatherEvent2.isExpired());
        assertFalse(weatherEvent3.isExpired());
        assertFalse(weatherEvent4.isExpired());
        assertFalse(weatherEvent5.isExpired());
        for (int i = 0; i < 10; i++) {
            weatherEvent1.updateTime();
            weatherEvent2.updateTime();
            weatherEvent3.updateTime();
            weatherEvent4.updateTime();
            weatherEvent5.updateTime();
        }
        // case: numHoursUntil == 0 and duration == 0
        assertTrue(weatherEvent1.isExpired());
        assertTrue(weatherEvent2.isExpired());
        assertTrue(weatherEvent3.isExpired());
        assertTrue(weatherEvent4.isExpired());
        assertTrue(weatherEvent5.isExpired());
    }

    @Test
    public void testIsActiveButNotIsExpired() {
        assertFalse(weatherEvent1.isExpired());
        assertTrue(weatherEvent1.isActive());
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
    public void testConstructorWithNegativePriority() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(4, 5, -1, 1.1f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        }, "Priority cannot be less than 0");
    }

    @Test
    public void testConstructorWithZeroDuration() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(0, 0, 1, 1.2f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        }, "Duration cannot be 0");
    }

    @Test
    public void testConstructorWithNegativeDuration() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(5, -1, 3, 1.3f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        }, "Duration cannot be less than 0");
    }

    @Test
    public void testConstructorWithNegativeNumHoursUntil() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(-1, 5, 1, 1.2f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        }, "Number of hours until the event occurs cannot be less than 0");
    }

    @Test
    public void testConstructorWithInvalidSeverity() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(2, 3, 1, -1f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }
        }, "Severity must be greater than 0.");
    }
}
