package com.csse3200.game.areas.weather;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.tasks.PanicTask;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.services.*;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.EffectsMusicService;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import com.csse3200.game.services.sound.SoundService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        acidShowerEvent1 = new AcidShowerEvent(0, 9, 1, 1.2f);
        acidShowerEvent2 = new AcidShowerEvent(1, 2, 2, 1.4f);
        acidShowerEvent3 = new AcidShowerEvent(2, 4, 5, 1.0f);
        acidShowerEvent4 = new AcidShowerEvent(3, 3, 3, 1.3f);
        acidShowerEvent5 = new AcidShowerEvent(5, 5, 1, 1.1f);
        ParticleService mockParticleService = mock(ParticleService.class);
        ServiceLocator.registerParticleService(mockParticleService);
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

    @Test
    void testTriggerAcidBurn() throws InvalidSoundFileException {
        AcidShowerEvent acidShowerEvent6 = new AcidShowerEvent(0,9,1,1.2f);
        acidShowerEvent6.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("acidBurn");
        verify(ServiceLocator.getSoundService().getEffectsMusicService()).play(EffectSoundFile.ACID_BURN);
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPanicEffect");
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("damagePlants");
    }
}