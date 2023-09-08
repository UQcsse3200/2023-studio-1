package com.csse3200.game.areas.weather;

public class RainEvent extends WeatherEvent {
    private static final float MIN_HUMIDITY_MODIFIER = 0.7f;
    private static final float MAX_HUMIDITY_MODIFIER = 1.3f;
    private static final float MIN_TEMPERATURE_MODIFIER = 0.7f;
    private static final float MAX_TEMPERATURE_MODIFIER = 0.9f;


    public RainEvent(int numHoursUntil, int duration, int priority, float severity) {
        super(numHoursUntil, duration, priority, severity);
    }
}
