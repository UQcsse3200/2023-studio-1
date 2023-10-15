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
class FertiliseCropTilesQuestTest {

    private FertiliseCropTilesQuest FCTQuest1, FCTQuest2, FCTQuest3, FCTQuest4, FCTQuest5, FCTQuest6;
    private Reward r1, r2, r3, r4, r5, r6;

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

        FCTQuest1 = new FertiliseCropTilesQuest("Fertilise Crop Tiles Quest 1", r1, 10);
        FCTQuest2 = new FertiliseCropTilesQuest("Fertilise Crop Tiles Quest 2", r2, 10, 10);
        FCTQuest3 = new FertiliseCropTilesQuest("Fertilise Crop Tiles Quest 3", r3, -1);
        FCTQuest4 = new FertiliseCropTilesQuest("Fertilise Crop Tiles Quest 4", r4, 3);
        FCTQuest5 = new FertiliseCropTilesQuest("Fertilise Crop Tiles Quest 5", r5, 50);
        FCTQuest6 = new FertiliseCropTilesQuest("Fertilise Crop Tiles Quest 6", r6, 10, -1);

        FCTQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());

    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    void testRegisterMission() {
        ServiceLocator.clear();
        assertFalse(FCTQuest1.isCompleted());
        assertFalse(FCTQuest2.isCompleted());
        assertTrue(FCTQuest3.isCompleted());
        assertFalse(FCTQuest4.isCompleted());
        assertFalse(FCTQuest5.isCompleted());
        assertTrue(FCTQuest6.isCompleted());
        FCTQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        FCTQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertFalse(FCTQuest1.isCompleted());
        assertFalse(FCTQuest2.isCompleted());
        assertTrue(FCTQuest3.isCompleted());
        assertFalse(FCTQuest4.isCompleted());
        assertFalse(FCTQuest5.isCompleted());
        assertTrue(FCTQuest6.isCompleted());
    }

    @Test
    void testIsCompleted() {
        assertFalse(FCTQuest1.isCompleted());
        assertFalse(FCTQuest2.isCompleted());
        assertTrue(FCTQuest3.isCompleted());
        assertFalse(FCTQuest4.isCompleted());
        assertFalse(FCTQuest5.isCompleted());
        assertTrue(FCTQuest6.isCompleted());
        for (int i = 0; i < 10; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());
        }
        assertTrue(FCTQuest1.isCompleted());
        assertTrue(FCTQuest2.isCompleted());
        assertTrue(FCTQuest3.isCompleted());
        assertTrue(FCTQuest4.isCompleted());
        assertFalse(FCTQuest5.isCompleted());
        assertTrue(FCTQuest6.isCompleted());
        for (int i = 0; i < 40; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());
        }
        assertTrue(FCTQuest1.isCompleted());
        assertTrue(FCTQuest2.isCompleted());
        assertTrue(FCTQuest3.isCompleted());
        assertTrue(FCTQuest4.isCompleted());
        assertTrue(FCTQuest5.isCompleted());
        assertTrue(FCTQuest6.isCompleted());
    }

    @Test
    void testGetDescription() {
        String desc = "Fertilising crop tiles will cause your plants to grow faster.\n" +
                "Apply fertiliser to %d tiles.\n" +
                "%d out of %d crop tiles fertilised.";
        for (int i = 0; i < 50; i++) {
            int min123 = Math.min(i, 10);
            int min4 = Math.min(i, 3);
            String formatted1 = String.format(desc, 10, min123, 10);
            String formatted3 = String.format(desc, 0, 0, 0);
            String formatted4 = String.format(desc, 3, min4, 3);
            String formatted5 = String.format(desc, 50, i, 50);
            assertEquals(formatted1, FCTQuest1.getDescription());
            assertEquals(formatted3, FCTQuest3.getDescription());
            assertEquals(formatted4, FCTQuest4.getDescription());
            assertEquals(formatted5, FCTQuest5.getDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());
        }
    }

    @Test
    void testGetShortDescription() {
        String desc = "%d out of %d crop tiles fertilised";
        for (int i = 0; i < 50; i++) {
            int min123 = Math.min(i, 10);
            int min4 = Math.min(i, 3);
            String formatted1 = String.format(desc, min123, 10);
            String formatted3 = String.format(desc, 0, 0);
            String formatted4 = String.format(desc, min4, 3);
            String formatted5 = String.format(desc, i, 50);
            assertEquals(formatted1, FCTQuest1.getShortDescription());
            assertEquals(formatted3, FCTQuest3.getShortDescription());
            assertEquals(formatted4, FCTQuest4.getShortDescription());
            assertEquals(formatted5, FCTQuest5.getShortDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());
        }
    }

    @Test
    void testReadProgress() {
        int progressInt = 3;
        JsonValue progress = new JsonValue(progressInt);
        String desc = "Fertilising crop tiles will cause your plants to grow faster.\n" +
                "Apply fertiliser to %d tiles.\n" +
                "%d out of %d crop tiles fertilised.";
        String shortDesc = "%d out of %d crop tiles fertilised";
        FCTQuest1.readProgress(progress);
        FCTQuest3.readProgress(progress);
        FCTQuest4.readProgress(progress);
        FCTQuest5.readProgress(progress);
        String formatted1 = String.format(desc, 10, progressInt, 10);
        String formatted3 = String.format(desc, 0, progressInt, 0);
        String formatted4 = String.format(desc, 3, progressInt, 3);
        String formatted5 = String.format(desc, 50, progressInt, 50);
        String shortFormatted1 = String.format(shortDesc, progressInt, 10);
        String shortFormatted3 = String.format(shortDesc, progressInt, 0);
        String shortFormatted4 = String.format(shortDesc, progressInt, 3);
        String shortFormatted5 = String.format(shortDesc, progressInt, 50);
        assertEquals(formatted1, FCTQuest1.getDescription());
        assertEquals(formatted3, FCTQuest3.getDescription());
        assertEquals(formatted4, FCTQuest4.getDescription());
        assertEquals(formatted5, FCTQuest5.getDescription());
        assertEquals(shortFormatted1, FCTQuest1.getShortDescription());
        assertEquals(shortFormatted3, FCTQuest3.getShortDescription());
        assertEquals(shortFormatted4, FCTQuest4.getShortDescription());
        assertEquals(shortFormatted5, FCTQuest5.getShortDescription());
    }

    @Test
    void testGetProgress() {
        assertEquals(0, FCTQuest1.getProgress());
        assertEquals(0, FCTQuest2.getProgress());
        assertEquals(0, FCTQuest3.getProgress());
        assertEquals(0, FCTQuest4.getProgress());
        assertEquals(0, FCTQuest5.getProgress());
        assertEquals(0, FCTQuest6.getProgress());
        for (int i = 0; i < 50; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());
        }
        assertEquals(10, FCTQuest1.getProgress());
        assertEquals(10, FCTQuest2.getProgress());
        assertEquals(0, FCTQuest3.getProgress());
        assertEquals(3, FCTQuest4.getProgress());
        assertEquals(50, FCTQuest5.getProgress());
        assertEquals(0, FCTQuest6.getProgress());
    }

    @Test
    void testResetState() {
        for (int i = 0; i < 50; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FERTILISE_CROP.name());
        }
        assertTrue(FCTQuest1.isCompleted());
        assertTrue(FCTQuest2.isCompleted());
        assertTrue(FCTQuest3.isCompleted());
        assertTrue(FCTQuest4.isCompleted());
        assertTrue(FCTQuest5.isCompleted());
        assertTrue(FCTQuest6.isCompleted());
        FCTQuest1.resetState();
        FCTQuest2.resetState();
        FCTQuest3.resetState();
        FCTQuest4.resetState();
        FCTQuest5.resetState();
        FCTQuest6.resetState();
        assertFalse(FCTQuest1.isCompleted());
        assertFalse(FCTQuest2.isCompleted());
        assertTrue(FCTQuest3.isCompleted());
        assertFalse(FCTQuest4.isCompleted());
        assertFalse(FCTQuest5.isCompleted());
        assertTrue(FCTQuest6.isCompleted());
    }
}