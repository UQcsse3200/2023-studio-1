package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class RainStormEvent extends WeatherEvent {

    private static final Logger logger = LoggerFactory.getLogger(RainStormEvent.class);

    private ScheduledEvent nextLightningStrike;
    private long stormSoundId;

    /**
     * Constructs an {@link WeatherEvent} with a given duration, priority and countdown
     *
     * @param numHoursUntil number of in-game hours until the weather event can occur
     * @param duration      number of in-game hours that the event can occur for
     * @param priority      priority of the weather event
     * @param severity      the severity of this rainstorm event
     */
    public RainStormEvent(int numHoursUntil, int duration, int priority, float severity) throws IllegalArgumentException {
        super(numHoursUntil, duration, priority, severity);
        climateControllerEvents.addListener("lightningStrike", this::triggerStrike);
    }

    @Override
    public void startEffect() {
        logger.info("Starting RainStorm effects");

        // Trigger in-game effects
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());
        climateControllerEvents.trigger("douseFlames");
        scheduleNextLightningStrike();

        // Add particle effects
        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.RAIN);

        // Add lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier((1.0f - severity / 1.5f) * 0.3f + 0.6f);

        // Start sound effects
        try {
            stormSoundId = ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.STORM, true);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to play storm sound effect", exception);
        }
    }

    @Override
    public void stopEffect() {
        logger.info("Stopping RainStorm effects");

        // Cancel in-game effects
        climateControllerEvents.trigger("stopWaterLevelEffect");
        climateControllerEvents.trigger("stopPlacedLightEffects");
        climateControllerEvents.cancelEvent(nextLightningStrike);
        nextLightningStrike = null;

        // Remove particle effects
        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.RAIN);

        // Remove lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier(1.0f);

        // Stop sound effects
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().stop(EffectSoundFile.STORM, stormSoundId);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to stop storm sound effect", exception);
        }
    }

    private void scheduleNextLightningStrike() {
        nextLightningStrike = climateControllerEvents.scheduleEvent(getNextTimeToLightningStrike(), "lightningStrike");
    }

    private void triggerStrike() {
        if (nextLightningStrike != null) {
            logger.info("Triggering lightning strike");
            // Play SFX
            try {
                ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.LIGHTNING_STRIKE);
            } catch (InvalidSoundFileException exception) {
                logger.error("Failed to play lightning strike sound effect", exception);
            }
            // Trigger animal panic
            climateControllerEvents.trigger("startPanicEffect");
            // Apply the desired lighting effect
            climateControllerEvents.trigger("lightingEffect", getNextLightningStrikeDuration(),
                    (Function<Float, Color>) this::getLightningColourOffset);
            // Recursively schedule next strike
            scheduleNextLightningStrike();
        }
    }

    private float getNextTimeToLightningStrike() {
        float maxTime = 6.0f + 8.0f * (1.5f - severity) / 1.5f;
        float minTime = 2.0f + 3.0f * (1.5f - severity) / 1.5f;
        return MathUtils.random(minTime, maxTime);
    }

    private float getNextLightningStrikeDuration() {
        float maxTime = 0.8f + 0.4f * severity / 1.5f;
        float minTime = 0.2f + 0.2f * severity / 1.5f;
        return MathUtils.random(minTime, maxTime);
    }

    private Color getLightningColourOffset(float t) {
        float brightness = 0.8f * MathUtils.sin((float) (Math.PI * t)) + 0.2f;
        brightness *= brightness * 0.8f;
        return new Color(brightness, brightness, brightness, 0.0f);
    }

    private float getDryRate() {
        // Lowest severity: Crop tiles do not dry or get watered
        // Highest severity: Watered at 4x regular dry rate
        return -0.002f * severity / 1.5f;
    }

}
