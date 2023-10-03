package com.csse3200.game.missions.rewards;

import com.csse3200.game.areas.weather.WeatherEvent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * TriggerWeatherReward is a Reward that when collected will add the provided WeatherEvents to the ClimateController.
 */
public class TriggerWeatherReward extends Reward {

    /**
     * WeatherEvents to be added to the ClimateController
     */
    private final List<WeatherEvent> weatherEvents;

    /**
     * A TriggerWeatherReward with a list of WeatherEvent objects.
     * @param weatherEvents - WeatherEvents to be added to the ClimateController.
     */
    public TriggerWeatherReward(List<WeatherEvent> weatherEvents) {
        super();
        this.weatherEvents = weatherEvents;
    }

    /**
     * Adds each event from the list of weatherEvents to the ClimateController and sets the TriggerWeatherRewards as
     * collected.
     */
    @Override
    public void collect() {
        setCollected();
        for (WeatherEvent weatherEvent : weatherEvents) {
            ServiceLocator.getGameArea().getClimateController().addWeatherEvent(weatherEvent);
        }
    }

}
