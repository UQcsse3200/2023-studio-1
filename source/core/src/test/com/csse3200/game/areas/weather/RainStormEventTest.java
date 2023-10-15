package com.csse3200.game.areas.weather;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.services.LightService;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.services.sound.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class RainStormEventTest {

    private RainStormEvent rainStormEvent1, rainStormEvent2, rainStormEvent3, rainStormEvent4, rainStormEvent5;
    private static final Logger logger = LoggerFactory.getLogger(RainStormEvent.class);

    @BeforeEach
    public void setUp() {
        GameArea gameArea = mock(GameArea.class);
        ClimateController climateController = new ClimateController();
        when(gameArea.getClimateController()).thenReturn(climateController);
        ServiceLocator.registerGameArea(gameArea);

        SoundService soundService = mock(SoundService.class);
        when(soundService.getEffectsMusicService()).thenReturn(mock(EffectsMusicService.class));
        java.util.List<SoundFile> effects = new ArrayList<>();
        effects.add(EffectSoundFile.STORM);
        ServiceLocator.registerSoundService(soundService);
        try {
            soundService.getEffectsMusicService().loadSounds(effects);
        } catch (InvalidSoundFileException e) {
            logger.info("Sound files not loaded");
        }

        TimeService timeService = mock(TimeService.class);
        ServiceLocator.registerTimeService(timeService);

        rainStormEvent1 = new RainStormEvent(0, 9, 1, 1.2f);
        rainStormEvent2 = new RainStormEvent(1, 2, 2, 1.4f);
        rainStormEvent3 = new RainStormEvent(2, 4, 5, 1.0f);
        rainStormEvent4 = new RainStormEvent(3, 3, 3, 0.6f);
        rainStormEvent5 = new RainStormEvent(5, 5, 1, 0.7f);
        ParticleService mockParticleService = mock(ParticleService.class);
        ServiceLocator.registerParticleService(mockParticleService);
        LightService lightService = mock(LightService.class);
        ServiceLocator.registerLightService(lightService);
    }

    @AfterEach
    public void cleanUp() {
        ServiceLocator.clear();
    }

    //     These tests will require dealing with the lighting system
    @Test
    void testStartEffect() throws InvalidSoundFileException {
        rainStormEvent1.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        rainStormEvent2.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        rainStormEvent3.startEffect();
        ServiceLocator.getGameArea().getClimateController().getEvents().trigger("startPlantAoeEffect", -2f);
        //TODO - update
        verify(ServiceLocator.getParticleService(), times(3)).startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.66f);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.62f);
        verify(ServiceLocator.getLightService(), times(1)).setBrightnessMultiplier(0.70000005f);
    }

    @Test
    void testStopEffect() {
        rainStormEvent1.stopEffect();
        rainStormEvent2.stopEffect();
        rainStormEvent3.stopEffect();
        //TODO - update
        verify(ServiceLocator.getParticleService(), times(3)).stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        verify(ServiceLocator.getLightService(), times(3)).setBrightnessMultiplier(1.0f);
    }
}
