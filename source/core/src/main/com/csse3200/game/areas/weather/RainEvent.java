package com.csse3200.game.areas.weather;

public class RainEvent extends WeatherEvent {

    public RainEvent(int numHoursUntil, int duration, int priority) {
        super(numHoursUntil, duration, priority);
    }

    //TODO
    @Override
    public void getEffect() {

    }

    @Override
    protected float generateRandomHumidityModifier() {
        return super.generateRandomHumidityModifier() * getSeverity();
    }

    @Override
    protected float generateRandomTemperatureModifier() {
        return super.generateRandomTemperatureModifier() * getSeverity();
    }
}
