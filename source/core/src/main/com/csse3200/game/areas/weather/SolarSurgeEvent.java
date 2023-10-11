package com.csse3200.game.areas.weather;

/**
 * Represents a heat wave event within the planet's weather system.
 * SolarSurgeEvent is caused by the production of a massive amount of solar energy.
 */
public class SolarSurgeEvent extends WeatherEvent {
    /**
     * Minimum modifier that is used when calculating the effect of the weather event on humidity
     */
    protected static final float MIN_HUMIDITY_MODIFIER = -0.2f;
    /**
     * Maximum modifier that is used when calculating the effect of the weather event on humidity
     */
    protected static final float MAX_HUMIDITY_MODIFIER = -0.4f;
    /**
     * Minimum modifier that is used when calculating the effect of the weather event on temperature
     */
    protected static final float MIN_TEMPERATURE_MODIFIER = 15f;
    /**
     * Maximum modifier that is used when calculating the effect of the weather event on temperature
     */
    protected static final float MAX_TEMPERATURE_MODIFIER = 30f;

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
        humidityModifier = MIN_HUMIDITY_MODIFIER + (MAX_HUMIDITY_MODIFIER - MIN_HUMIDITY_MODIFIER) * severity;
        temperatureModifier = MIN_TEMPERATURE_MODIFIER +
                (MAX_TEMPERATURE_MODIFIER - MIN_TEMPERATURE_MODIFIER) * severity;
    }

    /**
     *
     */
    @Override
    public void startEffect() {
        //  Handled elsewhere
    }

    /**
     *
     */
    @Override
    public void stopEffect() {
        // Handled elsewhere
    }

    @Override
    public String toString() {
        return "solarSurge";
    }
}
