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

        // Apply the desired lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier(0.0f);
        rOffset = MathUtils.random();
        gOffset = MathUtils.random();
        bOffset = MathUtils.random();
        climateControllerEvents.trigger("lightingEffect", 34.0f,
                (Function<Float, Color>) this::getAuroraColourOffset);
        scheduleNextSurge();

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

        // Remove lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier(1.0f);
        climateControllerEvents.cancelEvent(nextSurge);

        // Stop sound effects
        try {
            ServiceLocator.getSoundService().getEffectsMusicService().stop(EffectSoundFile.SOLAR_SURGE, solarSurgeSoundId);
        } catch (InvalidSoundFileException exception) {
            logger.error("Failed to stop solar surge sound effect", exception);
        }
    }

    private void scheduleNextSurge() {
        float timeToEndOfEvent = ((59 - ServiceLocator.getTimeService().getMinute()) / 60.0f + duration - 1) * 30.0f;
        float timeToSurge = getNextTimeToSurge();

        if (timeToEndOfEvent < timeToSurge) {
            return;
        }

        nextSurge = climateControllerEvents.scheduleEvent(timeToSurge, "surge");
    }

    private void triggerSurge() {
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

    private float getNextTimeToSurge() {
        float maxTime = 2.0f + 0.8f * (1.5f - severity) / 1.5f;
        float minTime = 0.8f + 0.2f * (1.5f - severity) / 1.5f;
        return MathUtils.random(minTime, maxTime);
    }

    private Color getAuroraColourOffset(float t) {
        float r = MathUtils.sin((float) (Math.PI * (t + rOffset)));
        float g = MathUtils.sin((float) (Math.PI * 0.8f * (t + gOffset)));
        float b = MathUtils.sin((float) (Math.PI * 0.6f * (t + bOffset)));

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
