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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TameAnimalsQuestTest {
    private TameAnimalsQuest TAQuest1, TAQuest2, TAQuest3, TAQuest4, TAQuest5;
    private Reward r1 = mock(Reward.class);

    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());

        TAQuest1 = new TameAnimalsQuest("Tame Animals Quest 1", r1, 10);
        TAQuest2 = new TameAnimalsQuest("Tame Animals Quest 2", r1, -1);
        TAQuest3 = new TameAnimalsQuest("Tame Animals Quest 3", r1, 5);
        TAQuest4 = new TameAnimalsQuest("Tame Animals Quest 4", r1, 10, 10);
        TAQuest5 = new TameAnimalsQuest("Tame Animals Quest 5", r1, 10, -1);

    }
    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    public void testIsCompleted() {
        TAQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        for (int i = 0; i < 10; i++) {
            assertFalse(TAQuest1.isCompleted());
            assertTrue(TAQuest2.isCompleted());
            if (i >= 5) {
                assertTrue(TAQuest3.isCompleted());
            } else {
                assertFalse(TAQuest3.isCompleted());
            }
            assertFalse(TAQuest4.isCompleted());
            assertTrue(TAQuest5.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.ANIMAL_TAMED.name());
        }
        assertTrue(TAQuest1.isCompleted());
        assertTrue(TAQuest2.isCompleted());
        assertTrue(TAQuest3.isCompleted());
        assertTrue(TAQuest4.isCompleted());
        assertTrue(TAQuest5.isCompleted());
        TAQuest1.resetState();
        TAQuest2.resetState();
        TAQuest3.resetState();
        TAQuest4.resetState();
        TAQuest5.resetState();
        assertFalse(TAQuest1.isCompleted());
        assertTrue(TAQuest2.isCompleted());
        assertFalse(TAQuest3.isCompleted());
        assertFalse(TAQuest4.isCompleted());
        assertTrue(TAQuest5.isCompleted());
    }

    @Test
    public void testGetDescription() {
        TAQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        TAQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        String desc = "Taming certain animals yields special drops.\nTame %d tameable animals by " +
                "feeding them plants.\n%d out of %d animals tamed.";
        for (int i = 0; i < 10; i++) {
            assertEquals(String.format(desc, 10, i, 10), TAQuest1.getDescription());
            assertEquals(String.format(desc, 0, 0, 0), TAQuest2.getDescription());
            assertEquals(String.format(desc, 5, Math.min(i, 5), 5), TAQuest3.getDescription());
            assertEquals(String.format(desc, 10, i, 10), TAQuest4.getDescription());
            assertEquals(String.format(desc, 0, 0, 0), TAQuest5.getDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.ANIMAL_TAMED.name());
        }
    }

    @Test
    public void testReadAndGetProgress() {
        assertEquals(0, TAQuest1.getProgress());
        assertEquals(0, TAQuest2.getProgress());
        assertEquals(0, TAQuest3.getProgress());
        assertEquals(0, TAQuest4.getProgress());
        assertEquals(0, TAQuest5.getProgress());
        int progressInt = 3;
        JsonValue jsonValue = new JsonValue(progressInt);
        TAQuest1.readProgress(jsonValue);
        TAQuest2.readProgress(jsonValue);
        TAQuest3.readProgress(jsonValue);
        TAQuest4.readProgress(jsonValue);
        TAQuest5.readProgress(jsonValue);
        String desc = "Taming certain animals yields special drops.\nTame %d tameable animals by " +
                "feeding them plants.\n%d out of %d animals tamed.";
        assertEquals(String.format(desc, 10, progressInt, 10), TAQuest1.getDescription());
        assertEquals(String.format(desc, 0, progressInt, 0), TAQuest2.getDescription());
        assertEquals(String.format(desc, 5, progressInt, 5), TAQuest3.getDescription());
        assertEquals(String.format(desc, 10, progressInt, 10), TAQuest4.getDescription());
        assertEquals(String.format(desc, 0, progressInt, 0), TAQuest5.getDescription());
    }
}