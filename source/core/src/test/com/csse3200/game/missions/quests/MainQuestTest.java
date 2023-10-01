package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
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

        mainQuest1 = new MainQuest("Main Quest 1", r1, 10, requiredQuests1, "Test 1");
        mainQuest2 = new MainQuest("Main Quest 2", r2, 10, requiredQuests2, "Test 2");
        mainQuest3 = new MainQuest("Main Quest 3", r3, 10, requiredQuests3, "Test 3");
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    void testRegisterMission() {
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

    @Test
    void testIsCompleted() {
        testRegisterMission();
        assertFalse(mainQuest1.isCompleted());
        assertFalse(mainQuest2.isCompleted());
        assertFalse(mainQuest3.isCompleted());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
        assertTrue(mainQuest1.isCompleted());
        assertFalse(mainQuest2.isCompleted());
        assertFalse(mainQuest3.isCompleted());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
        assertTrue(mainQuest1.isCompleted());
        assertTrue(mainQuest2.isCompleted());
        assertFalse(mainQuest3.isCompleted());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
        assertTrue(mainQuest1.isCompleted());
        assertTrue(mainQuest2.isCompleted());
        assertTrue(mainQuest3.isCompleted());
    }

    @Test
    void testGetDescription() {
        testRegisterMission();
        String desc1 = "You must Test 1!\nComplete the quests: " + requirement1 + ".";
        String desc2 = "You must Test 2!\nComplete the quests: " + requirement1 + ", " + requirement2 + ".";
        String desc3 = "You must Test 3!\nComplete the quests: " + requirement1 + ", " + requirement2 + ", " +
                requirement3 + ".";
        assertEquals(desc1, mainQuest1.getDescription());
        assertEquals(desc2, mainQuest2.getDescription());
        assertEquals(desc3, mainQuest3.getDescription());
    }

    @Test
    void testGetShortDescription() {
        testRegisterMission();
        String desc = "%d required quests to be completed";
        assertEquals(String.format(desc, 1), mainQuest1.getShortDescription());
        assertEquals(String.format(desc, 2), mainQuest2.getShortDescription());
        assertEquals(String.format(desc, 3), mainQuest3.getShortDescription());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement1);
        assertEquals(String.format(desc, 0), mainQuest1.getShortDescription());
        assertEquals(String.format(desc, 1), mainQuest2.getShortDescription());
        assertEquals(String.format(desc, 2), mainQuest3.getShortDescription());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement2);
        assertEquals(String.format(desc, 0), mainQuest1.getShortDescription());
        assertEquals(String.format(desc, 0), mainQuest2.getShortDescription());
        assertEquals(String.format(desc, 1), mainQuest3.getShortDescription());
        ServiceLocator.getMissionManager().getEvents().trigger(
                MissionManager.MissionEvent.QUEST_REWARD_COLLECTED.name(), requirement3);
        assertEquals(String.format(desc, 0), mainQuest1.getShortDescription());
        assertEquals(String.format(desc, 0), mainQuest2.getShortDescription());
        assertEquals(String.format(desc, 0), mainQuest3.getShortDescription());
    }

//    @Test
//    void testReadProgress() {
//        int progressInt = 3;
//        JsonValue progress = new JsonValue(progressInt);
//        String desc = "Fertilising crop tiles will cause your plants to grow faster.\n" +
//                "Apply fertiliser to %d tiles.\n" +
//                "%d out of %d crop tiles fertilised.";
//        String shortDesc = "%d out of %d crop tiles fertilised";
//        mainQuest1.readProgress(progress);
//        mainQuest2.readProgress(progress);
//        mainQuest3.readProgress(progress);
//        String formatted1 = String.format(desc, 10, progressInt, 10);
//        String formatted2 = String.format(desc, 10, progressInt, 10);
//        String formatted3 = String.format(desc, 0, progressInt, 0);
//        String formatted4 = String.format(desc, 3, progressInt, 3);
//        String formatted5 = String.format(desc, 50, progressInt, 50);
//        String formatted6 = String.format(desc, 0, progressInt, 0);
//        String shortFormatted1 = String.format(shortDesc, progressInt, 10);
//        String shortFormatted2 = String.format(shortDesc, progressInt, 10);
//        String shortFormatted3 = String.format(shortDesc, progressInt, 0);
//        String shortFormatted4 = String.format(shortDesc, progressInt, 3);
//        String shortFormatted5 = String.format(shortDesc, progressInt, 50);
//        String shortFormatted6 = String.format(shortDesc, progressInt, 0);
//        assertEquals(formatted1, mainQuest1.getDescription());
//        assertEquals(formatted2, mainQuest2.getDescription());
//        assertEquals(formatted3, mainQuest3.getDescription());
//        assertEquals(shortFormatted1, mainQuest1.getShortDescription());
//        assertEquals(shortFormatted2, mainQuest2.getShortDescription());
//        assertEquals(shortFormatted3, mainQuest3.getShortDescription());
//    }
//
//    @Test
//    void testGetProgress() {
//        assertEquals(0, mainQuest1.getProgress());
//        assertEquals(0, mainQuest2.getProgress());
//        assertEquals(0, mainQuest3.getProgress());
//        testIsCompleted();
//        assertNotEquals(0, mainQuest1.getProgress());
//        assertNotEquals(0, mainQuest2.getProgress());
//        assertEquals(0, mainQuest3.getProgress());
//        assertEquals(10, mainQuest1.getProgress());
//        assertEquals(10, mainQuest2.getProgress());
//        assertEquals(0, mainQuest3.getProgress());
//    }

    @Test
    void testResetState() {
        testIsCompleted();
        assertTrue(mainQuest1.isCompleted());
        assertTrue(mainQuest2.isCompleted());
        assertTrue(mainQuest3.isCompleted());
        mainQuest1.resetState();
        mainQuest2.resetState();
        mainQuest3.resetState();
        assertFalse(mainQuest1.isCompleted());
        assertFalse(mainQuest2.isCompleted());
        assertFalse(mainQuest3.isCompleted());
    }
}