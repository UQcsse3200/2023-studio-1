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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RainStormEventTest {

    private RainStormEvent rainStormEvent1, rainStormEvent2, rainStormEvent3, rainStormEvent4, rainStormEvent5;
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
        ServiceLocator.registerSoundService(soundService);

        TimeService timeService = mock(TimeService.class);
        ServiceLocator.registerTimeService(timeService);


        ParticleService mockParticleService = mock(ParticleService.class);
        ServiceLocator.registerParticleService(mockParticleService);
        LightService lightService = mock(LightService.class);
        ServiceLocator.registerLightService(lightService);
    }

    @AfterEach
    public void cleanUp() {
        ServiceLocator.clear();
    }
    public void initialiseEvents() {
        rainStormEvent1 = new RainStormEvent(0, 9, 1, 1.2f);
        rainStormEvent2 = new RainStormEvent(1, 2, 2, 1.4f);
        rainStormEvent3 = new RainStormEvent(2, 4, 5, 1.0f);
        rainStormEvent4 = new RainStormEvent(3, 3, 3, 0.6f);
        rainStormEvent5 = new RainStormEvent(5, 5, 1, 0.7f);
    }

    @Test
    void testStartEffectParticleAndLightingSystem() {
        initialiseEvents();
        rainStormEvent1.startEffect();
        //TODO
        verify(ServiceLocator.getParticleService(), times(1)).startEffect(ParticleService.ParticleEffectType.RAIN);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.66f);
    }

    @Test
    void testGetLightningColourOffset() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        initialiseEvents();
        RainStormEvent rainStormEvent6 = new RainStormEvent(0,10,1,1.2f);
        Method method = RainStormEvent.class.getDeclaredMethod("getLightningColourOffset", float.class);
        method.setAccessible(true);
        Color expectedBrightness = new Color(0.032f, 0.032f, 0.032f, 0.0f);
        Color actualBrightness = (Color) method.invoke(rainStormEvent6, 1.0f);
        assertEquals(expectedBrightness.r, actualBrightness.r);
        assertEquals(expectedBrightness.g, actualBrightness.g);
        assertEquals(expectedBrightness.b, actualBrightness.b);
    }

    @Test
    void testStartEffectTriggersEvents() {
        initialiseEvents();
        rainStormEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startWaterLevelEffect", -0.0016f);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("douseFlames");
        verify(ServiceLocator.getParticleService(), times(1)).startEffect(ParticleService.ParticleEffectType.RAIN);

    }

    @Test
    void testStartEffectPlaysSound() throws InvalidSoundFileException {
        initialiseEvents();
        rainStormEvent4.startEffect();
        rainStormEvent5.startEffect();
        verify(ServiceLocator.getSoundService().getEffectsMusicService(), times(2)).play(EffectSoundFile.STORM, true);
    }

    @Test
    void testStopEffect() {
        initialiseEvents();
        rainStormEvent1.stopEffect();
        rainStormEvent2.stopEffect();
        rainStormEvent3.stopEffect();
        //TODO - update
        verify(ServiceLocator.getParticleService(), times(3)).stopEffect(ParticleService.ParticleEffectType.RAIN);
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(1.0f);
    }

    @Test
    void testTriggerStrike() throws InvalidSoundFileException {
        initialiseEvents();
        RainStormEvent rainStormEvent6 = new RainStormEvent(0,9,1,1.3f);
        rainStormEvent6.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("lightningStrike");
        verify(ServiceLocator.getSoundService().getEffectsMusicService()).play(EffectSoundFile.LIGHTNING_STRIKE);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPanicEffect");
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("lightningEffect");
    }

    @Test
    @Order(1)
    void testThrowsInvalidSoundFileException() {
        try (MockedStatic<LoggerFactory> mockedStatic = mockStatic(LoggerFactory.class)) {
            final Logger testLogger = mock(Logger.class);
            final Logger fakeLogger = mock(Logger.class);
            mockedStatic.when(() -> LoggerFactory.getLogger(BackgroundMusicService.class)).thenReturn(fakeLogger);
            mockedStatic.when(() -> LoggerFactory.getLogger(SoundService.class)).thenReturn(fakeLogger);
            mockedStatic.when(() -> LoggerFactory.getLogger(RainStormEvent.class)).thenReturn(testLogger);
            ServiceLocator.registerSoundService(new SoundService());
            RainStormEvent exceptionRainStormEvent = new testRainStormEvent(5,5,5,5);
            exceptionRainStormEvent.startEffect();
            verify(testLogger).error(anyString(), any(InvalidSoundFileException.class));
            ServiceLocator.getGameArea().getClimateController().getEvents().trigger("lightningStrike");
            verify(testLogger, times(2)).error(anyString(), any(InvalidSoundFileException.class));

            SoundService mockSound = mock(SoundService.class);
            EffectsMusicService mockEffect = mock(EffectsMusicService.class);
            ServiceLocator.registerSoundService(mockSound);
            when(mockSound.getEffectsMusicService()).thenReturn(mockEffect);
            doThrow(InvalidSoundFileException.class).when(mockEffect).stop(EffectSoundFile.STORM, 0);
            exceptionRainStormEvent.stopEffect();
            verify(testLogger, times(3)).error(anyString(), any(InvalidSoundFileException.class));
            ServiceLocator.getGameArea().getClimateController().getEvents().trigger("lightningStrike");
            verify(testLogger, times(3)).error(anyString(), any(InvalidSoundFileException.class));
        } catch (InvalidSoundFileException e) {
            fail();
        }
    }

    public class testRainStormEvent extends RainStormEvent {
        public Logger logger = LoggerFactory.getLogger(RainStormEvent.class);
        public testRainStormEvent(int numHoursUntil, int duration, int priority, float severity) {
            super(numHoursUntil, duration, priority, severity);
        }
    }
}
