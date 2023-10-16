package com.csse3200.game.missions.quests;

import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FishingQuestTest {

    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());
    }

    @Test
    void fishingQuests() {
        FishingQuest quest = new FishingQuest("Fishing Quest", null, 10);
        assert quest.getName().equals("Fishing Quest");
        assert quest.getDescription().equals("Catching fish is a great food source and you can get hidden treasures!.\nGet items from fishing 10 times.\n0 out of 10 items gotten.");
        assert quest.getShortDescription().equals("0 out of 10 items gotten");
        assert quest.getProgress().equals(0);
        quest.registerMission(ServiceLocator.getMissionManager().getEvents());
        for (int i = 0; i < 10; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FISH.name());
            assert quest.getProgress().equals(i + 1);
        }
        assert quest.isCompleted();
    }

    @Test
    void testResetState() {
        FishingQuest quest = new FishingQuest("Fishing Quest", null, 10);
        quest.registerMission(ServiceLocator.getMissionManager().getEvents());
        for (int i = 0; i < 10; i++) {
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.FISH.name());
            assert quest.getProgress().equals(i + 1);
        }
        assert quest.isCompleted();
        quest.resetState();
        assert quest.getProgress().equals(0);
        assert !quest.isCompleted();
    }
}
