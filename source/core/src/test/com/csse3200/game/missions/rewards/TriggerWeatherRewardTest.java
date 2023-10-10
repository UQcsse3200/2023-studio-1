package com.csse3200.game.missions.rewards;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.areas.weather.WeatherEvent;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TriggerWeatherRewardTest {

    private TriggerWeatherReward reward1, reward2;

    private List<WeatherEvent> weatherEventList, emptyWeatherEventList;

    private ClimateController climateController;

    @BeforeEach
    void init() {
        emptyWeatherEventList = new ArrayList<>();
        weatherEventList = List.of(
                mock(WeatherEvent.class),
                mock(WeatherEvent.class),
                mock(WeatherEvent.class)
        );

        reward1 = new TriggerWeatherReward(emptyWeatherEventList);
        reward2 = new TriggerWeatherReward(weatherEventList);

        GameTime gameTime = mock(GameTime.class);
        TimeService timeService = mock(TimeService.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerTimeService(timeService);
        GameArea gameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(gameArea);
        EventHandler handler = new EventHandler();
        when(timeService.getEvents()).thenReturn(handler);
        climateController = mock(ClimateController.class);
        when(ServiceLocator.getGameArea().getClimateController()).thenReturn(climateController);
    }

    @AfterEach
    void clearServiceLocator() {
        ServiceLocator.clear();
    }

    @Test
    void collectEmptyWeatherReward() {
        reward1.collect();
        assertTrue(reward1.isCollected());

        verifyNoInteractions(climateController);
    }

    @Test
    void collectWeatherReward() {
        reward2.collect();
        assertTrue((reward2.isCollected()));

        for (WeatherEvent weatherEvent : weatherEventList) {
            verify(climateController).addWeatherEvent(weatherEvent);
        }
    }
}
