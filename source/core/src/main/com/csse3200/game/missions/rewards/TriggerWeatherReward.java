package com.csse3200.game.missions.rewards;

import com.csse3200.game.areas.weather.WeatherEvent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class TriggerWeatherReward extends Reward {

    private final List<WeatherEvent> weatherEvents;

    public TriggerWeatherReward(List<WeatherEvent> weatherEvents) {
        super();
        this.weatherEvents = weatherEvents;
    }

    @Override
    public void collect() {
        setCollected();
        for (WeatherEvent weatherEvent : weatherEvents) {
            ServiceLocator.getGameArea().getClimateController().addWeatherEvent(weatherEvent);
        }
    }

}
