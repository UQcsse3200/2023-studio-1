package com.csse3200.game.areas.weather;

public class SolarSurgeEvent extends WeatherEvent {
    public SolarSurgeEvent(int numHoursUntil, int duration, int priority, float severity) {
        super(numHoursUntil, duration, priority, severity);
    }
}
