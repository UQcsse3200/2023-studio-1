package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FishingQuestTest {

    @BeforeEach
    void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
    }

    @Test
    void fishingQuests() {
        FishingQuest quest = new FishingQuest("Fishing Quest", null, 10);
	    assertEquals("Fishing Quest", quest.getName());
	    assertEquals("Catching fish is a great food source and you can get hidden treasures!.\nGet items from fishing 10 times.\n0 out of 10 items gotten.", quest.getDescription());
	    assertEquals("0 out of 10 items gotten", quest.getShortDescription());
	    assertEquals(0, quest.getProgress());
        quest.registerMission(ServiceLocator.getMissionManager().getEvents());
        for (int i = 0; i < 10; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FISH.name());
	        assertEquals(quest.getProgress(), i + 1);
        }
        assertTrue(quest.isCompleted());
    }

    @Test
    void testResetState() {
        FishingQuest quest = new FishingQuest("Fishing Quest", null, 10);
        quest.registerMission(ServiceLocator.getMissionManager().getEvents());
        for (int i = 0; i < 10; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FISH.name());
	        assertEquals(quest.getProgress(), i + 1);
        }
        assertTrue(quest.isCompleted());
        quest.resetState();
	    assertEquals(0, quest.getProgress());
	    assertFalse(quest.isCompleted());
    }
}
