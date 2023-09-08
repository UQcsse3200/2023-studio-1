package com.csse3200.game.areas.weather;

/**
 * Represents rain within the game's weather system.
 */
public class AcidShowerEvent extends WeatherEvent {
    private static final float MIN_HUMIDITY_MODIFIER = 0.7f;
    private static final float MAX_HUMIDITY_MODIFIER = 1.3f;
    private static final float MIN_TEMPERATURE_MODIFIER = 0.7f;
    private static final float MAX_TEMPERATURE_MODIFIER = 0.9f;

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
}
