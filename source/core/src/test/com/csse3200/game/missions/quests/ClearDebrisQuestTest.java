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

        r1 = mock(Reward.class);
        r2 = mock(Reward.class);
        r3 = mock(Reward.class);
        r4 = mock(Reward.class);
        r5 = mock(Reward.class);
        r6 = mock(Reward.class);
        r7 = mock(Reward.class);

        CDQuest1 = new ClearDebrisQuest("Clear Debris Quest 1", r1, 10);
        CDQuest2 = new ClearDebrisQuest("Clear Debris Quest 2", r2, 10, false, 10);
        CDQuest3 = new ClearDebrisQuest("Clear Debris Quest 3", r3, 10, true, 10);
        CDQuest4 = new ClearDebrisQuest("Clear Debris Quest 4", r4, 3);
        CDQuest5 = new ClearDebrisQuest("Clear Debris Quest 5", r5, 50);
        CDQuest6 = new ClearDebrisQuest("Clear Debris Quest 6", r6, 10, false, -1);
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
            int min123 = Math.min(i, 10);
            int min4 = Math.min(i, 3);
            String formatted1 = String.format(desc, 10, min123, 10);
            String formatted2 = String.format(desc, 10, min123, 10);
            String formatted3 = String.format(desc, 10, min123, 10);
            String formatted4 = String.format(desc, 3, min4, 3);
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
        testRegisterMission();
        String desc = "%d out of %d debris pieces cleared";
        for (int i = 0; i < 50; i++) {
            int min123 = Math.min(i, 10);
            int min4 = Math.min(i, 3);
            String formatted1 = String.format(desc, min123, 10);
            String formatted2 = String.format(desc, min123, 10);
            String formatted3 = String.format(desc, min123, 10);
            String formatted4 = String.format(desc, min4, 3);
            String formatted5 = String.format(desc, i, 50);
            String formatted6 = String.format(desc, 0, 0);
            String formatted7 = String.format(desc, 0, 0);
            assertEquals(formatted1, CDQuest1.getShortDescription());
            assertEquals(formatted2, CDQuest2.getShortDescription());
            assertEquals(formatted3, CDQuest3.getShortDescription());
            assertEquals(formatted4, CDQuest4.getShortDescription());
            assertEquals(formatted5, CDQuest5.getShortDescription());
            assertEquals(formatted6, CDQuest6.getShortDescription());
            assertEquals(formatted7, CDQuest7.getShortDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.DEBRIS_CLEARED.name());
        }
    }

    @Test
    void testReadProgress() {
        int progressInt = 3;
        JsonValue progress = new JsonValue(progressInt);
        String desc = "Gather scattered parts of your ship.\n" +
                "Use your shovel to clear %d Ship Debris in the world!\n" +
                "%d out of %d debris pieces cleared.";
        String shortDesc = "%d out of %d debris pieces cleared";
        CDQuest1.readProgress(progress);
        CDQuest2.readProgress(progress);
        CDQuest3.readProgress(progress);
        CDQuest4.readProgress(progress);
        CDQuest5.readProgress(progress);
        CDQuest6.readProgress(progress);
        CDQuest7.readProgress(progress);
        String formatted1 = String.format(desc, 10, progressInt, 10);
        String formatted2 = String.format(desc, 10, progressInt, 10);
        String formatted3 = String.format(desc, 10, progressInt, 10);
        String formatted4 = String.format(desc, 3, progressInt, 3);
        String formatted5 = String.format(desc, 50, progressInt, 50);
        String formatted6 = String.format(desc, 0, progressInt, 0);
        String formatted7 = String.format(desc, 0, progressInt, 0);
        String shortFormatted1 = String.format(shortDesc, progressInt, 10);
        String shortFormatted2 = String.format(shortDesc, progressInt, 10);
        String shortFormatted3 = String.format(shortDesc, progressInt, 10);
        String shortFormatted4 = String.format(shortDesc, progressInt, 3);
        String shortFormatted5 = String.format(shortDesc, progressInt, 50);
        String shortFormatted6 = String.format(shortDesc, progressInt, 0);
        String shortFormatted7 = String.format(shortDesc, progressInt, 0);
        assertEquals(formatted1, CDQuest1.getDescription());
        assertEquals(formatted2, CDQuest2.getDescription());
        assertEquals(formatted3, CDQuest3.getDescription());
        assertEquals(formatted4, CDQuest4.getDescription());
        assertEquals(formatted5, CDQuest5.getDescription());
        assertEquals(formatted6, CDQuest6.getDescription());
        assertEquals(formatted7, CDQuest7.getDescription());
        assertEquals(shortFormatted1, CDQuest1.getShortDescription());
        assertEquals(shortFormatted2, CDQuest2.getShortDescription());
        assertEquals(shortFormatted3, CDQuest3.getShortDescription());
        assertEquals(shortFormatted4, CDQuest4.getShortDescription());
        assertEquals(shortFormatted5, CDQuest5.getShortDescription());
        assertEquals(shortFormatted6, CDQuest6.getShortDescription());
        assertEquals(shortFormatted7, CDQuest7.getShortDescription());
    }

    @Test
    void testGetProgress() {
        assertEquals(0, CDQuest1.getProgress());
        assertEquals(0, CDQuest2.getProgress());
        assertEquals(0, CDQuest3.getProgress());
        assertEquals(0, CDQuest4.getProgress());
        assertEquals(0, CDQuest5.getProgress());
        assertEquals(0, CDQuest6.getProgress());
        assertEquals(0, CDQuest7.getProgress());
        testIsCompleted();
        assertNotEquals(0, CDQuest1.getProgress());
        assertNotEquals(0, CDQuest2.getProgress());
        assertNotEquals(0, CDQuest3.getProgress());
        assertNotEquals(0, CDQuest4.getProgress());
        assertNotEquals(0, CDQuest5.getProgress());
        assertEquals(0, CDQuest6.getProgress());
        assertEquals(0, CDQuest7.getProgress());
    }

    @Test
    void testResetState() {
        testIsCompleted();
        CDQuest1.resetState();
        CDQuest2.resetState();
        CDQuest3.resetState();
        CDQuest4.resetState();
        CDQuest5.resetState();
        CDQuest6.resetState();
        CDQuest7.resetState();
        assertFalse(CDQuest1.isCompleted());
        assertFalse(CDQuest2.isCompleted());
        assertFalse(CDQuest3.isCompleted());
        assertFalse(CDQuest4.isCompleted());
        assertFalse(CDQuest5.isCompleted());
        assertTrue(CDQuest6.isCompleted());
        assertTrue(CDQuest7.isCompleted());
    }
}