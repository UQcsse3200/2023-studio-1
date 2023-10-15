package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlizzardEvent extends WeatherEvent {

    private static final Logger logger = LoggerFactory.getLogger(BlizzardEvent.class);

    private boolean hasSpawnedFireflies;
    private long blizzardSoundId;

    /**
     * Constructs an {@link WeatherEvent} with a given duration, priority and countdown
     *
     * @param numHoursUntil number of in-game hours until the weather event can occur
     * @param duration      number of in-game hours that the event can occur for
     * @param priority      priority of the weather event
     * @param severity      the severity of this rainstorm event
     */
    public BlizzardEvent(int numHoursUntil, int duration, int priority, float severity) throws IllegalArgumentException {
        super(numHoursUntil, duration, priority, severity);
        this.hasSpawnedFireflies = false;
    }

    @Override
    public void startEffect() {
        logger.info("Starting Blizzard effects");

        // Trigger in-game effects
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());
        climateControllerEvents.trigger("startPlantAoeEffect", getPlantEffectivenessOffset());
        climateControllerEvents.trigger("startPlayerMovementSpeedEffect", getPlayerMovementSpeedMultiplier());
        if (!hasSpawnedFireflies && !ServiceLocator.getTimeService().isNight()) {
            climateControllerEvents.trigger("spawnFireflies");
            hasSpawnedFireflies = true;
        }

        // TODO - update effect
        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

        // Adjust global lighting
        ServiceLocator.getLightService().setBrightnessMultiplier((1.0f - severity / 1.5f) * 0.2f + 0.5f);

        // Start sound effects
        try {
            blizzardSoundId = ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.BLIZZARD, true);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to play blizzard sound effect", exception);
        }
    }

    @Override
    public void stopEffect() {
        logger.info("Stopping Blizzard effects");

        // Cancel in-game effects
        climateControllerEvents.trigger("stopWaterLevelEffect");
        climateControllerEvents.trigger("stopPlantAoeEffect");
        climateControllerEvents.trigger("stopPlayerMovementSpeedEffect");

        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.BLIZZARD);

        // Adjust global lighting
        ServiceLocator.getLightService().setBrightnessMultiplier(1.0f);

        // Stop sound effects
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().stop(EffectSoundFile.BLIZZARD, blizzardSoundId);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to stop blizzard sound effect", exception);
        }
    }

    private float getDryRate() {
        // Lowest severity: Dry at regular rate
        // Highest severity: Dry at 2x regular rate
        return 0.0005f + 0.0005f * severity / 1.5f;
    }

    private int getPlantEffectivenessOffset() {
        if (severity > 0.75f) {
            return -2;
        } else {
            return -1;
        }
    }

    private float getPlayerMovementSpeedMultiplier() {
        return 0.45f + 0.5f * (1.5f - severity) / 1.5f;
    }

}
