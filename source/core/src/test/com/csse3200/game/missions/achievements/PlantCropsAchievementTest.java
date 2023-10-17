package com.csse3200.game.missions.achievements;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PlantCropsAchievementTest {

    PlantCropsAchievement plantCropsAchievement;
    EventHandler eventHandler;

    @BeforeEach
    void init() {
        plantCropsAchievement = new PlantCropsAchievement("test", 1);

        MissionManager missionManager = mock(MissionManager.class);
        ServiceLocator.registerMissionManager(missionManager);
        eventHandler = mock(EventHandler.class);
        when(missionManager.getEvents()).thenReturn(eventHandler);
    }

    @AfterEach
    void after() {
        ServiceLocator.clear();
    }

    @Test
    void testShortDescription() {
        String expected = "0 out of 1 crops planted";
        assertEquals(expected, plantCropsAchievement.getShortDescription());
    }

    @Test
    void testGetProgress() {
        assertEquals(0, plantCropsAchievement.getProgress());
    }

    @Test
    void testReadProgress() {
        JsonValue jsonValue = new JsonValue(3);
        plantCropsAchievement.readProgress(jsonValue);
        assertEquals(3, plantCropsAchievement.getProgress());
    }

    @Test
    void testIsComplete() {
        assertFalse(plantCropsAchievement.isCompleted());
        plantCropsAchievement.updateState("");
        assertTrue(plantCropsAchievement.isCompleted());
    }

    @Test
    void testGetDescription() {
        String expected = "Become a test, plant 1 crops on crop tiles!\n"
                + "0 out of 1 crops planted!";
        assertEquals(expected, plantCropsAchievement.getDescription());
    }

    @Test
    void testRegisterMission() {
        plantCropsAchievement.registerMission(eventHandler);
        verify(eventHandler).addListener(eq("PLANT_CROP"), any(EventListener1.class));
    }
}
