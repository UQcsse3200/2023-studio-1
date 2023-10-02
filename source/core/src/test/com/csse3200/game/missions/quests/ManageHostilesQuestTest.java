package com.csse3200.game.missions.quests;

import com.csse3200.game.entities.EntityType;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ManageHostilesQuestTest {
    private ManageHostilesQuest MHQuest1, MHQuest2, MHQuest3, MHQuest4, MHQuest5, MHQuest6;
    private Reward r1, r2, r3, r4, r5, r6;

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

        Set<EntityType>;

        MHQuest1 = new ManageHostilesQuest("Manage Hostiles Quest 1", r1, 10);

    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }
}