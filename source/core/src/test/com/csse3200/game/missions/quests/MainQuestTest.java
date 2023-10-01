package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MainQuestTest {
    private MainQuest mainQuest1, mainQuest2, mainQuest3;
    private Reward r1, r2, r3;

    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());

        Set<String> requiredQuests1 = new HashSet<>();
        Set<String> requiredQuests2 = new HashSet<>();
        Set<String> requiredQuests3 = new HashSet<>();

        requiredQuests1.add("Requirement Quest 1");

        requiredQuests2.add("Requirement Quest 1");
        requiredQuests2.add("Requirement Quest 2");

        requiredQuests3.add("Requirement Quest 1");
        requiredQuests3.add("Requirement Quest 2");
        requiredQuests3.add("Requirement Quest 3");

        r1 = mock(Reward.class);
        r2 = mock(Reward.class);
        r3 = mock(Reward.class);

        mainQuest1 = new MainQuest("Main Quest 1", r1, 10, requiredQuests1, "Test 1");
        mainQuest2 = new MainQuest("Main Quest 2", r2, 10, requiredQuests2, "Test 2");
        mainQuest3 = new MainQuest("Main Quest 3", r3, 10, requiredQuests3, "Test 3");
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    void testRegisterMission(){
        assertFalse(mainQuest1.isCompleted());
        assertFalse(mainQuest2.isCompleted());
        assertFalse(mainQuest3.isCompleted());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        mainQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        mainQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        mainQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertFalse(mainQuest1.isCompleted());
        assertFalse(mainQuest2.isCompleted());
        assertFalse(mainQuest3.isCompleted());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
    }
}