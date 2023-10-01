package com.csse3200.game.missions.quests;

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
    private Reward r1, r2, r3, r4, r5;

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
    void registerMission() {
    }

    @Test
    void testIsCompleted() {
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
    void readProgress() {
    }

    @Test
    void getProgress() {
    }

    @Test
    void resetState() {
    }
}