package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.PlanetOxygenService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OxygenLevelQuestTest {
    
    private OxygenLevelQuest OLQuest1, OLQuest2, OLQuest3, OLQuest4, OLQuest5, OLQuest6, OLQuest7;
    private Reward r1, r2, r3, r4, r5, r6, r7;
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

        OLQuest1 = new OxygenLevelQuest("Oxygen Level Quest 1", r1, ServiceLocator.getPlanetOxygenService(), "Quest 1", 168, 50);
        OLQuest2 = new OxygenLevelQuest("Oxygen Level Quest 2", r2, ServiceLocator.getPlanetOxygenService(), "Quest 2", 0, 0);
        OLQuest3 = new OxygenLevelQuest("Oxygen Level Quest 3", r3, ServiceLocator.getPlanetOxygenService(), "Quest 3", 168, 50);

        OLQuest4 = new OxygenLevelQuest("Oxygen Level Quest 4", r4, ServiceLocator.getPlanetOxygenService(), "Quest 4", 168, 50);
        OLQuest5 = new OxygenLevelQuest("Oxygen Level Quest 5", r5, ServiceLocator.getPlanetOxygenService(), "Quest 5", 168, 50);
        OLQuest6 = new OxygenLevelQuest("Oxygen Level Quest 6", r6, ServiceLocator.getPlanetOxygenService(), "Quest 6", 168, 50);
        OLQuest7 = new OxygenLevelQuest("Oxygen Level Quest 7", r7, ServiceLocator.getPlanetOxygenService(), "Quest 7", 168, 50);
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }
}