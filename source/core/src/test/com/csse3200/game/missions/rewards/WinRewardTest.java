package com.csse3200.game.missions.rewards;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class WinRewardTest {

    private WinReward reward;

    EventHandler eventHandler;

    @BeforeEach
    public void init() {
        reward = new WinReward();

        MissionManager missionManager = mock(MissionManager.class);
        ServiceLocator.registerMissionManager(missionManager);
        eventHandler = mock(EventHandler.class);
        when(ServiceLocator.getMissionManager().getEvents()).thenReturn(eventHandler);
    }

   @AfterEach
    public void clearServiceLocator() {
        ServiceLocator.clear();
   }

   @Test
    public void collectWinReward() {
        assertFalse(reward.isCollected());

        reward.collect();

        assertTrue(reward.isCollected());

        verify(eventHandler).trigger("winScreen");
   }

}
