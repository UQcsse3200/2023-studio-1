package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class AutoQuestTest {

    private AutoQuest autoQuest1, autoQuest2, autoQuest3;
    private Reward r1, r2, r3;

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
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        autoQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertTrue(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        autoQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertTrue(r1.isCollected());
        assertTrue(r2.isCollected());
        assertFalse(r3.isCollected());
        autoQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertTrue(r1.isCollected());
        assertTrue(r2.isCollected());
        assertTrue(r3.isCollected());
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
    void testReadProgress() {
        JsonValue mockProgress = mock(JsonValue.class);
        testGetDescription();
        String autoQuest1Desc = autoQuest1.getDescription();
        String autoQuest2Desc = autoQuest2.getDescription();
        String autoQuest3Desc = autoQuest3.getDescription();
        testGetProgress();
        testIsCompleted();
        testGetShortDescription();
        String autoQuest1ShortDesc = autoQuest1.getShortDescription();
        String autoQuest2ShortDesc = autoQuest2.getShortDescription();
        String autoQuest3ShortDesc = autoQuest3.getShortDescription();
        testRegisterMission();
        autoQuest1.readProgress(mockProgress);
        autoQuest2.readProgress(mockProgress);
        autoQuest3.readProgress(mockProgress);
        testGetDescription();
        assertEquals(autoQuest1Desc, autoQuest1.getDescription());
        assertEquals(autoQuest2Desc, autoQuest2.getDescription());
        assertEquals(autoQuest3Desc, autoQuest3.getDescription());
        testGetProgress();
        testIsCompleted();
        testGetShortDescription();
        assertEquals(autoQuest1ShortDesc, autoQuest1.getShortDescription());
        assertEquals(autoQuest2ShortDesc, autoQuest2.getShortDescription());
        assertEquals(autoQuest3ShortDesc, autoQuest3.getShortDescription());
        testResetState();
    }

    @Test
    void testGetProgress() {
        assertEquals(0, autoQuest1.getProgress());
        assertEquals(0, autoQuest2.getProgress());
        assertEquals(0, autoQuest3.getProgress());
    }

    @Test
    void testResetState() {
        testIsCompleted();
        autoQuest1.resetState();
        autoQuest2.resetState();
        autoQuest3.resetState();
        testIsCompleted();
    }
}