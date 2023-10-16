package com.csse3200.game.areas.weather;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.*;
import com.csse3200.game.services.sound.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AcidShowerEventTest {

    private AcidShowerEvent acidShowerEvent1, acidShowerEvent2, acidShowerEvent3, acidShowerEvent4, acidShowerEvent5;
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
        acidShowerEvent1 = new AcidShowerEvent(0, 9, 1, 1.2f);
        acidShowerEvent2 = new AcidShowerEvent(1, 2, 2, 1.4f);
        acidShowerEvent3 = new AcidShowerEvent(2, 4, 5, 1.0f);
        acidShowerEvent4 = new AcidShowerEvent(3, 3, 3, 1.3f);
        acidShowerEvent5 = new AcidShowerEvent(5, 5, 1, 1.1f);
    }

    @Test
    void testIsExpired() {
        initialiseEvents();
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
        initialiseEvents();
        assertFalse(acidShowerEvent1.isExpired());
        assertTrue(acidShowerEvent1.isActive());
    }

    @Test
    void testStartEffectParticleAndLightingSystem() {
        initialiseEvents();
        acidShowerEvent1.startEffect();
        verify(ServiceLocator.getParticleService(), times(1)).startEffect(ParticleService.ParticleEffectType.RAIN);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.79f);
    }

    @Test
    void testStartEffectTriggersEvents() {
        initialiseEvents();
        acidShowerEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startWaterLevelEffect", -0.0003f);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("douseFlames");
    }

    @Test
    void testStartEffectPlaysSound() throws InvalidSoundFileException {
        initialiseEvents();
        acidShowerEvent4.startEffect();
        acidShowerEvent5.startEffect();
        verify(ServiceLocator.getSoundService().getEffectsMusicService(), times(2)).play(EffectSoundFile.STORM, true);
    }

    @Test
    void testStopEffect() {
        initialiseEvents();
        acidShowerEvent1.stopEffect();
        acidShowerEvent2.stopEffect();
        acidShowerEvent3.stopEffect();
        verify(ServiceLocator.getParticleService(), times(3)).stopEffect(ParticleService.ParticleEffectType.RAIN);
        acidShowerEvent4.stopEffect();
        acidShowerEvent5.stopEffect();
        verify(ServiceLocator.getParticleService(), times(5)).stopEffect(ParticleService.ParticleEffectType.RAIN);
    }

    @Test
    void testTriggerAcidBurn() throws InvalidSoundFileException {
        AcidShowerEvent acidShowerEvent6 = new AcidShowerEvent(0,9,1,1.2f);
        acidShowerEvent6.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("acidBurn");
        verify(ServiceLocator.getSoundService().getEffectsMusicService()).play(EffectSoundFile.ACID_BURN);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPanicEffect");
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("damagePlants");
    }

    @Test
    @Order(1)
    void testThrowsInvalidSoundFileException() {
        try (MockedStatic<LoggerFactory> mockedStatic = mockStatic(LoggerFactory.class)) {
            final Logger testLogger = mock(Logger.class);
            final Logger fakeLogger = mock(Logger.class);
            mockedStatic.when(() -> LoggerFactory.getLogger(BackgroundMusicService.class)).thenReturn(fakeLogger);
            mockedStatic.when(() -> LoggerFactory.getLogger(SoundService.class)).thenReturn(fakeLogger);
            mockedStatic.when(() -> LoggerFactory.getLogger(AcidShowerEvent.class)).thenReturn(testLogger);
            ServiceLocator.registerSoundService(new SoundService());
            AcidShowerEvent exceptionAcidShowerEvent = new testAcidShowerEvent(5,5,5,5);
            exceptionAcidShowerEvent.startEffect();
            verify(testLogger).error(anyString(), any(InvalidSoundFileException.class));
            ServiceLocator.getGameArea().getClimateController().getEvents().trigger("acidBurn");
            verify(testLogger, times(2)).error(anyString(), any(InvalidSoundFileException.class));

            SoundService mockSound = mock(SoundService.class);
            EffectsMusicService mockEffect = mock(EffectsMusicService.class);
            ServiceLocator.registerSoundService(mockSound);
            when(mockSound.getEffectsMusicService()).thenReturn(mockEffect);
            doThrow(InvalidSoundFileException.class).when(mockEffect).stop(EffectSoundFile.STORM, 0);
            exceptionAcidShowerEvent.stopEffect();
            verify(testLogger, times(3)).error(anyString(), any(InvalidSoundFileException.class));
        } catch (InvalidSoundFileException e) {
            fail();
        }
    }

    public class testAcidShowerEvent extends AcidShowerEvent {
        public Logger logger = LoggerFactory.getLogger(AcidShowerEvent.class);
        public testAcidShowerEvent(int numHoursUntil, int duration, int priority, float severity) {
            super(numHoursUntil, duration, priority, severity);
        }
    }
}