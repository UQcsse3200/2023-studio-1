package com.csse3200.game.missions.quests;

import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EventListener;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InventoryStateQuestTest {
    private InventoryStateQuest inventoryStateQuest1, inventoryStateQuest2, inventoryStateQuest3;
    private InventoryComponent inventory;
    private SpaceGameArea spaceGameArea;
    private Entity player;
    private EventHandler eventHandler;

    @BeforeEach
    public void init() {
        inventory = mock(InventoryComponent.class);

        inventoryStateQuest1 = new InventoryStateQuest("Test", mock(Reward.class), Map.of("Item1", 3, "Item2", 2));

        Reward reward = mock(Reward.class);
        when(reward.isCollected()).thenReturn(true);
        inventoryStateQuest2 = new InventoryStateQuest("Test", reward, Map.of("Item3", 2));

        inventoryStateQuest3 = new InventoryStateQuest("Test", mock(Reward.class), 10, Map.of("Item3", 2));

        spaceGameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(spaceGameArea);

        player = mock(Entity.class);
        when(spaceGameArea.getPlayer()).thenReturn(player);

        inventory = mock(InventoryComponent.class);
        when(player.getComponent(InventoryComponent.class)).thenReturn(inventory);

        eventHandler = mock(EventHandler.class);
        MissionManager missionManager = mock(MissionManager.class);
        ServiceLocator.registerMissionManager(missionManager);
        when(missionManager.getEvents()).thenReturn(eventHandler);
    }

    @AfterEach
    public void after() {
        ServiceLocator.clear();
    }

    @Test
    public void testInventoryIsMissing() {
        inventory = null;
        assertFalse(inventoryStateQuest1.isCompleted());
        // initially missing then gets inventory
        assertFalse(inventoryStateQuest1.checkPlayerInventoryMissing());
    }

    @Test
    public void testInventoryNotMissing() {
        assertFalse(inventoryStateQuest1.checkPlayerInventoryMissing());
    }

    @Test
    public void completeWhenItemsCollected() {
        when(inventory.getItemCount("Item1")).thenReturn(3);
        when(inventory.getItemCount("Item2")).thenReturn(2);
        assertTrue(inventoryStateQuest1.isCompleted());
    }

    @Test
    public void notCompleteWhenAllItemsNotCollected() {
        when(inventory.getItemCount("Item1")).thenReturn(2);
        when(inventory.getItemCount("Item2")).thenReturn(1);
        assertFalse(inventoryStateQuest1.isCompleted());
    }



    @Test
    public void testGetShortDescription() {
        when(inventory.getItemCount("Item1")).thenReturn(3);
        when(inventory.getItemCount("Item2")).thenReturn(2);
        String expected = "5 out of 5 required items collected";

        assertEquals(expected, inventoryStateQuest1.getShortDescription());

        when(player.getComponent(InventoryComponent.class)).thenReturn(null);
        String expected2 = "Error, player inventory not detected!";
        assertEquals(expected2, inventoryStateQuest2.getShortDescription());
    }

    @Test
    public void testRegisterMission() {
        TimeService timeService = mock(TimeService.class);
        ServiceLocator.registerTimeService(timeService);
        when(ServiceLocator.getTimeService().getEvents()).thenReturn(eventHandler);

        inventoryStateQuest1.registerMission(eventHandler);
        verify(eventHandler).addListener(eq("minuteUpdate"), any(EventListener0.class));
    }

    @Test
    public void testIsComplete() {
        assertTrue(inventoryStateQuest2.isCompleted());

        when(player.getComponent(InventoryComponent.class)).thenReturn(null);
        assertFalse(inventoryStateQuest3.isCompleted());
    }

    @Test
    public void testGetDescription() {
        when(inventory.getItemCount("Item3")).thenReturn(2);
        String expected = "Gather items of all shapes and sizes to improve your efficiency!\n" +
                "Gather the following items:\n" +
                "    2 items of type: Item3 (2 collected).\n";
        assertEquals(expected, inventoryStateQuest2.getDescription());

        when(player.getComponent(InventoryComponent.class)).thenReturn(null);
        String expected2 = "Error, player inventory not detected!";
        assertEquals(expected2, inventoryStateQuest3.getDescription());
    }

    @Test
    public void testProgress() {
        assertEquals(0, inventoryStateQuest1.getProgress());
    }

    @Test
    public void testEmptyMethods() {
        boolean isCompleted = inventoryStateQuest2.isCompleted();
        inventoryStateQuest2.readProgress(new JsonValue(0));
        inventoryStateQuest2.resetState();
        // should be no state change after funciton calls
        assertEquals(isCompleted, inventoryStateQuest2.isCompleted());
    }
}

