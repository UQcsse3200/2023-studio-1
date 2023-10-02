package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ShipRepairQuestTest {
    private ShipRepairQuest SRQuest1, SRQuest2, SRQuest3, SRQuest4, SRQuest5;
    private Reward r1 = mock(Reward.class);

    @BeforeEach
    public void init() {
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerTimeService(new TimeService());
        ServiceLocator.registerMissionManager(new MissionManager());


        SRQuest1 = new ShipRepairQuest("Ship Repair Quest 1", r1, 10);
        SRQuest2 = new ShipRepairQuest("Ship Repair Quest 2", r1, -1);
        SRQuest3 = new ShipRepairQuest("Ship Repair Quest 3", r1, 5);
        SRQuest4 = new ShipRepairQuest("Ship Repair Quest 4", r1, 10, 10);
        SRQuest5 = new ShipRepairQuest("Ship Repair Quest 5", r1, 10, -1);

    }
    @AfterEach
    public void reset() {
        ServiceLocator.clear();
    }

    @Test
    public void testIsCompleted() {
        SRQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        for (int i = 0; i < 10; i++) {
            assertFalse(SRQuest1.isCompleted());
            assertTrue(SRQuest2.isCompleted());
            if (i >= 5) {
                assertTrue(SRQuest3.isCompleted());
            } else {
                assertFalse(SRQuest3.isCompleted());
            }
            assertFalse(SRQuest4.isCompleted());
            assertTrue(SRQuest5.isCompleted());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.SHIP_PART_ADDED.name());
        }
        assertTrue(SRQuest1.isCompleted());
        assertTrue(SRQuest2.isCompleted());
        assertTrue(SRQuest3.isCompleted());
        assertTrue(SRQuest4.isCompleted());
        assertTrue(SRQuest5.isCompleted());
        SRQuest1.resetState();
        SRQuest2.resetState();
        SRQuest3.resetState();
        SRQuest4.resetState();
        SRQuest5.resetState();
        assertFalse(SRQuest1.isCompleted());
        assertTrue(SRQuest2.isCompleted());
        assertFalse(SRQuest3.isCompleted());
        assertFalse(SRQuest4.isCompleted());
        assertTrue(SRQuest5.isCompleted());
    }

    @Test
    public void testGetDescription() {
        SRQuest1.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest2.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest3.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest4.registerMission(ServiceLocator.getMissionManager().getEvents());
        SRQuest5.registerMission(ServiceLocator.getMissionManager().getEvents());
        String desc = "Repair your ship and unlock useful features.\nAdd %d scavenged " +
                "parts from your ship's hull to your ship.\n%d out of %d additional ship parts added.";
        for (int i = 0; i < 10; i++) {
            assertEquals(String.format(desc, 10, i, 10), SRQuest1.getDescription());
            assertEquals(String.format(desc, 0, 0, 0), SRQuest2.getDescription());
            assertEquals(String.format(desc, 5, Math.min(i, 5), 5), SRQuest3.getDescription());
            assertEquals(String.format(desc, 10, i, 10), SRQuest4.getDescription());
            assertEquals(String.format(desc, 0, 0, 0), SRQuest5.getDescription());
            ServiceLocator.getMissionManager().getEvents().trigger(MissionManager.MissionEvent.SHIP_PART_ADDED.name());
        }
    }

    @Test
    public void testReadAndGetProgress() {
        assertEquals(0, SRQuest1.getProgress());
        assertEquals(0, SRQuest2.getProgress());
        assertEquals(0, SRQuest3.getProgress());
        assertEquals(0, SRQuest4.getProgress());
        assertEquals(0, SRQuest5.getProgress());
        int progressInt = 3;
        JsonValue jsonValue = new JsonValue(progressInt);
        SRQuest1.readProgress(jsonValue);
        SRQuest2.readProgress(jsonValue);
        SRQuest3.readProgress(jsonValue);
        SRQuest4.readProgress(jsonValue);
        SRQuest5.readProgress(jsonValue);
        String desc = "Repair your ship and unlock useful features.\nAdd %d scavenged " +
                "parts from your ship's hull to your ship.\n%d out of %d additional ship parts added.";
        assertEquals(String.format(desc, 10, progressInt, 10), SRQuest1.getDescription());
        assertEquals(String.format(desc, 0, progressInt, 0), SRQuest2.getDescription());
        assertEquals(String.format(desc, 5, progressInt, 5), SRQuest3.getDescription());
        assertEquals(String.format(desc, 10, progressInt, 10), SRQuest4.getDescription());
        assertEquals(String.format(desc, 0, progressInt, 0), SRQuest5.getDescription());
    }

}