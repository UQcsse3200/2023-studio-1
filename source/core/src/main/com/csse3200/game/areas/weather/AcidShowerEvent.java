package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

/**
 * An acid rain event to occur in game affecting both the temperature and the climate and other surrounding
 * entities.
 */
public class AcidShowerEvent extends WeatherEvent {

    /**
     * Constructs a AcidShowerEvent with a given countdown, duration, priority and severity.
     *
     * @param numHoursUntil number of in-game hours until the weather event can occur
     * @param duration      number of in-game hours that the event can occur for
     * @param priority      priority of the weather event
     * @param severity      intensity of the weather event, impacting its effects
     */
    public AcidShowerEvent(int numHoursUntil, int duration, int priority, float severity) {
        super(numHoursUntil, duration, priority, severity);
    }

    @Override
    public void startEffect() {
        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }

    @Override
    public void stopEffect() {
        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }

}
