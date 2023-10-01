package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AcidShowerEventTest {

    private AcidShowerEvent acidShowerEvent1, acidShowerEvent2, acidShowerEvent3, acidShowerEvent4, acidShowerEvent5;

    @BeforeEach
    public void setUp() {
        acidShowerEvent1 = new AcidShowerEvent(0, 9, 1, 1.2f);
        acidShowerEvent2 = new AcidShowerEvent(1, 2, 2, 1.4f);
        acidShowerEvent3 = new AcidShowerEvent(2, 4, 5, 1.0f);
        acidShowerEvent4 = new AcidShowerEvent(3, 3, 3, 1.3f);
        acidShowerEvent5 = new AcidShowerEvent(5, 5, 1, 1.1f);
        ParticleService mockParticleService = mock(ParticleService.class);
        ServiceLocator.registerParticleService(mockParticleService);
    }

    @AfterEach
    public void cleanUp() {
        ServiceLocator.clear();
    }

    @Test
    public void testIsExpired() {
        // case: numHoursUntil == 0 and duration > 0
        assertFalse(acidShowerEvent1.isExpired());
        // case: numHoursUntil > 0 and duration > 0
        assertFalse(acidShowerEvent2.isExpired());
        assertFalse(acidShowerEvent3.isExpired());
        for (int i = 0; i < 10; i++) {
            acidShowerEvent1.updateTime();
            acidShowerEvent2.updateTime();
            acidShowerEvent3.updateTime();
        }
        // case: numHoursUntil == 0 and duration == 0
        assertTrue(acidShowerEvent1.isExpired());
        assertTrue(acidShowerEvent2.isExpired());
        assertTrue(acidShowerEvent3.isExpired());
    }

    @Test
    public void testIsActiveButNotIsExpired() {
        assertFalse(acidShowerEvent1.isExpired());
        assertTrue(acidShowerEvent1.isActive());
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
    public void testStartEffect() {
        acidShowerEvent1.startEffect();
        acidShowerEvent2.startEffect();
        acidShowerEvent3.startEffect();
        verify(ServiceLocator.getParticleService(), times(3)).startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        acidShowerEvent4.startEffect();
        acidShowerEvent5.startEffect();
        verify(ServiceLocator.getParticleService(), times(5)).startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }

    @Test
    public void testStopEffect() {
        acidShowerEvent1.stopEffect();
        acidShowerEvent2.stopEffect();
        acidShowerEvent3.stopEffect();
        verify(ServiceLocator.getParticleService(), times(3)).stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        acidShowerEvent4.stopEffect();
        acidShowerEvent5.stopEffect();
        verify(ServiceLocator.getParticleService(), times(5)).stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }
}