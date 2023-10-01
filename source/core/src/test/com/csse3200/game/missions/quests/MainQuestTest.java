package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MainQuestTest {
    private MainQuest mainQuest1, mainQuest2, mainQuest3, mainQuest4, mainQuest5, mainQuest6, mainQuest7;
    private Reward r1, r2, r3, r4, r5, r6, r7;

    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());

        r1 = mock(Reward.class);
        r2 = mock(Reward.class);
        r3 = mock(Reward.class);
        r4 = mock(Reward.class);
        r5 = mock(Reward.class);
        r6 = mock(Reward.class);
        r7 = mock(Reward.class);

        mainQuest1 = new MainQuest("Main Quest 1", r1, 10);
        mainQuest2 = new MainQuest("Main Quest 2", r2, 10, false, 10);
        mainQuest3 = new MainQuest("Main Quest 3", r3, 10, true, 10);
        mainQuest4 = new MainQuest("Main Quest 4", r4, 10);
        mainQuest5 = new MainQuest("Main Quest 5", r5, 10);
        mainQuest6 = new MainQuest("Main Quest 6", r6, 10, false, -1);
        mainQuest7 = new MainQuest("Main Quest 7", r7, 10);
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }
}