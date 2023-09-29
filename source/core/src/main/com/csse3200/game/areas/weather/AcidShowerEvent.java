package com.csse3200.game.areas.weather;

import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.ServiceLocator;

/**
 * An acid rain event to occur in game affecting both the temperature and the climate and other surrounding
 * entities.
 */
public class AcidShowerEvent extends WeatherEvent {
    /**
     * Minimum modifier that is used when calculating the effect of the weather event on humidity
     */
    protected static final float MIN_HUMIDITY_MODIFIER = 0.05f;
    /**
     * Maximum modifier that is used when calculating the effect of the weather event on humidity
     */
    protected static final float MAX_HUMIDITY_MODIFIER = 0.2f;
    /**
     * Minimum modifier that is used when calculating the effect of the weather event on temperature
     */
    protected static final float MIN_TEMPERATURE_MODIFIER = -5f;
    /**
     * Maximum modifier that is used when calculating the effect of the weather event on temperature
     */
    protected static final float MAX_TEMPERATURE_MODIFIER = -10f;

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
        humidityModifier = MIN_HUMIDITY_MODIFIER + (MAX_HUMIDITY_MODIFIER - MIN_HUMIDITY_MODIFIER) * severity;
        temperatureModifier = MIN_TEMPERATURE_MODIFIER +
                (MAX_TEMPERATURE_MODIFIER - MIN_TEMPERATURE_MODIFIER) * severity;
    }

    @Override
    public void startEffect() {
        ServiceLocator.getParticleService().startEffect(ParticleService.ParticleEffectType.ACID_RAIN);
    }

    @Override
    public void stopEffect() {
        ServiceLocator.getParticleService().stopEffect(ParticleService.ParticleEffectType.ACID_RAIN.name());

    }
}
