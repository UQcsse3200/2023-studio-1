package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.sound.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolarSurgeEventTest {

    private SolarSurgeEvent solarSurgeEvent1, solarSurgeEvent2, solarSurgeEvent3, solarSurgeEvent4, solarSurgeEvent5;
    private static final Logger logger = LoggerFactory.getLogger(SolarSurgeEventTest.class);
    private GameTime gameTime;

    @BeforeEach
    public void setUp() {
        gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        GameArea gameArea = mock(GameArea.class);
        ClimateController climateController = new ClimateController();
        when(gameArea.getClimateController()).thenReturn(climateController);
        ServiceLocator.registerGameArea(gameArea);

        solarSurgeEvent1 = new SolarSurgeEvent(0, 9, 1, 1.2f);
        solarSurgeEvent2= new SolarSurgeEvent(1, 2, 2, 1.4f);
        solarSurgeEvent3 = new SolarSurgeEvent(2, 4, 5, 1.0f);
        solarSurgeEvent4= new SolarSurgeEvent(3, 3, 3, 0.6f);
        solarSurgeEvent5 = new SolarSurgeEvent(5, 5, 1, 0.7f);

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
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", 2f);
        solarSurgeEvent2.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", 2f);
        solarSurgeEvent3.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", 2f);
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(0.0f);
    }

    @Test
    void testStartEffectLightingSystem() {
        solarSurgeEvent1.startEffect();
        solarSurgeEvent2.startEffect();
        solarSurgeEvent3.startEffect();
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(0.0f);
    }

    @Test
    void testGetAuroraColourOffset() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SolarSurgeEvent solarSurgeEvent = new SolarSurgeEvent(0,10,1,1.2f);
        Method method = SolarSurgeEvent.class.getDeclaredMethod("getAuroraColourOffset", float.class);
        method.setAccessible(true);
        Color expectedColour = new Color(0.83f, 0.8887397f, 0.98375493f, 0.0f);
        Color actualColour = (Color) method.invoke(solarSurgeEvent, 1.0f);
        assertEquals(expectedColour.r, actualColour.r);
        assertEquals(expectedColour.g, actualColour.g);
        assertEquals(expectedColour.b, actualColour.b);
        assertEquals(expectedColour.a, actualColour.a);
    }

    @Test
    void testStartEffectTriggersEvents() {
        solarSurgeEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startWaterLevelEffect", 0.0082f);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", 2f);
        solarSurgeEvent4.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startWaterLevelEffect", 0.0046f);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", 1f);
    }

    @Test
    void testStartEffectPlaysSound() throws InvalidSoundFileException {
        solarSurgeEvent4.startEffect();
        solarSurgeEvent5.startEffect();
        verify(ServiceLocator.getSoundService().getEffectsMusicService(), times(2)).play(EffectSoundFile.SOLAR_SURGE, true);
    }

    @Test
    void testStopEffect() {
        solarSurgeEvent1.stopEffect();
        solarSurgeEvent2.stopEffect();
        solarSurgeEvent3.stopEffect();
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(1.0f);
    }

    @Test
    void testTriggerSurge() throws InvalidSoundFileException {
        SolarSurgeEvent solarSurgeEvent6 = new SolarSurgeEvent(0,9,1,1.3f);
        solarSurgeEvent6.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("surge");
        verify(ServiceLocator.getSoundService().getEffectsMusicService()).play(EffectSoundFile.SURGE);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPanicEffect");
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("damagePlants");
    }

}