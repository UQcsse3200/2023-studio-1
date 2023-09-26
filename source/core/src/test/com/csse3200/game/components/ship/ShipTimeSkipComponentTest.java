package com.csse3200.game.components.ship;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ShipTimeSkipComponentTest {
    private boolean isMorningHour;

    @BeforeEach
    void setupTest() {
        isMorningHour = false;
    }

    void setMorningHour() {
        isMorningHour = true;
    }

    @ParameterizedTest(name = "Updates the time from {0}d {1}h {2}m to {3}d 6h 0m when unlocked")
    @MethodSource({"updatesTimeWhenUnlockedParams"})
    void updatesTimeWhenUnlocked(int initialDay, int initialHour, int initialMinute, int expectedDay) {
        GameTime mockTimeSource = spy(GameTime.class);
        ServiceLocator.registerTimeSource(mockTimeSource);

        TimeService timeService = new TimeService();
        ServiceLocator.registerTimeService(timeService);

        timeService.setDay(initialDay);
        timeService.setHour(initialHour);
        timeService.setMinute(initialMinute);

        timeService.getEvents().addListener("morningTime", this::setMorningHour);

        ShipTimeSkipComponent component = new ShipTimeSkipComponent();
        Entity testEntity = new Entity().addComponent(component);
        testEntity.create();

        // not unlocked yet, shouldn't do anything
        testEntity.getEvents().trigger("interact");
        verify(mockTimeSource, times(0)).setTimeScale(200f);

        // unlock the feature
        testEntity.getEvents().trigger("progressUpdated", 4, new HashSet<>(List.of(ShipProgressComponent.Feature.BED)));

        // should trigger a time change now
        testEntity.getEvents().trigger("interact");
        verify(mockTimeSource, times(1)).setTimeScale(200f);

        while (!isMorningHour) {
            timeService.update();
        }

        assert timeService.getDay() == expectedDay;
        verify(mockTimeSource, times(1)).setTimeScale(1f);
    }

    private static Stream<Arguments> updatesTimeWhenUnlockedParams() {
        return Stream.of(
                // (initialDay, initialHour, initialMinute, expectedDay)
                arguments(0, 5, 0, 0),
                arguments(0, 5, 59, 0),
                arguments(0, 6, 0, 1),
                arguments(0, 6, 1, 1)
        );
    }

    @Test
    void doesNotTriggerTimeScaleWhenTimeSkipNotInProgress() {
        GameTime mockTimeSource = spy(GameTime.class);
        ServiceLocator.registerTimeSource(mockTimeSource);

        TimeService timeService = new TimeService();
        ServiceLocator.registerTimeService(timeService);

        timeService.getEvents().addListener("morningTime", this::setMorningHour);

        ShipTimeSkipComponent component = new ShipTimeSkipComponent();
        Entity testEntity = new Entity().addComponent(component);
        testEntity.create();

        while (!isMorningHour) {
            timeService.update();
        }

        verify(mockTimeSource, times(0)).setTimeScale(1f);
    }
}