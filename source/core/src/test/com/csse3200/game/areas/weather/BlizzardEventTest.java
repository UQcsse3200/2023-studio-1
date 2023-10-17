package com.csse3200.game.areas.weather;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.sound.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BlizzardEventTest {

    private BlizzardEvent blizzardEvent1, blizzardEvent2, blizzardEvent3, blizzardEvent4, blizzardEvent5;

    @BeforeEach
    public void setUp() {
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
        blizzardEvent1 = new BlizzardEvent(0, 9, 1, 1.2f);
        blizzardEvent2 = new BlizzardEvent(1, 2, 2, 1.4f);
        blizzardEvent3 = new BlizzardEvent(2, 4, 5, 1.0f);
        blizzardEvent4 = new BlizzardEvent(3, 3, 3, 0.6f);
        blizzardEvent5 = new BlizzardEvent(5, 5, 1, 0.7f);
    }

    @Test
    void testStartEffectParticleAndLightingSystem() throws InvalidSoundFileException {
        initialiseEvents();
        blizzardEvent1.startEffect();
        blizzardEvent2.startEffect();
        blizzardEvent3.startEffect();
        verify(ServiceLocator.getParticleService(), times(3)).startEffect(ParticleService.ParticleEffectType.BLIZZARD);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.54f);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.5133333f);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.56666666f);
    }

    @Test
    void testHasSpawnedFireflies() {
        GameArea gameArea = mock(GameArea.class);
        ClimateController mockClimateController = mock(ClimateController.class);
        when(gameArea.getClimateController()).thenReturn(mockClimateController);
        when(mockClimateController.getEvents()).thenReturn(mock(EventHandler.class));
        TimeService timeService = mock(TimeService.class);
        ServiceLocator.registerTimeService(timeService);
        when(timeService.isNight()).thenReturn(false);
        ServiceLocator.registerGameArea(gameArea);
        initialiseEvents();
        blizzardEvent1.startEffect();
        blizzardEvent1.startEffect();
        verify(mockClimateController.getEvents()).trigger("spawnFireflies");
    }

    @Test
    void testStartEffectTriggersEvents() {
        initialiseEvents();
        blizzardEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        blizzardEvent2.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        blizzardEvent4.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -1f);
        verify(ServiceLocator.getParticleService(), times(3)).startEffect(ParticleService.ParticleEffectType.BLIZZARD);
    }

    @Test
    void testStartEffectPlaysSound() throws InvalidSoundFileException {
        initialiseEvents();
        blizzardEvent4.startEffect();
        blizzardEvent5.startEffect();
        verify(ServiceLocator.getSoundService().getEffectsMusicService(), times(2)).play(EffectSoundFile.BLIZZARD, true);
    }

    @Test
    void testStopEffect() {
        initialiseEvents();
        blizzardEvent1.stopEffect();
        blizzardEvent2.stopEffect();
        blizzardEvent3.stopEffect();
        verify(ServiceLocator.getParticleService(), times(3)).stopEffect(ParticleService.ParticleEffectType.BLIZZARD);
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(1.0f);
    }

    @Test
    @Order(1)
    void testThrowsInvalidSoundFileException() {
        try (MockedStatic<LoggerFactory> mockedStatic = mockStatic(LoggerFactory.class)) {
            final Logger testLogger = mock(Logger.class);
            final Logger fakeLogger = mock(Logger.class);
            mockedStatic.when(() -> LoggerFactory.getLogger(BackgroundMusicService.class)).thenReturn(fakeLogger);
            mockedStatic.when(() -> LoggerFactory.getLogger(SoundService.class)).thenReturn(fakeLogger);
            mockedStatic.when(() -> LoggerFactory.getLogger(BlizzardEvent.class)).thenReturn(testLogger);
            ServiceLocator.registerSoundService(new SoundService());
            BlizzardEvent exceptionBlizzardEvent = new testBlizzardEvent(5,5,5,5);
            exceptionBlizzardEvent.startEffect();
            verify(testLogger).error(anyString(), any(InvalidSoundFileException.class));

            SoundService mockSound = mock(SoundService.class);
            EffectsMusicService mockEffect = mock(EffectsMusicService.class);
            ServiceLocator.registerSoundService(mockSound);
            when(mockSound.getEffectsMusicService()).thenReturn(mockEffect);
            doThrow(InvalidSoundFileException.class).when(mockEffect).stop(EffectSoundFile.BLIZZARD, 0);
            exceptionBlizzardEvent.stopEffect();
            verify(testLogger, times(2)).error(anyString(), any(InvalidSoundFileException.class));
        } catch (InvalidSoundFileException e) {
            fail();
        }
    }

    public class testBlizzardEvent extends BlizzardEvent {
        public Logger logger = LoggerFactory.getLogger(BlizzardEvent.class);
        public testBlizzardEvent(int numHoursUntil, int duration, int priority, float severity) {
            super(numHoursUntil, duration, priority, severity);
        }
    }
}
