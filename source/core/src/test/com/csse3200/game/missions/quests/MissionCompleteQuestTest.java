package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

class MissionCompleteQuestTest {
    private MissionCompleteQuest MCQuest1, MCQuest2, MCQuest3, MCQuest4, MCQuest5;
    private Reward r1, r2, r3, r4, r5;
    String requirement1 = "Requirement Quest 1";
    String requirement2 = "Requirement Quest 2";
    String requirement3 = "Requirement Quest 3";

    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());

        Set<String> requiredQuests1 = new HashSet<>();
        Set<String> requiredQuests2 = new HashSet<>();
        Set<String> requiredQuests3 = new HashSet<>();

        requiredQuests1.add(requirement1);

        requiredQuests2.add(requirement1);
        requiredQuests2.add(requirement2);

        requiredQuests3.add(requirement1);
        requiredQuests3.add(requirement2);
        requiredQuests3.add(requirement3);

        r1 = mock(Reward.class);
        r2 = mock(Reward.class);
        r3 = mock(Reward.class);
        r4 = mock(Reward.class);
        r5 = mock(Reward.class);

        MCQuest1 = new MissionCompleteQuest("Mission Complete Quest 1", r1, 10);
        MCQuest2 = new MissionCompleteQuest("Mission Complete Quest 2", r2, 0);
        MCQuest3 = new MissionCompleteQuest("Mission Complete Quest 3", r3, -1);
        MCQuest4 = new MissionCompleteQuest("Mission Complete Quest 4", r4, 10, true, 1);
        MCQuest5 = new MissionCompleteQuest("Mission Complete Quest 5", r5, 10, false, 1);
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    public void testRegisterMission() {
        assertFalse(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        assertFalse(MCQuest4.isCompleted());
        assertFalse(MCQuest5.isCompleted());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
        MCQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        MCQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        MCQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        MCQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        MCQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertFalse(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        assertFalse(MCQuest4.isCompleted());
        assertFalse(MCQuest5.isCompleted());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
    }

    @Test
    public void testIsCompleted() {
        testRegisterMission();
        assertFalse(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        assertFalse(MCQuest4.isCompleted());
        assertFalse(MCQuest5.isCompleted());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.REWARD_COMPLETE.name());
        assertFalse(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        assertTrue(MCQuest4.isCompleted());
        assertTrue(MCQuest5.isCompleted());
        for (int i = 1; i < 10; i++){
            assertFalse(MCQuest1.isCompleted());
            assertTrue(MCQuest2.isCompleted());
            assertTrue(MCQuest3.isCompleted());
            assertTrue(MCQuest4.isCompleted());
            assertTrue(MCQuest5.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(
                    MissionManager.MissionEvent.REWARD_COMPLETE.name());
        }
        assertTrue(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        assertTrue(MCQuest4.isCompleted());
        assertTrue(MCQuest5.isCompleted());
    }

    @Test
    public void testGetDescription() {
        testRegisterMission();
        String desc = "Complete %d missions to get a tractor.\n%d out of %d required quests completed.";
        for (int i = 0; i < 10; i++) {
            int minNum23 = Math.min(i, 0);
            int minNum45 = Math.min(i, 1);
            assertEquals(String.format(desc, 10, i, 10), MCQuest1.getDescription());
            assertEquals(String.format(desc, 0, minNum23, 0), MCQuest2.getDescription());
            assertEquals(String.format(desc, 0, minNum23, 0), MCQuest3.getDescription());
            assertEquals(String.format(desc, 1, minNum45, 1), MCQuest4.getDescription());
            assertEquals(String.format(desc, 1, minNum45, 1), MCQuest5.getDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.REWARD_COMPLETE.name());
        }
    }

    @Test
    public void testGetShortDescription() {
        testRegisterMission();
        String desc = "%d out of %d required quests completed";
        for (int i = 0; i < 10; i++) {
            int minNum23 = Math.min(i, 0);
            int minNum45 = Math.min(i, 1);
            assertEquals(String.format(desc, i, 10), MCQuest1.getShortDescription());
            assertEquals(String.format(desc, minNum23, 0), MCQuest2.getShortDescription());
            assertEquals(String.format(desc, minNum23, 0), MCQuest3.getShortDescription());
            assertEquals(String.format(desc, minNum45, 1), MCQuest4.getShortDescription());
            assertEquals(String.format(desc, minNum45, 1), MCQuest5.getShortDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.REWARD_COMPLETE.name());
        }
    }

    @Test
    public void testReadProgress() {
        int progressInt = 3;
        JsonValue progress = new JsonValue(progressInt);
        assertEquals(0, MCQuest1.getProgress());
        assertEquals(0, MCQuest2.getProgress());
        assertEquals(0, MCQuest3.getProgress());
        assertEquals(0, MCQuest4.getProgress());
        assertEquals(0, MCQuest5.getProgress());
        MCQuest1.readProgress(progress);
        MCQuest2.readProgress(progress);
        MCQuest3.readProgress(progress);
        MCQuest4.readProgress(progress);
        MCQuest5.readProgress(progress);
        assertEquals(3, MCQuest1.getProgress());
        assertEquals(3, MCQuest2.getProgress());
        assertEquals(3, MCQuest3.getProgress());
        assertEquals(3, MCQuest4.getProgress());
        assertEquals(3, MCQuest5.getProgress());
        progress = new JsonValue(10);
        MCQuest1.readProgress(progress);
        assertEquals(10, MCQuest1.getProgress());
    }

    @Test
    public void testGetProgress() {
        assertEquals(0, MCQuest1.getProgress());
        assertEquals(0, MCQuest2.getProgress());
        assertEquals(0, MCQuest3.getProgress());
        assertEquals(0, MCQuest4.getProgress());
        assertEquals(0, MCQuest5.getProgress());
        testIsCompleted();
        assertNotEquals(0, MCQuest1.getProgress());
        assertEquals(0, MCQuest2.getProgress());
        assertEquals(0, MCQuest3.getProgress());
        assertNotEquals(0, MCQuest4.getProgress());
        assertNotEquals(0, MCQuest5.getProgress());
        assertEquals(10, MCQuest1.getProgress());
        assertEquals(0, MCQuest2.getProgress());
        assertEquals(0, MCQuest3.getProgress());
        assertEquals(1, MCQuest4.getProgress());
        assertEquals(1, MCQuest5.getProgress());
    }

    @Test
    public void testResetState() {
        testIsCompleted();
        assertTrue(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        assertTrue(MCQuest4.isCompleted());
        assertTrue(MCQuest5.isCompleted());
        MCQuest1.resetState();
        MCQuest2.resetState();
        MCQuest3.resetState();
        MCQuest4.resetState();
        MCQuest5.resetState();
        assertFalse(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        assertFalse(MCQuest4.isCompleted());
        assertFalse(MCQuest5.isCompleted());
    }
}