package com.csse3200.game.missions.achievements;

import com.badlogic.gdx.utils.JsonValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;

public class CollectItemsAchievementTest {

    private CollectItemsAchievement collectItemsAchievement;

    @BeforeEach
    public void init() {
        collectItemsAchievement = new CollectItemsAchievement("test", 5);
    }

    @Test
    public void testReadProgress() {
        JsonValue jsonValue = new JsonValue(4);
        collectItemsAchievement.readProgress(jsonValue);
        assertEquals(4, collectItemsAchievement.getProgress());
    }

    @Test
    public void testGetProgress() {
        assertEquals(0, collectItemsAchievement.getProgress());
        collectItemsAchievement.updateState();
        assertEquals(1, collectItemsAchievement.getProgress());
        collectItemsAchievement.updateState();
        assertEquals(2, collectItemsAchievement.getProgress());
    }

    @Test
    public void testResetState() {
        collectItemsAchievement.updateState();
        collectItemsAchievement.updateState();
        assertEquals(2, collectItemsAchievement.getProgress());
        collectItemsAchievement.resetState();
        assertEquals(0, collectItemsAchievement.getProgress());
    }
}
