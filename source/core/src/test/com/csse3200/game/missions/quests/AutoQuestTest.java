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

class AutoQuestTest {

    private AutoQuest autoQuest1, autoQuest2, autoQuest3;
    private Reward r1, r2, r3;
    private boolean areQuestsRegistered = false;

    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());

        r1 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r2 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r3 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };


        autoQuest1 = new AutoQuest("AQ1", r1, "Auto Quest 1");
        autoQuest2 = new AutoQuest("AQ2", r2, "Auto Quest 2");
        autoQuest3 = new AutoQuest("AQ3", r3, "Auto Quest 3");
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    void testRegisterMission() {
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        autoQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertTrue(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        autoQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertTrue(r1.isCollected());
        assertTrue(r2.isCollected());
        assertFalse(r3.isCollected());
        autoQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertTrue(r1.isCollected());
        assertTrue(r2.isCollected());
        assertTrue(r3.isCollected());
        areQuestsRegistered = true;
    }

    private void registerQuests() {
        if (!areQuestsRegistered) {
            autoQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
            autoQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
            autoQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
            areQuestsRegistered = true;
        }
    }

    @Test
    void testIsCompleted() {
        registerQuests();
        assertFalse(autoQuest1.isCompleted());
        assertFalse(autoQuest2.isCompleted());
        assertFalse(autoQuest3.isCompleted());
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertTrue(autoQuest1.isCompleted());
        assertTrue(autoQuest2.isCompleted());
        assertTrue(autoQuest3.isCompleted());
    }

    @Test
    void testGetDescription() {
        assertEquals("Auto Quest 1", autoQuest1.getDescription());
        assertEquals("Auto Quest 2", autoQuest2.getDescription());
        assertEquals("Auto Quest 3", autoQuest3.getDescription());
    }

    @Test
    void testGetShortDescription() {
        assertEquals("Auto Quest 1", autoQuest1.getShortDescription());
        assertEquals("Auto Quest 2", autoQuest2.getShortDescription());
        assertEquals("Auto Quest 3", autoQuest3.getShortDescription());
        assertEquals(autoQuest1.getDescription(), autoQuest1.getShortDescription());
        assertEquals(autoQuest2.getDescription(), autoQuest2.getShortDescription());
        assertEquals(autoQuest3.getDescription(), autoQuest3.getShortDescription());
    }

    @Test
    void testReadProgress() {
        registerQuests();
        autoQuest1.readProgress(new JsonValue(5));
        autoQuest2.readProgress(new JsonValue(5));
        autoQuest3.readProgress(new JsonValue(5));
        assertEquals(5, autoQuest1.getProgress());
        assertEquals(5, autoQuest2.getProgress());
        assertEquals(5, autoQuest3.getProgress());
        for (int i = 0; i < 5; i++) {
            ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        }
        assertEquals(0, autoQuest1.getProgress());
        assertEquals(0, autoQuest2.getProgress());
        assertEquals(0, autoQuest3.getProgress());
        autoQuest1.readProgress(new JsonValue(1));
        autoQuest2.readProgress(new JsonValue(1));
        autoQuest3.readProgress(new JsonValue(1));

        assertEquals(1, autoQuest1.getProgress());
        assertEquals(1, autoQuest2.getProgress());
        assertEquals(1, autoQuest3.getProgress());
    }

    @Test
    void testGetProgress() {
        registerQuests();
        assertEquals(1, autoQuest1.getProgress());
        assertEquals(1, autoQuest2.getProgress());
        assertEquals(1, autoQuest3.getProgress());
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertEquals(0, autoQuest1.getProgress());
        assertEquals(0, autoQuest2.getProgress());
        assertEquals(0, autoQuest3.getProgress());
    }

    @Test
    void testResetState() {
        registerQuests();
        assertFalse(autoQuest1.isCompleted());
        assertFalse(autoQuest2.isCompleted());
        assertFalse(autoQuest3.isCompleted());
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertTrue(autoQuest1.isCompleted());
        assertTrue(autoQuest2.isCompleted());
        assertTrue(autoQuest3.isCompleted());
        autoQuest1.resetState();
        autoQuest2.resetState();
        autoQuest3.resetState();
        assertFalse(autoQuest1.isCompleted());
        assertFalse(autoQuest2.isCompleted());
        assertFalse(autoQuest3.isCompleted());
        ServiceLocator.getTimeService().getEvents().trigger("minuteUpdate");
        assertTrue(autoQuest1.isCompleted());
        assertTrue(autoQuest2.isCompleted());
        assertTrue(autoQuest3.isCompleted());
    }
}