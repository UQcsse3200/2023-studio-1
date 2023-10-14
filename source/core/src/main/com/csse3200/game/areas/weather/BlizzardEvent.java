package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

public class BlizzardEvent extends WeatherEvent {

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
    }

    @Override
    public void startEffect() {
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());
        // TODO - update effect
        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        // Adjust global lighting
    }

    @Override
    public void stopEffect() {
        climateControllerEvents.trigger("stopWaterLevelEffect");
        // TODO - update effect
        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
        // Adjust global lighting
    }

    private float getDryRate() {
        // Lowest severity: Dry at regular rate
        // Highest severity: Dry at 2x regular rate
        return 0.0005f + 0.0005f * severity / 1.5f;
    }

}
