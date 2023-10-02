package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.entities.EntityType;
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

class PlantInteractionQuestTest {
    private PlantInteractionQuest PIQuest1, PIQuest2, PIQuest3, PIQuest4, PIQuest5, PIQuest6, PIQuest7;
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


        Set<String> plantTypes1 = new HashSet<>();
        Set<String> plantTypes2 = new HashSet<>();
        plantTypes1.add("Cosmic Cob");

        plantTypes2.add("Aloe Vera");
        plantTypes2.add("Cosmic Cob");

        MissionManager.MissionEvent plant = MissionManager.MissionEvent.PLANT_CROP;
        MissionManager.MissionEvent harvest = MissionManager.MissionEvent.HARVEST_CROP;

        PIQuest1 = new PlantInteractionQuest("Plant Interaction Quest 1", r1, plant, plantTypes1, 10);
        PIQuest2 = new PlantInteractionQuest("Plant Interaction Quest 2", r2, plant, plantTypes1, 0);
        PIQuest3 = new PlantInteractionQuest("Plant Interaction Quest 3", r3, harvest, plantTypes1, 10);
        PIQuest4 = new PlantInteractionQuest("Plant Interaction Quest 4", r4, plant, plantTypes2, 10);
        PIQuest5 = new PlantInteractionQuest("Plant Interaction Quest 5", r5, harvest, plantTypes2, -1);
        PIQuest6 = new PlantInteractionQuest("Plant Interaction Quest 6", r6,10, harvest, plantTypes2, 3);
        PIQuest7 = new PlantInteractionQuest("Plant Interaction Quest 7", r7, 10, plant, plantTypes2, 3);

    }

    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    public void testRegisterMission() {
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertFalse(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertFalse(PIQuest6.isCompleted());
        assertFalse(PIQuest7.isCompleted());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
        assertFalse(r6.isCollected());
        assertFalse(r7.isCollected());
        PIQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        PIQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        PIQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        PIQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        PIQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        PIQuest6.registerMission(ServiceLocator.getMissionManager().getEvents());
        PIQuest7.registerMission(ServiceLocator.getMissionManager().getEvents());
        assertFalse(r1.isCollected());
        assertFalse(r2.isCollected());
        assertFalse(r3.isCollected());
        assertFalse(r4.isCollected());
        assertFalse(r5.isCollected());
        assertFalse(r6.isCollected());
        assertFalse(r7.isCollected());
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertFalse(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertFalse(PIQuest6.isCompleted());
        assertFalse(PIQuest7.isCompleted());
    }

    @Test
    public void testIsCompleted() {
        testRegisterMission();
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertFalse(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertFalse(PIQuest6.isCompleted());
        assertFalse(PIQuest7.isCompleted());
        for (int i = 0; i < 10; i++) {
            assertFalse(PIQuest1.isCompleted());
            assertTrue(PIQuest2.isCompleted());
            assertFalse(PIQuest3.isCompleted());
            assertFalse(PIQuest4.isCompleted());
            assertTrue(PIQuest5.isCompleted());
            assertFalse(PIQuest6.isCompleted());
            if (i >= 3) {
                assertTrue(PIQuest7.isCompleted());
            } else {
                assertFalse(PIQuest7.isCompleted());
            }
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.PLANT_CROP.name(),
                    "Aloe Vera");
        }
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertTrue(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertFalse(PIQuest6.isCompleted());
        assertTrue(PIQuest7.isCompleted());
        PIQuest4.resetState();
        PIQuest7.resetState();
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertFalse(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertFalse(PIQuest6.isCompleted());
        assertFalse(PIQuest7.isCompleted());
        for (int i = 0; i < 10; i++) {
            assertFalse(PIQuest1.isCompleted());
            assertTrue(PIQuest2.isCompleted());
            assertFalse(PIQuest3.isCompleted());
            assertFalse(PIQuest4.isCompleted());
            assertTrue(PIQuest5.isCompleted());
            assertFalse(PIQuest6.isCompleted());
            if (i >= 3) {
                assertTrue(PIQuest7.isCompleted());
            } else {
                assertFalse(PIQuest7.isCompleted());
            }
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.PLANT_CROP.name(),
                    "Cosmic Cob");
        }
        assertTrue(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertTrue(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertFalse(PIQuest6.isCompleted());
        assertTrue(PIQuest7.isCompleted());
        PIQuest1.resetState();
        PIQuest4.resetState();
        PIQuest7.resetState();
        for (int i = 0; i < 10; i++) {
            assertFalse(PIQuest1.isCompleted());
            assertTrue(PIQuest2.isCompleted());
            assertFalse(PIQuest3.isCompleted());
            assertFalse(PIQuest4.isCompleted());
            assertTrue(PIQuest5.isCompleted());
            if (i >= 3) {
                assertTrue(PIQuest6.isCompleted());
            } else {
                assertFalse(PIQuest6.isCompleted());
            }
            assertFalse(PIQuest7.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.HARVEST_CROP.name(),
                    "Aloe Vera");
        }
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertFalse(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertTrue(PIQuest6.isCompleted());
        assertFalse(PIQuest7.isCompleted());
        PIQuest6.resetState();
        for (int i = 0; i < 10; i++) {
            assertFalse(PIQuest1.isCompleted());
            assertTrue(PIQuest2.isCompleted());
            assertFalse(PIQuest3.isCompleted());
            assertFalse(PIQuest4.isCompleted());
            assertTrue(PIQuest5.isCompleted());
            if (i >= 3) {
                assertTrue(PIQuest6.isCompleted());
            } else {
                assertFalse(PIQuest6.isCompleted());
            }
            assertFalse(PIQuest7.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.HARVEST_CROP.name(),
                    "Cosmic Cob");
        }
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertTrue(PIQuest3.isCompleted());
        assertFalse(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertTrue(PIQuest6.isCompleted());
        assertFalse(PIQuest7.isCompleted());
    }

    @Test
    public void testGetDescription() {
        testRegisterMission();
        String desc1 = "Manage the presence of hostile creatures on your farm.\n" +
                "Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
                "Deal with %d creatures of type Oxygen Eater.\n" +
                "%d out of %d hostiles dealt with.";
        String desc2 = "Manage the presence of hostile creatures on your farm.\n" +
                "Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
                "Deal with %d creatures of type Cow, Oxygen Eater.\n" +
                "%d out of %d hostiles dealt with.";
        String desc3 = "Manage the presence of hostile creatures on your farm.\n" +
                "Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
                "Deal with %d creatures of type Cow, Astrolotl, Oxygen Eater.\n" +
                "%d out of %d hostiles dealt with.";
        for (int i = 0; i < 10; i++) {
            int min7 = Math.min(i, 3);
            String formatted1 = String.format(desc1, 10, i, 10);
            String formatted2 = String.format(desc2, 0, 0, 0);
            String formatted3 = String.format(desc2, 10, i, 10);
            String formatted4 = String.format(desc3, 10, i, 10);
            String formatted5 = String.format(desc3, 0, 0, 0);
            String formatted6 = String.format(desc3, 10, i, 10);
            String formatted7 = String.format(desc3, 3, min7, 3);
            assertEquals(formatted1, PIQuest1.getDescription());
            assertEquals(formatted2, PIQuest2.getDescription());
            assertEquals(formatted3, PIQuest3.getDescription());
            assertEquals(formatted4, PIQuest4.getDescription());
            assertEquals(formatted5, PIQuest5.getDescription());
            assertEquals(formatted6, PIQuest6.getDescription());
            assertEquals(formatted7, PIQuest7.getDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(
                    MissionManager.MissionEvent.ANIMAL_DEFEATED.name(), EntityType.OxygenEater);
        }
    }

    @Test
    public void testGetShortDescription() {
        testRegisterMission();
        String desc = "%d out of %d hostiles dealt with";
        for (int i = 0; i < 10; i++) {
            int min7 = Math.min(i, 3);
            String formatted1 = String.format(desc, i, 10);
            String formatted2 = String.format(desc, 0, 0);
            String formatted3 = String.format(desc, i, 10);
            String formatted4 = String.format(desc, i, 10);
            String formatted5 = String.format(desc, 0, 0);
            String formatted6 = String.format(desc, i, 10);
            String formatted7 = String.format(desc, min7, 3);
            assertEquals(formatted1, PIQuest1.getShortDescription());
            assertEquals(formatted2, PIQuest2.getShortDescription());
            assertEquals(formatted3, PIQuest3.getShortDescription());
            assertEquals(formatted4, PIQuest4.getShortDescription());
            assertEquals(formatted5, PIQuest5.getShortDescription());
            assertEquals(formatted6, PIQuest6.getShortDescription());
            assertEquals(formatted7, PIQuest7.getShortDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(
                    MissionManager.MissionEvent.ANIMAL_DEFEATED.name(), EntityType.OxygenEater);
        }
        PIQuest1.resetState();
        PIQuest2.resetState();
        PIQuest3.resetState();
        PIQuest4.resetState();
        PIQuest5.resetState();
        PIQuest6.resetState();
        PIQuest7.resetState();
        for (int i = 0; i < 10; i++) {
            int min7 = Math.min(i, 3);
            String formatted1 = String.format(desc, 0, 10);
            String formatted2 = String.format(desc, 0, 0);
            String formatted3 = String.format(desc, i, 10);
            String formatted4 = String.format(desc, i, 10);
            String formatted5 = String.format(desc, 0, 0);
            String formatted6 = String.format(desc, i, 10);
            String formatted7 = String.format(desc, min7, 3);
            assertEquals(formatted1, PIQuest1.getShortDescription());
            assertEquals(formatted2, PIQuest2.getShortDescription());
            assertEquals(formatted3, PIQuest3.getShortDescription());
            assertEquals(formatted4, PIQuest4.getShortDescription());
            assertEquals(formatted5, PIQuest5.getShortDescription());
            assertEquals(formatted6, PIQuest6.getShortDescription());
            assertEquals(formatted7, PIQuest7.getShortDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(
                    MissionManager.MissionEvent.ANIMAL_DEFEATED.name(), EntityType.Cow);
        }PIQuest1.resetState();
        PIQuest2.resetState();
        PIQuest3.resetState();
        PIQuest4.resetState();
        PIQuest5.resetState();
        PIQuest6.resetState();
        PIQuest7.resetState();
        for (int i = 0; i < 10; i++) {
            int min7 = Math.min(i, 3);
            String formatted1 = String.format(desc, 0, 10);
            String formatted2 = String.format(desc, 0, 0);
            String formatted3 = String.format(desc, 0, 10);
            String formatted4 = String.format(desc, i, 10);
            String formatted5 = String.format(desc, 0, 0);
            String formatted6 = String.format(desc, i, 10);
            String formatted7 = String.format(desc, min7, 3);
            assertEquals(formatted1, PIQuest1.getShortDescription());
            assertEquals(formatted2, PIQuest2.getShortDescription());
            assertEquals(formatted3, PIQuest3.getShortDescription());
            assertEquals(formatted4, PIQuest4.getShortDescription());
            assertEquals(formatted5, PIQuest5.getShortDescription());
            assertEquals(formatted6, PIQuest6.getShortDescription());
            assertEquals(formatted7, PIQuest7.getShortDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(
                    MissionManager.MissionEvent.ANIMAL_DEFEATED.name(), EntityType.Astrolotl);
        }
    }

    @Test
    public void testReadProgress() {
        int progressInt = 3;
        JsonValue progress = new JsonValue(progressInt);
        String desc1 = "Manage the presence of hostile creatures on your farm.\n" +
                "Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
                "Deal with %d creatures of type Oxygen Eater.\n" +
                "%d out of %d hostiles dealt with.";
        String desc2 = "Manage the presence of hostile creatures on your farm.\n" +
                "Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
                "Deal with %d creatures of type Cow, Oxygen Eater.\n" +
                "%d out of %d hostiles dealt with.";
        String desc3 = "Manage the presence of hostile creatures on your farm.\n" +
                "Deal with hostile creatures by defeating them with weapons, or let your Space Snappers eat them.\n" +
                "Deal with %d creatures of type Cow, Astrolotl, Oxygen Eater.\n" +
                "%d out of %d hostiles dealt with.";
        String shortDesc = "%d out of %d hostiles dealt with";
        PIQuest1.readProgress(progress);
        PIQuest2.readProgress(progress);
        PIQuest3.readProgress(progress);
        PIQuest4.readProgress(progress);
        PIQuest5.readProgress(progress);
        PIQuest6.readProgress(progress);
        PIQuest7.readProgress(progress);
        String formatted1 = String.format(desc1, 10, progressInt, 10);
        String formatted2 = String.format(desc2, 0, progressInt, 0);
        String formatted3 = String.format(desc2, 10, progressInt, 10);
        String formatted4 = String.format(desc3, 10, progressInt, 10);
        String formatted5 = String.format(desc3, 0, progressInt, 0);
        String formatted6 = String.format(desc3, 10, progressInt, 10);
        String formatted7 = String.format(desc3, 3, progressInt, 3);
        String shortFormatted1 = String.format(shortDesc, progressInt, 10);
        String shortFormatted2 = String.format(shortDesc, progressInt, 0);
        String shortFormatted3 = String.format(shortDesc, progressInt, 10);
        String shortFormatted4 = String.format(shortDesc, progressInt, 10);
        String shortFormatted5 = String.format(shortDesc, progressInt, 0);
        String shortFormatted6 = String.format(shortDesc, progressInt, 10);
        String shortFormatted7 = String.format(shortDesc, progressInt, 3);
        assertEquals(formatted1, PIQuest1.getDescription());
        assertEquals(formatted2, PIQuest2.getDescription());
        assertEquals(formatted3, PIQuest3.getDescription());
        assertEquals(formatted4, PIQuest4.getDescription());
        assertEquals(formatted5, PIQuest5.getDescription());
        assertEquals(formatted6, PIQuest6.getDescription());
        assertEquals(formatted7, PIQuest7.getDescription());
        assertEquals(shortFormatted1, PIQuest1.getShortDescription());
        assertEquals(shortFormatted2, PIQuest2.getShortDescription());
        assertEquals(shortFormatted3, PIQuest3.getShortDescription());
        assertEquals(shortFormatted4, PIQuest4.getShortDescription());
        assertEquals(shortFormatted5, PIQuest5.getShortDescription());
        assertEquals(shortFormatted6, PIQuest6.getShortDescription());
        assertEquals(shortFormatted7, PIQuest7.getShortDescription());
    }

    @Test
    public void testGetProgress() {
        assertEquals(0, PIQuest1.getProgress());
        assertEquals(0, PIQuest2.getProgress());
        assertEquals(0, PIQuest3.getProgress());
        assertEquals(0, PIQuest4.getProgress());
        assertEquals(0, PIQuest5.getProgress());
        assertEquals(0, PIQuest6.getProgress());
        assertEquals(0, PIQuest7.getProgress());
        testIsCompleted();
        assertNotEquals(0, PIQuest1.getProgress());
        assertEquals(0, PIQuest2.getProgress());
        assertNotEquals(0, PIQuest3.getProgress());
        assertNotEquals(0, PIQuest4.getProgress());
        assertEquals(0, PIQuest5.getProgress());
        assertNotEquals(0, PIQuest6.getProgress());
        assertNotEquals(0, PIQuest7.getProgress());

        assertEquals(10, PIQuest1.getProgress());
        assertEquals(0, PIQuest2.getProgress());
        assertEquals(10, PIQuest3.getProgress());
        assertEquals(10, PIQuest4.getProgress());
        assertEquals(0, PIQuest5.getProgress());
        assertEquals(10, PIQuest6.getProgress());
        assertEquals(3, PIQuest7.getProgress());
    }

    @Test
    public void testResetState() {
        testIsCompleted();
        assertTrue(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertTrue(PIQuest3.isCompleted());
        assertTrue(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertTrue(PIQuest6.isCompleted());
        PIQuest1.resetState();
        PIQuest2.resetState();
        PIQuest3.resetState();
        PIQuest4.resetState();
        PIQuest5.resetState();
        PIQuest6.resetState();
        assertFalse(PIQuest1.isCompleted());
        assertTrue(PIQuest2.isCompleted());
        assertFalse(PIQuest3.isCompleted());
        assertFalse(PIQuest4.isCompleted());
        assertTrue(PIQuest5.isCompleted());
        assertFalse(PIQuest6.isCompleted());
    }
}