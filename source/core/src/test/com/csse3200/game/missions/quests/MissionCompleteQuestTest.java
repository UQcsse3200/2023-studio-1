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
        String desc = "Complete %d missions to get a tractor.\n";
        assertEquals(desc, MCQuest1.getDescription());
        assertEquals(desc, MCQuest2.getDescription());
        assertEquals(desc, MCQuest3.getDescription());
    }

    @Test
    public void testGetShortDescription() {
        testRegisterMission();
        String desc = "%d required quests to be completed";
        assertEquals(String.format(desc, 1), MCQuest1.getShortDescription());
        assertEquals(String.format(desc, 2), MCQuest2.getShortDescription());
        assertEquals(String.format(desc, 3), MCQuest3.getShortDescription());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
        assertEquals(String.format(desc, 0), MCQuest1.getShortDescription());
        assertEquals(String.format(desc, 1), MCQuest2.getShortDescription());
        assertEquals(String.format(desc, 2), MCQuest3.getShortDescription());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
        assertEquals(String.format(desc, 0), MCQuest1.getShortDescription());
        assertEquals(String.format(desc, 0), MCQuest2.getShortDescription());
        assertEquals(String.format(desc, 1), MCQuest3.getShortDescription());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
        assertEquals(String.format(desc, 0), MCQuest1.getShortDescription());
        assertEquals(String.format(desc, 0), MCQuest2.getShortDescription());
        assertEquals(String.format(desc, 0), MCQuest3.getShortDescription());
    }

    @Test
    public void testReadProgress() {
        JsonReader reader = new JsonReader();
        String[] progressArray1 = {requirement1};
        String[] progressArray2 = {requirement1, requirement2};
        String[] progressArray3 = {requirement1, requirement2, requirement3};
        JsonValue progress1 = reader.parse(Arrays.toString(progressArray1));
        JsonValue progress2 = reader.parse(Arrays.toString(progressArray2));
        JsonValue progress3 = reader.parse(Arrays.toString(progressArray3));
        MCQuest1.readProgress(progress1);
        MCQuest2.readProgress(progress1);
        MCQuest3.readProgress(progress1);
        assertTrue(MCQuest1.isCompleted());
        assertFalse(MCQuest2.isCompleted());
        assertFalse(MCQuest3.isCompleted());
        MCQuest1.readProgress(progress2);
        MCQuest2.readProgress(progress2);
        MCQuest3.readProgress(progress2);
        assertTrue(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertFalse(MCQuest3.isCompleted());
        MCQuest1.readProgress(progress3);
        MCQuest2.readProgress(progress3);
        MCQuest3.readProgress(progress3);
        assertTrue(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
    }

    @Test
    public void testGetProgress() {
        testRegisterMission();
        String[] progressArray1 = {requirement1};
        String[] progressArray2 = {requirement1, requirement2};
        String[] progressArray3 = {requirement1, requirement2, requirement3};
        String[] progressArray0 = {};
        assertArrayEquals(progressArray0, (Object[]) MCQuest1.getProgress());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
        assertArrayEquals(progressArray1, (Object[]) MCQuest1.getProgress());
        assertArrayEquals(progressArray1, (Object[]) MCQuest2.getProgress());
        assertArrayEquals(progressArray1, (Object[]) MCQuest3.getProgress());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
        assertArrayEquals(progressArray1, (Object[]) MCQuest1.getProgress());
        assertArrayEquals(progressArray2, (Object[]) MCQuest2.getProgress());
        assertArrayEquals(progressArray2, (Object[]) MCQuest3.getProgress());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
        assertArrayEquals(progressArray1, (Object[]) MCQuest1.getProgress());
        assertArrayEquals(progressArray2, (Object[]) MCQuest2.getProgress());
        assertArrayEquals(progressArray3, (Object[]) MCQuest3.getProgress());
        assertTrue(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
    }

    @Test
    public void testResetState() {
        testIsCompleted();
        assertTrue(MCQuest1.isCompleted());
        assertTrue(MCQuest2.isCompleted());
        assertTrue(MCQuest3.isCompleted());
        MCQuest1.resetState();
        MCQuest2.resetState();
        MCQuest3.resetState();
        assertFalse(MCQuest1.isCompleted());
        assertFalse(MCQuest2.isCompleted());
        assertFalse(MCQuest3.isCompleted());
    }
}