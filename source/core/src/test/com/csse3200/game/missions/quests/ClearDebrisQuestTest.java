package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
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
class ClearDebrisQuestTest {

    private ClearDebrisQuest CDQuest1, CDQuest2, CDQuest3;
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


        CDQuest1 = new ClearDebrisQuest("Clear Debris Quest 1", r1, 10);
        CDQuest2 = new ClearDebrisQuest("Clear Debris Quest 2", r2, 10, false, 10);
        CDQuest3 = new ClearDebrisQuest("Clear Debris Quest 3", r3, 10, true, 10);
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
        CDQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());

    @Test
    void testUpdateState() {
    }

    @Test
    void testIsCompleted() {
        assertTrue(CDQuest1.isCompleted());
        assertTrue(CDQuest2.isCompleted());
        assertTrue(CDQuest3.isCompleted());
    }

    @Test
    void testGetDescription() {
        assertEquals("Clear Debris Quest 1", CDQuest1.getDescription());
        assertEquals("Clear Debris Quest 2", CDQuest2.getDescription());
        assertEquals("Clear Debris Quest 3", CDQuest3.getDescription());
    }

    @Test
    void testGetShortDescription() {
        assertEquals("Clear Debris Quest 1", CDQuest1.getShortDescription());
        assertEquals("Clear Debris Quest 2", CDQuest2.getShortDescription());
        assertEquals("Clear Debris Quest 3", CDQuest3.getShortDescription());
        assertEquals(CDQuest1.getDescription(), CDQuest1.getShortDescription());
        assertEquals(CDQuest2.getDescription(), CDQuest2.getShortDescription());
        assertEquals(CDQuest3.getDescription(), CDQuest3.getShortDescription());
    }

    @Test
    void testReadProgress() {
        JsonValue mockProgress = mock(JsonValue.class);
        testGetDescription();
        String CDQuest1Desc = CDQuest1.getDescription();
        String CDQuest2Desc = CDQuest2.getDescription();
        String CDQuest3Desc = CDQuest3.getDescription();
        testGetProgress();
        testIsCompleted();
        testGetShortDescription();
        String CDQuest1ShortDesc = CDQuest1.getShortDescription();
        String CDQuest2ShortDesc = CDQuest2.getShortDescription();
        String CDQuest3ShortDesc = CDQuest3.getShortDescription();
        testRegisterMission();
        CDQuest1.readProgress(mockProgress);
        CDQuest2.readProgress(mockProgress);
        CDQuest3.readProgress(mockProgress);
        testGetDescription();
        assertEquals(CDQuest1Desc, CDQuest1.getDescription());
        assertEquals(CDQuest2Desc, CDQuest2.getDescription());
        assertEquals(CDQuest3Desc, CDQuest3.getDescription());
        testGetProgress();
        testIsCompleted();
        testGetShortDescription();
        assertEquals(CDQuest1ShortDesc, CDQuest1.getShortDescription());
        assertEquals(CDQuest2ShortDesc, CDQuest2.getShortDescription());
        assertEquals(CDQuest3ShortDesc, CDQuest3.getShortDescription());
        testResetState();
    }

    @Test
    void testGetProgress() {
        assertEquals(0, CDQuest1.getProgress());
        assertEquals(0, CDQuest2.getProgress());
        assertEquals(0, CDQuest3.getProgress());
    }

    @Test
    void testResetState() {
        testIsCompleted();
        CDQuest1.resetState();
        CDQuest2.resetState();
        CDQuest3.resetState();
        testIsCompleted();
    }
}