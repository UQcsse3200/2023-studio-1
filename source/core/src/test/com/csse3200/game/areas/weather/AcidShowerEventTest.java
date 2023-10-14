package com.csse3200.game.areas.weather;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AcidShowerEventTest {

    private AcidShowerEvent acidShowerEvent1, acidShowerEvent2, acidShowerEvent3, acidShowerEvent4, acidShowerEvent5;

    @BeforeEach
    public void setUp() {
        GameArea gameArea = mock(GameArea.class);
        ClimateController climateController = new ClimateController();
        when(gameArea.getClimateController()).thenReturn(climateController);
        ServiceLocator.registerGameArea(gameArea);

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
    void testIsExpired() {
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
    void testIsActiveButNotIsExpired() {
        assertFalse(acidShowerEvent1.isExpired());
        assertTrue(acidShowerEvent1.isActive());
    }

    @Test
    void testStartEffect() {
        acidShowerEvent1.startEffect();
        acidShowerEvent2.startEffect();
        acidShowerEvent3.startEffect();
        verify(ServiceLocator.getParticleService(), times(3)).startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        acidShowerEvent4.startEffect();
        acidShowerEvent5.startEffect();
        verify(ServiceLocator.getParticleService(), times(5)).startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }

    @Test
    void testStopEffect() {
        acidShowerEvent1.stopEffect();
        acidShowerEvent2.stopEffect();
        acidShowerEvent3.stopEffect();
        verify(ServiceLocator.getParticleService(), times(3)).stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        acidShowerEvent4.stopEffect();
        acidShowerEvent5.stopEffect();
        verify(ServiceLocator.getParticleService(), times(5)).stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }
}