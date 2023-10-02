package com.csse3200.game.missions.quests;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.PlanetOxygenService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EventListener;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OxygenLevelQuestTest {
    
    private OxygenLevelQuest OLQuest1, OLQuest2, OLQuest3, OLQuest4, OLQuest5, OLQuest6, OLQuest7, OLQuest8;
    private Reward r1, r2, r3, r4, r5, r6, r7, r8;
    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
        ServiceLocator.registerPlanetOxygenService(new PlanetOxygenService());

        r1 = mock(Reward.class);
        r2 = mock(Reward.class);
        r3 = mock(Reward.class);
        r4 = mock(Reward.class);
        r5 = mock(Reward.class);
        r6 = mock(Reward.class);
        r7 = mock(Reward.class);
        r8 = mock(Reward.class);

        OLQuest1 = new OxygenLevelQuest("Oxygen Level Quest 1", r1, ServiceLocator.getPlanetOxygenService(), "Quest 1", 0, 0);
        OLQuest2 = new OxygenLevelQuest("Oxygen Level Quest 2", r2, ServiceLocator.getPlanetOxygenService(), "Quest 2", 0, 50);
        OLQuest3 = new OxygenLevelQuest("Oxygen Level Quest 3", r3, ServiceLocator.getPlanetOxygenService(), "Quest 3", 24, 0);
        OLQuest4 = new OxygenLevelQuest("Oxygen Level Quest 4", r4, ServiceLocator.getPlanetOxygenService(), "Quest 4", 24, 50);

        OLQuest5 = new OxygenLevelQuest("Oxygen Level Quest 5", r5, 10, ServiceLocator.getPlanetOxygenService(), "Quest 5", 0, 0);
        OLQuest6 = new OxygenLevelQuest("Oxygen Level Quest 6", r6, 10, ServiceLocator.getPlanetOxygenService(), "Quest 6", 0, 50);
        OLQuest7 = new OxygenLevelQuest("Oxygen Level Quest 7", r7, 10, ServiceLocator.getPlanetOxygenService(), "Quest 7", 24, 0);
        OLQuest8 = new OxygenLevelQuest("Oxygen Level Quest 8", r8, 10, ServiceLocator.getPlanetOxygenService(), "Quest 8", 24, 50);
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    public void testRegisterMission() {
        EventHandler mockEventHandler = mock(EventHandler.class);
        assertTrue(OLQuest1.isCompleted());
        assertFalse(OLQuest2.isCompleted());
        assertFalse(OLQuest3.isCompleted());
        assertFalse(OLQuest4.isCompleted());
        assertTrue(OLQuest5.isCompleted());
        assertFalse(OLQuest6.isCompleted());
        assertFalse(OLQuest7.isCompleted());
        assertFalse(OLQuest8.isCompleted());
        OLQuest1.registerMission(mockEventHandler);
        OLQuest2.registerMission(mockEventHandler);
        OLQuest3.registerMission(mockEventHandler);
        OLQuest4.registerMission(mockEventHandler);
        OLQuest5.registerMission(mockEventHandler);
        OLQuest6.registerMission(mockEventHandler);
        OLQuest7.registerMission(mockEventHandler);
        OLQuest8.registerMission(mockEventHandler);
        verify(mockEventHandler, never()).addListener(anyString(), any(EventListener0.class));
        assertTrue(OLQuest1.isCompleted());
        assertFalse(OLQuest2.isCompleted());
        assertFalse(OLQuest3.isCompleted());
        assertFalse(OLQuest4.isCompleted());
        assertTrue(OLQuest5.isCompleted());
        assertFalse(OLQuest6.isCompleted());
        assertFalse(OLQuest7.isCompleted());
        assertFalse(OLQuest8.isCompleted());
    }
}