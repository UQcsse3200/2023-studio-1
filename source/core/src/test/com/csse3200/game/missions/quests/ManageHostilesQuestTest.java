package com.csse3200.game.missions.quests;

import com.csse3200.game.entities.EntityType;
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

        Set<EntityType> hostileTypes1 = new HashSet<>();
        Set<EntityType> hostileTypes2 = new HashSet<>();
        Set<EntityType> hostileTypes3 = new HashSet<>();
        hostileTypes1.add(EntityType.OxygenEater);

        hostileTypes2.add(EntityType.OxygenEater);
        hostileTypes2.add(EntityType.Cow);

        hostileTypes3.add(EntityType.OxygenEater);
        hostileTypes3.add(EntityType.Cow);
        hostileTypes3.add(EntityType.Astrolotl);

        MHQuest1 = new ManageHostilesQuest("Manage Hostiles Quest 1", r1, hostileTypes1, 10);
        MHQuest2 = new ManageHostilesQuest("Manage Hostiles Quest 2", r2, hostileTypes2, 0);
        MHQuest3 = new ManageHostilesQuest("Manage Hostiles Quest 3", r3, hostileTypes2, 10);
        MHQuest4 = new ManageHostilesQuest("Manage Hostiles Quest 4", r4, hostileTypes3, 10);
        MHQuest5 = new ManageHostilesQuest("Manage Hostiles Quest 5", r5, hostileTypes3, -1);
        MHQuest6 = new ManageHostilesQuest("Manage Hostiles Quest 6", r6, 10, hostileTypes3, 10);

    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    public void testRegisterMission() {
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertFalse(MHQuest3.isCompleted());
        assertFalse(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertFalse(MHQuest6.isCompleted());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
        assertFalse(r6.isCollected());
        MHQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        MHQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        MHQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        MHQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        MHQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        MHQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
        assertFalse(r6.isCollected());
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertFalse(MHQuest3.isCompleted());
        assertFalse(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertFalse(MHQuest6.isCompleted());
    }

    @Test
    public void testIsCompleted() {
        testRegisterMission();
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertFalse(MHQuest3.isCompleted());
        assertFalse(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertFalse(MHQuest6.isCompleted());
        for (int i = 0; i < 10; i++) {
            assertFalse(MHQuest1.isCompleted());
            assertTrue(MHQuest2.isCompleted());
            assertFalse(MHQuest3.isCompleted());
            assertFalse(MHQuest4.isCompleted());
            assertTrue(MHQuest5.isCompleted());
            assertFalse(MHQuest6.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.ANIMAL_DEFEATED.name(),
                    EntityType.Astrolotl);
        }
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertFalse(MHQuest3.isCompleted());
        assertTrue(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertTrue(MHQuest6.isCompleted());
        MHQuest4.resetState();
        MHQuest6.resetState();
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertFalse(MHQuest3.isCompleted());
        assertFalse(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertFalse(MHQuest6.isCompleted());
        for (int i = 0; i < 10; i++) {
            assertFalse(MHQuest1.isCompleted());
            assertTrue(MHQuest2.isCompleted());
            assertFalse(MHQuest3.isCompleted());
            assertFalse(MHQuest4.isCompleted());
            assertTrue(MHQuest5.isCompleted());
            assertFalse(MHQuest6.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.ANIMAL_DEFEATED.name(),
                    EntityType.Cow);
        }
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertTrue(MHQuest3.isCompleted());
        assertTrue(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertTrue(MHQuest6.isCompleted());
        MHQuest3.resetState();
        MHQuest4.resetState();
        MHQuest6.resetState();
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertFalse(MHQuest3.isCompleted());
        assertFalse(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertFalse(MHQuest6.isCompleted());
        for (int i = 0; i < 10; i++) {
            assertFalse(MHQuest1.isCompleted());
            assertTrue(MHQuest2.isCompleted());
            assertFalse(MHQuest3.isCompleted());
            assertFalse(MHQuest4.isCompleted());
            assertTrue(MHQuest5.isCompleted());
            assertFalse(MHQuest6.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.ANIMAL_DEFEATED.name(),
                    EntityType.OxygenEater);
        }
        assertTrue(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertTrue(MHQuest3.isCompleted());
        assertTrue(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertTrue(MHQuest6.isCompleted());
    }

    @Test
    public void testResetState() {
        testIsCompleted();
        assertTrue(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertTrue(MHQuest3.isCompleted());
        assertTrue(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertTrue(MHQuest6.isCompleted());
        MHQuest1.resetState();
        MHQuest2.resetState();
        MHQuest3.resetState();
        MHQuest4.resetState();
        MHQuest5.resetState();
        MHQuest6.resetState();
        assertFalse(MHQuest1.isCompleted());
        assertTrue(MHQuest2.isCompleted());
        assertFalse(MHQuest3.isCompleted());
        assertFalse(MHQuest4.isCompleted());
        assertTrue(MHQuest5.isCompleted());
        assertFalse(MHQuest6.isCompleted());
    }
}