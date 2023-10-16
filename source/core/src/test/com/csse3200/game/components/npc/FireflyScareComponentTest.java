package com.csse3200.game.components.npc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireflyScareComponentTest {

    @Test
    void testUpdate() {
        TimeService mockTimeService = mock(TimeService.class);
        doReturn(true).when(mockTimeService).isDay();

        ServiceLocator.registerTimeService(mockTimeService);
        FireflyScareComponent scare = spy(new FireflyScareComponent());
        try {
            scare.update();
        } catch (NullPointerException e) {
            // Tried to remove entity and failed which is good so pass test
            return;
        }
        fail();
    }

    @Test
    void testUpdateNight() {
        TimeService mockTimeService = mock(TimeService.class);
        doReturn(false).when(mockTimeService).isDay();

        ServiceLocator.registerTimeService(mockTimeService);
        FireflyScareComponent scare = spy(new FireflyScareComponent());
        try {
            scare.update();
        } catch (NullPointerException e) {
            // Tried to remove entity and failed which is good so pass test
            fail();
        }
    }
}
