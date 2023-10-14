package com.csse3200.game.areas.weather;

/**
 * Represents a heat wave event within the planet's weather system.
 * SolarSurgeEvent is caused by the production of a massive amount of solar energy.
 */
public class SolarSurgeEvent extends WeatherEvent {

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
    }

    @Override
    public void startEffect() {
        climateControllerEvents.trigger("startWaterLevelEffect", getDryRate());
        climateControllerEvents.trigger("startPlantAoeEffect", getPlantEffectivenessMultiplier());
    }

    @Override
    public void stopEffect() {
        climateControllerEvents.trigger("stopWaterLevelEffect");
        climateControllerEvents.trigger("stopPlantAoeEffect");
    }

    private float getDryRate() {
        // Lowest severity: Dry at 2x regular rate
        // Highest severity: Dry at 20x regular rate
        return 0.001f + 0.009f * severity / 1.5f;
    }

    private int getPlantEffectivenessMultiplier() {
        if (severity > 0.75f) {
            return 2;
        } else {
            return 1;
        }
    }

}
