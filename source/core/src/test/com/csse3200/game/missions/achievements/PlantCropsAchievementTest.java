package com.csse3200.game.missions.achievements;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlantCropsAchievementTest {

    PlantCropsAchievement plantCropsAchievement;

    @BeforeEach
    public void init() {
        plantCropsAchievement = new PlantCropsAchievement("test", 1);
    }

    @Test
    public void testShortDescription() {
        String expected = "0 out of 1 crops planted";
        assertEquals(expected, plantCropsAchievement.getShortDescription());
    }

    @Test
    public void testGetProgress() {
        assertEquals(0, plantCropsAchievement.getProgress());
    }

    @Test
    public void testReadProgress() {
        JsonValue jsonValue = new JsonValue(3);
        plantCropsAchievement.readProgress(jsonValue);
        assertEquals(3, plantCropsAchievement.getProgress());
    }
}
