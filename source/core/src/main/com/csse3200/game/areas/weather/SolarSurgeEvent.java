package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * Represents a heat wave event within the planet's weather system.
 * SolarSurgeEvent is caused by the production of a massive amount of solar energy.
 */
public class SolarSurgeEvent extends WeatherEvent {

    private static final Logger logger = LoggerFactory.getLogger(SolarSurgeEvent.class);

    private ScheduledEvent nextSurge;

    private float rOffset;
    private float gOffset;
    private float bOffset;

    private long solarSurgeSoundId;

    /**
     * Constructs a SolarSurgeEvent with a given countdown, duration, priority and severity.
     *
     * @param numHoursUntil number of in-game hours until the weather event can occur
     * @param duration      number of in-game hours that the event can occur for
     * @param priority      priority of the weather event
     * @param severity      intensity of the weather event, impacting its effects
     */
    public SolarSurgeEvent(int numHoursUntil, int duration, int priority, float severity) {
        super(numHoursUntil, duration, priority, severity);
        climateControllerEvents.addListener("surge", this::triggerSurge);
    }

    @Override
    public void startEffect() {
        logger.info("Starting SolarSurge effects");

        // Trigger in-game effects
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());
        climateControllerEvents.trigger("startPlantAoeEffect", getPlantEffectivenessMultiplier());
        scheduleNextSurge();

        // Apply the desired lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier(0.0f);
        rOffset = MathUtils.random();
        gOffset = MathUtils.random();
        bOffset = MathUtils.random();
        climateControllerEvents.trigger("lightingEffect", 34.0f,
                (Function<Float, Color>) this::getAuroraColourOffset);

        // Start sound effects
        try {
            solarSurgeSoundId = ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.SOLAR_SURGE, true);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to play solar surge sound effect", exception);
        }
    }

    @Override
    public void stopEffect() {
        logger.info("Stopping SolarSurge effects");

        // Cancel in-game effects
        climateControllerEvents.trigger("stopWaterLevelEffect");
        climateControllerEvents.trigger("stopPlantAoeEffect");
        climateControllerEvents.cancelEvent(nextSurge);
        nextSurge = null;

        // Remove lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier(1.0f);

        // Stop sound effects
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().stop(EffectSoundFile.SOLAR_SURGE, solarSurgeSoundId);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to stop solar surge sound effect", exception);
        }
    }

    private void scheduleNextSurge() {
        nextSurge = climateControllerEvents.scheduleEvent(4.0f - 2.0f * severity / 1.5f, "surge");
    }

    private void triggerSurge() {
        if (nextSurge != null) {
            logger.info("Triggering surge");

            // Play SFX
            try {
                ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.SURGE);
            } catch (InvalidSoundFileException exception) {
                logger.error("Failed to play lightning strike sound effect", exception);
            }
            // Trigger animal panic and damage plants (if sufficiently severe)
            if (severity > 1.2f) {
                climateControllerEvents.trigger("startPanicEffect");
                climateControllerEvents.trigger("damagePlants");
            }
            // Recursively schedule next aurora
            scheduleNextSurge();
        }
    }

    private Color getAuroraColourOffset(float t) {
        float r = MathUtils.sin((float) (Math.PI * 30.0f * (t + rOffset)));
        float g = MathUtils.sin((float) (Math.PI * 22.0f * (t + gOffset)));
        float b = MathUtils.sin((float) (Math.PI * 14.0f * (t + bOffset)));

        float multiplier = 0.05f + 0.15f * severity / 1.5f;
        r = multiplier * r * r + (1 - multiplier);
        g = multiplier * g * g + (1 - multiplier);
        b = multiplier * b * b + (1 - multiplier);

        return new Color(r, g, b, 0.0f);
    }

    private float getDryRate() {
        // Lowest severity: Dry at 2x regular rate
        // Highest severity: Dry at 20x regular rate
        return 0.001f + 0.009f * severity / 1.5f;
    }

    private int getPlantEffectivenessMultiplier() {
        if (severity > 0.75f) {
            return 2;
        } else {
            return 1;
        }
    }

}
