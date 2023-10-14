package com.csse3200.game.areas.weather;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.events.ScheduledEvent;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

import java.util.function.Function;

public class RainStormEvent extends WeatherEvent {

    private ScheduledEvent nextLightningStrike;

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
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());

        // TODO - update effect
        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.ACID_RAIN);

        // Add lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier((1.0f - severity / 1.5f) * 0.3f + 0.6f);
        scheduleNextLightningStrike();
    }

    @Override
    public void stopEffect() {
        climateControllerEvents.trigger("stopWaterLevelEffect");

        // TODO - update effect
        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);

        // Remove lighting effects
        ServiceLocator.getLightService().setBrightnessMultiplier(1.0f);
        climateControllerEvents.cancelEvent(nextLightningStrike);
    }

    private void scheduleNextLightningStrike() {
        float timeToEndOfEvent = ((59 - ServiceLocator.getTimeService().getMinute()) / 60.0f + duration - 1) * 30.0f;
        float timeToStrike = getNextTimeToLightningStrike();

        if (timeToEndOfEvent < timeToStrike) {
            return;
        }

        nextLightningStrike = climateControllerEvents.scheduleEvent(timeToStrike, "lightningStrike");
    }

    private void triggerStrike() {
        // Trigger animal panic
        climateControllerEvents.trigger("startPanicEffect");
        // Apply the desired lighting effect
        climateControllerEvents.trigger("lightingEffect", getNextLightningStrikeDuration(),
                (Function<Float, Color>) this::getLightningColourOffset);
        // Recursively schedule next strike
        scheduleNextLightningStrike();
    }

    private float getNextTimeToLightningStrike() {
        float maxTime = 6.0f + 8.0f * (1.5f - severity) / 1.5f;
        float minTime = 2.0f + 3.0f * (1.5f - severity) / 1.5f;
        return MathUtils.random(minTime, maxTime);
    }

    private float getNextLightningStrikeDuration() {
        float maxTime = 1.8f + 2.0f * severity / 1.5f;
        float minTime = 0.6f + 0.6f * severity / 1.5f;
        return MathUtils.random(minTime, maxTime);
    }

    private Color getLightningColourOffset(float t) {
        float brightness = 0.8f * MathUtils.sin((float) (Math.PI * t * (severity / 1.5f + 1.0f))) + 0.2f;
        brightness *= brightness * 0.8f;
        return new Color(brightness, brightness, brightness, 0.0f);
    }

    private float getDryRate() {
        // Lowest severity: Crop tiles do not dry or get watered
        // Highest severity: Watered at 4x regular dry rate
        return -0.002f * severity / 1.5f;
    }

}
