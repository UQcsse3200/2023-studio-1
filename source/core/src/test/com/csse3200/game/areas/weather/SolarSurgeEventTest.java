package com.csse3200.game.areas.weather;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.sound.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SolarSurgeEventTest {

    private SolarSurgeEvent solarSurgeEvent1, solarSurgeEvent2, solarSurgeEvent3, solarSurgeEvent4, solarSurgeEvent5;
    private static final Logger logger = LoggerFactory.getLogger(SolarSurgeEventTest.class);

    @BeforeEach
    public void setUp() {
        GameArea gameArea = mock(GameArea.class);
        ClimateController climateController = new ClimateController();
        when(gameArea.getClimateController()).thenReturn(climateController);
        ServiceLocator.registerGameArea(gameArea);

        solarSurgeEvent1 = new SolarSurgeEvent(0, 9, 1, 1.2f);
        solarSurgeEvent2= new SolarSurgeEvent(1, 2, 2, 1.4f);
        solarSurgeEvent3 = new SolarSurgeEvent(2, 4, 5, 1.0f);
        solarSurgeEvent4= new SolarSurgeEvent(3, 3, 3, 1.3f);
        solarSurgeEvent5 = new SolarSurgeEvent(5, 5, 1, 1.1f);

        SoundService soundService = mock(SoundService.class);
        when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
        java.util.List<SoundFile> effects = new ArrayList<>();
        effects.add(EffectSoundFile.SOLAR_SURGE);
        ServiceLocator.registerSoundService(soundService);
        try {
            soundService.getEffectsMusicService().loadSounds(effects);
        } catch (InvalidSoundFileException e) {
            logger.info("Sound files not loaded");
        }

        TimeService timeService = mock(TimeService.class);
        ServiceLocator.registerTimeService(timeService);

        LightService lightService = mock(LightService.class);
        ServiceLocator.registerLightService(lightService);
    }

    @AfterEach
    public void cleanUp() {
        ServiceLocator.clear();
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

    @Test
    void testStartEffect() throws InvalidSoundFileException {
        solarSurgeEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        solarSurgeEvent2.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        solarSurgeEvent3.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(0.0f);
    }

    @Test
    void testStopEffect() {
        solarSurgeEvent1.stopEffect();
        solarSurgeEvent2.stopEffect();
        solarSurgeEvent3.stopEffect();
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(1.0f);
    }

}