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

    private ClearDebrisQuest CDQuest1, CDQuest2, CDQuest3, CDQuest4, CDQuest5, CDQuest6, CDQuest7;
    private Reward r1, r2, r3, r4, r5, r6, r7;

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
        r4 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r5 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r6 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };
        r7 = new Reward() {
            @Override
            public void collect() {
                setCollected();
            }
        };

        CDQuest1 = new ClearDebrisQuest("Clear Debris Quest 1", r1, 10);
        CDQuest2 = new ClearDebrisQuest("Clear Debris Quest 2", r2, 10, false, 10);
        CDQuest3 = new ClearDebrisQuest("Clear Debris Quest 3", r3, 10, true, 10);
        CDQuest4 = new ClearDebrisQuest("Clear Debris Quest 4", r4, 3);
        CDQuest5 = new ClearDebrisQuest("Clear Debris Quest 5", r5, 50);
        CDQuest6 = new ClearDebrisQuest("Clear Debris Quest 6", r6, 0);
        CDQuest7 = new ClearDebrisQuest("Clear Debris Quest 7", r7, -1);
    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    void testRegisterMission() {
        assertFalse(CDQuest1.isCompleted());
        assertFalse(CDQuest2.isCompleted());
        assertFalse(CDQuest3.isCompleted());
        assertFalse(CDQuest4.isCompleted());
        assertFalse(CDQuest5.isCompleted());
        assertTrue(CDQuest6.isCompleted());
        assertTrue(CDQuest7.isCompleted());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
        assertFalse(r6.isCollected());
        assertFalse(r7.isCollected());
        CDQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
        CDQuest7.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
        assertFalse(r6.isCollected());
        assertFalse(r7.isCollected());
        assertFalse(CDQuest1.isCompleted());
        assertFalse(CDQuest2.isCompleted());
        assertFalse(CDQuest3.isCompleted());
        assertFalse(CDQuest4.isCompleted());
        assertFalse(CDQuest5.isCompleted());
        assertTrue(CDQuest6.isCompleted());
        assertTrue(CDQuest7.isCompleted());
    }

    @Test
    void testUpdateState() {
    }

    @Test
    void testIsCompleted() {
        testRegisterMission();
        for (int i = 0; i < 10; i++) {
            assertFalse(CDQuest1.isCompleted());
            assertFalse(CDQuest2.isCompleted());
            assertFalse(CDQuest3.isCompleted());
            if (i < 3){
                assertFalse(CDQuest4.isCompleted());
            } else {
                assertTrue(CDQuest4.isCompleted());
            }
            assertFalse(CDQuest5.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
        }
        assertTrue(CDQuest1.isCompleted());
        assertTrue(CDQuest2.isCompleted());
        assertTrue(CDQuest3.isCompleted());
        assertTrue(CDQuest4.isCompleted());
        assertFalse(CDQuest5.isCompleted());
        for (int i = 0; i < 40; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
        }
        assertTrue(CDQuest1.isCompleted());
        assertTrue(CDQuest2.isCompleted());
        assertTrue(CDQuest3.isCompleted());
        assertTrue(CDQuest4.isCompleted());
        assertTrue(CDQuest5.isCompleted());
    }

    @Test
    void testGetDescription() {
        testRegisterMission();
        String desc = "Gather scattered parts of your ship.\n" +
                "Use your shovel to clear %d Ship Debris in the world!\n" +
                "%d out of %d debris pieces cleared.";
        for (int i = 0; i < 50; i++) {
            int max123 = Math.min(i, 10);
            int max4 = Math.min(i, 3);
            String formatted1 = String.format(desc, 10, max123, 10);
            String formatted2 = String.format(desc, 10, max123, 10);
            String formatted3 = String.format(desc, 10, max123, 10);
            String formatted4 = String.format(desc, 3, max4, 3);
            String formatted5 = String.format(desc, 50, i, 50);
            String formatted6 = String.format(desc, 0, 0, 0);
            String formatted7 = String.format(desc, 0, 0, 0);
            assertEquals(formatted1, CDQuest1.getDescription());
            assertEquals(formatted2, CDQuest2.getDescription());
            assertEquals(formatted3, CDQuest3.getDescription());
            assertEquals(formatted4, CDQuest4.getDescription());
            assertEquals(formatted5, CDQuest5.getDescription());
            assertEquals(formatted6, CDQuest6.getDescription());
            assertEquals(formatted7, CDQuest7.getDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
        }
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