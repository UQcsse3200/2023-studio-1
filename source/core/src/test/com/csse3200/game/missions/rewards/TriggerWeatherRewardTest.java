package com.csse3200.game.missions.rewards;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.areas.weather.WeatherEvent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TriggerWeatherRewardTest {

    private TriggerWeatherReward reward1, reward2;

    private List<WeatherEvent> weatherEventList, emptyWeatherEventList;

    private ClimateController climateController;

    @BeforeEach
    public void init() {
        emptyWeatherEventList = new ArrayList<>();
        weatherEventList = List.of(
                mock(WeatherEvent.class),
                mock(WeatherEvent.class),
                mock(WeatherEvent.class)
        );

        reward1 = new TriggerWeatherReward(emptyWeatherEventList);
        reward2 = new TriggerWeatherReward(weatherEventList);

        GameArea gameArea = mock(GameArea.class);
        climateController = mock(ClimateController.class);

        //when(ServiceLocator.getGameArea()).thenReturn(gameArea);
        //when(gameArea.getClimateController()).thenReturn(climateController);
    }

    @Test
    public void collectEmptyWeatherReward() {
        //reward1.collect();
        //assertTrue(reward1.isCollected());

        //verifyNoInteractions(climateController);
    }

    @Test
    public void collectWeatherReward() {
        //reward2.collect();
        //assertTrue((reward2.isCollected()));

        /*for (WeatherEvent weatherEvent : weatherEventList) {
            verify(climateController).addWeatherEvent(weatherEvent);
        }*/
    }
}
