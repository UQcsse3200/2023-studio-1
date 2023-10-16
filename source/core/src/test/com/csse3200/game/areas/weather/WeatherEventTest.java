package com.csse3200.game.areas.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.utils.Json;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeatherEventTest {

    private WeatherEvent weatherEvent1, weatherEvent2, weatherEvent3, weatherEvent4, weatherEvent5;

    @BeforeEach
    public void setUp() {
        GameArea gameArea = mock(GameArea.class);
        ClimateController climateController = new ClimateController();
        when(gameArea.getClimateController()).thenReturn(climateController);
        ServiceLocator.registerGameArea(gameArea);

        weatherEvent1 = new WeatherEvent(0, 10, 1, 1.2f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        };
        weatherEvent2 = new WeatherEvent(1, 2, 2, 1.4f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        };
        weatherEvent3 = new WeatherEvent(2, 4, 5, 1.0f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        };
        weatherEvent4 = new WeatherEvent(3, 3, 3, 1.3f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        };
        weatherEvent5 = new WeatherEvent(5, 5, 1, 1.1f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        };
    }

    @AfterEach
    public void cleanUp() {
        ServiceLocator.clear();
    }

    @Test
    void testUpdateTime() {
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
    void testGetDuration() {
        assertEquals(10, weatherEvent1.getDuration());
        assertEquals(2, weatherEvent2.getDuration());
        assertEquals(4, weatherEvent3.getDuration());
        assertEquals(3, weatherEvent4.getDuration());
        assertEquals(5, weatherEvent5.getDuration());
    }

    @Test
    void testGetNumHoursUntil() {
        assertEquals(0, weatherEvent1.getNumHoursUntil());
        assertEquals(1, weatherEvent2.getNumHoursUntil());
        assertEquals(2, weatherEvent3.getNumHoursUntil());
        assertEquals(3, weatherEvent4.getNumHoursUntil());
        assertEquals(5, weatherEvent5.getNumHoursUntil());
    }

    @Test
    void testGetPriority() {
        assertEquals(1, weatherEvent1.getPriority());
        assertEquals(2, weatherEvent2.getPriority());
        assertEquals(5, weatherEvent3.getPriority());
        assertEquals(3, weatherEvent4.getPriority());
        assertEquals(1, weatherEvent5.getPriority());
    }

    @Test
    void testIsExpired() {
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
    void testIsActiveButNotIsExpired() {
        assertFalse(weatherEvent1.isExpired());
        assertTrue(weatherEvent1.isActive());
    }

    @Test
    void testGetSeverity() {
        assertEquals(1.2f, weatherEvent1.getSeverity());
        assertEquals(1.4f, weatherEvent2.getSeverity());
        assertEquals(1.0f, weatherEvent3.getSeverity());
        assertEquals(1.3f, weatherEvent4.getSeverity());
        assertEquals(1.1f, weatherEvent5.getSeverity());
    }

    @Test
    void testConstructorWithNegativePriority() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(4, 5, -1, 1.1f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        }, "Priority cannot be less than 0");
    }

    @Test
    void testConstructorWithZeroDuration() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(0, 0, 1, 1.2f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        }, "Duration cannot be 0");
    }

    @Test
    void testConstructorWithNegativeDuration() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(5, -1, 3, 1.3f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        }, "Duration cannot be less than 0");
    }

    @Test
    void testConstructorWithNegativeNumHoursUntil() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(-1, 5, 1, 1.2f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        }, "Number of hours until the event occurs cannot be less than 0");
    }

    @Test
    void testConstructorWithInvalidSeverity() {
        assertThrows(IllegalArgumentException.class, () -> new WeatherEvent(2, 3, 1, -1f) {
            @Override
            public void startEffect() {

            }

            @Override
            public void stopEffect() {

            }

            @Override
            public String toString() {
                return null;
            }
        }, "Severity must be greater than 0.");
    }

    @Test
    void testWrite() {
        Json mockJson1 = mock(Json.class);
        weatherEvent1.write(mockJson1);
        verify(mockJson1).writeObjectStart("Event");
        verify(mockJson1).writeValue("name", weatherEvent1.getClass().getSimpleName());
        verify(mockJson1).writeValue("hoursUntil", 0);
        verify(mockJson1).writeValue("duration", 10);
        verify(mockJson1).writeValue("priority", 1);
        verify(mockJson1).writeValue("severity", 1.2f);
        verify(mockJson1).writeObjectEnd();
        Json mockJson2 = mock(Json.class);
        weatherEvent2.write(mockJson2);
        verify(mockJson2).writeObjectStart("Event");
        verify(mockJson2).writeValue("name", weatherEvent1.getClass().getSimpleName());
        verify(mockJson2).writeValue("hoursUntil", 1);
        verify(mockJson2).writeValue("duration", 2);
        verify(mockJson2).writeValue("priority", 2);
        verify(mockJson2).writeValue("severity", 1.4f);
        verify(mockJson2).writeObjectEnd();
        Json mockJson3 = mock(Json.class);
        weatherEvent3.write(mockJson3);
        verify(mockJson3).writeObjectStart("Event");
        verify(mockJson3).writeValue("name", weatherEvent1.getClass().getSimpleName());
        verify(mockJson3).writeValue("hoursUntil", 2);
        verify(mockJson3).writeValue("duration", 4);
        verify(mockJson3).writeValue("priority", 5);
        verify(mockJson3).writeValue("severity", 1.0f);
        verify(mockJson3).writeObjectEnd();
    }
}
