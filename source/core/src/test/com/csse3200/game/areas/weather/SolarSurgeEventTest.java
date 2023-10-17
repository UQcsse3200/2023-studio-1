package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Color;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.*;
import com.csse3200.game.services.sound.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    public void initialiseEvents() {
        solarSurgeEvent1 = new SolarSurgeEvent(0, 9, 1, 1.2f);
        solarSurgeEvent2 = new SolarSurgeEvent(1, 2, 2, 1.4f);
        solarSurgeEvent3 = new SolarSurgeEvent(2, 4, 5, 1.0f);
        solarSurgeEvent4 = new SolarSurgeEvent(3, 3, 3, 0.6f);
        solarSurgeEvent5 = new SolarSurgeEvent(5, 5, 1, 0.7f);
    }
    @Test
    void testIsExpired() {
        initialiseEvents();
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
        initialiseEvents();
        assertFalse(solarSurgeEvent1.isExpired());
        assertTrue(solarSurgeEvent1.isActive());
    }

    @Test
    void testStartEffectLightingSystem() {
        initialiseEvents();
        solarSurgeEvent1.startEffect();
        solarSurgeEvent2.startEffect();
        solarSurgeEvent3.startEffect();
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(0.0f);
    }

    @Test
    void testGetAuroraColourOffset() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        initialiseEvents();
        SolarSurgeEvent solarSurgeEvent = new SolarSurgeEvent(0,10,1,1.2f);
        Method method = SolarSurgeEvent.class.getDeclaredMethod("getAuroraColourOffset", float.class);
        method.setAccessible(true);
        Color expectedColour = new Color(0.83f, 0.83f, 0.83f, 0.0f);
        Color actualColour = (Color) method.invoke(solarSurgeEvent, 1.0f);
        assertEquals(expectedColour.r, actualColour.r);
        assertEquals(expectedColour.g, actualColour.g);
        assertEquals(expectedColour.b, actualColour.b);
        assertEquals(expectedColour.a, actualColour.a);
    }

    @Test
    void testStartEffectTriggersEvents() {
        initialiseEvents();
        solarSurgeEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startWaterLevelEffect", 0.0082f);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", 2f);
        solarSurgeEvent4.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startWaterLevelEffect", 0.0046f);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", 1f);
        verify(ServiceLocator.getLightService(), times(2)).setBrightnessMultiplier(0.0f);
    }

    @Test
    void testStartEffectPlaysSound() throws InvalidSoundFileException {
        initialiseEvents();
        solarSurgeEvent4.startEffect();
        solarSurgeEvent5.startEffect();
        verify(ServiceLocator.getSoundService().getEffectsMusicService(), times(2)).play(EffectSoundFile.SOLAR_SURGE, true);
    }

    @Test
    void testStopEffect() {
        initialiseEvents();
        solarSurgeEvent1.stopEffect();
        solarSurgeEvent2.stopEffect();
        solarSurgeEvent3.stopEffect();
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(1.0f);
    }

    @Test
    void testTriggerSurge() throws InvalidSoundFileException {
        initialiseEvents();
        solarSurgeEvent5.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("surge");
        solarSurgeEvent5.stopEffect();
        SolarSurgeEvent solarSurgeEvent6 = new SolarSurgeEvent(0,9,1,1.3f);
        solarSurgeEvent6.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("surge");
        verify(ServiceLocator.getSoundService().getEffectsMusicService(), times(2)).play(EffectSoundFile.SURGE);
    }

    @Test
    void testDoesNotThrowInvalidSoundFileException() {
        try {
            ServiceLocator.registerSoundService(new SoundService());
            SolarSurgeEvent exceptionSolarSurgeEvent = new SolarSurgeEvent(5,5,5,5);
            exceptionSolarSurgeEvent.startEffect();
            ServiceLocator.getGameArea().getClimateController().getEvents().trigger("surge");
            SoundService mockSound = mock(SoundService.class);
            EffectsMusicService mockEffect = mock(EffectsMusicService.class);
            ServiceLocator.registerSoundService(mockSound);
            when(mockSound.getEffectsMusicService()).thenReturn(mockEffect);
            doThrow(InvalidSoundFileException.class).when(mockEffect).stop(EffectSoundFile.SOLAR_SURGE, 0);
            exceptionSolarSurgeEvent.stopEffect();
            ServiceLocator.getGameArea().getClimateController().getEvents().trigger("surge");
        } catch (InvalidSoundFileException e) {
            fail();
        }
        assertTrue(true);
    }

}