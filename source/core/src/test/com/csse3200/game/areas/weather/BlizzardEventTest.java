package com.csse3200.game.areas.weather;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.EffectsMusicService;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import com.csse3200.game.services.sound.SoundService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class BlizzardEventTest {

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

        blizzardEvent1 = new BlizzardEvent(0, 9, 1, 1.2f);
        blizzardEvent2 = new BlizzardEvent(1, 2, 2, 1.4f);
        blizzardEvent3 = new BlizzardEvent(2, 4, 5, 1.0f);
        blizzardEvent4 = new BlizzardEvent(3, 3, 3, 0.6f);
        blizzardEvent5 = new BlizzardEvent(5, 5, 1, 0.7f);

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
    void testStartEffectParticleAndLightingSystem() throws InvalidSoundFileException {
        blizzardEvent1.startEffect();
        blizzardEvent2.startEffect();
        blizzardEvent3.startEffect();
        verify(ServiceLocator.getParticleService(), times(3)).startEffect(ParticleService.ParticleEffectType.BLIZZARD);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.54f);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.5133333f);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.56666666f);
    }

    @Test
    void testStartEffectTriggersEvents() {
        blizzardEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        blizzardEvent2.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        blizzardEvent4.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -1f);
    }

    @Test
    void testStartEffectPlaysSound() throws InvalidSoundFileException {
        blizzardEvent4.startEffect();
        blizzardEvent5.startEffect();
        verify(ServiceLocator.getSoundService().getEffectsMusicService(), times(2)).play(EffectSoundFile.BLIZZARD, true);
    }

    @Test
    void testStopEffect() {
        blizzardEvent1.stopEffect();
        blizzardEvent2.stopEffect();
        blizzardEvent3.stopEffect();
        verify(ServiceLocator.getParticleService(), times(3)).stopEffect(ParticleService.ParticleEffectType.BLIZZARD);
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(1.0f);
    }
}