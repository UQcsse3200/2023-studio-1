package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ShipRepairQuestTest {
    private ShipRepairQuest SRQuest1, SRQuest2, SRQuest3, SRQuest4, SRQuest5, SRQuest6, SRQuest7, SRQuest8;
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


        Set<String> plantTypes1 = new HashSet<>();
        Set<String> plantTypes2 = new HashSet<>();
        plantTypes1.add("Cosmic Cob");

        plantTypes2.add("Aloe Vera");
        plantTypes2.add("Cosmic Cob");

        MissionManager.MissionEvent plant = MissionManager.MissionEvent.PLANT_CROP;
        MissionManager.MissionEvent harvest = MissionManager.MissionEvent.HARVEST_CROP;
        MissionManager.MissionEvent bug = MissionManager.MissionEvent.ANIMAL_DEFEATED;

        SRQuest1 = new ShipRepairQuest("Ship Repair Quest 1", r1, 10);
        SRQuest1 = new ShipRepairQuest("Ship Repair Quest 1", r1, 0);
        SRQuest1 = new ShipRepairQuest("Ship Repair Quest 1", r1, 10, 10);

    }
}