package com.csse3200.game.areas.weather;

public class RainStormEvent extends WeatherEvent {

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
    }

    @Override
    public void startEffect() {
        // Trigger "beginRainstorm" event
        // Start the RAIN_STORM particle effect
        // Adjust global lighting
        // Add occasional light increase to mimic lighting if severe enough
    }

    @Override
    public void stopEffect() {
        // Trigger "endRainstorm" event
        // Stop the RAIN_STORM particle effect
        // Adjust global lighting
        // Add occasional light increase to mimic lighting if severe enough
    }

}
