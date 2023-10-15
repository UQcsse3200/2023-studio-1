package com.csse3200.game.missions.quests;

import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.missions.rewards.Reward;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InventoryStateQuestTest {
    private InventoryStateQuest inventoryStateQuest;
    private InventoryComponent inventory;
    private SpaceGameArea spaceGameArea;
    private Entity player;

    @BeforeEach
    public void init() {
        inventory = mock(InventoryComponent.class);

        inventoryStateQuest = new InventoryStateQuest("Test", mock(Reward.class), Map.of("Item1", 3, "Item2", 2));

        spaceGameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(spaceGameArea);

        player = mock(Entity.class);
        when(spaceGameArea.getPlayer()).thenReturn(player);

        inventory = mock(InventoryComponent.class);
        when(player.getComponent(InventoryComponent.class)).thenReturn(inventory);
    }

    @AfterEach
    public void after() {
        ServiceLocator.clear();
    }

    @Test
    public void testInventoryIsMissing() {
        inventory = null;
        assertFalse(inventoryStateQuest.isCompleted());
    }

    @Test
    public void completeWhenItemsCollected() {
        when(inventory.getItemCount("Item1")).thenReturn(3);
        when(inventory.getItemCount("Item2")).thenReturn(2);
        assertTrue(inventoryStateQuest.isCompleted());
    }

    @Test
    public void notCompleteWhenAllItemsNotCollected() {
        when(inventory.getItemCount("Item1")).thenReturn(2);
        when(inventory.getItemCount("Item2")).thenReturn(1);
        assertFalse(inventoryStateQuest.isCompleted());
    }



    @Test
    public void testGetShortDescription() {
        when(inventory.getItemCount("Item1")).thenReturn(3);
        when(inventory.getItemCount("Item2")).thenReturn(2);
        String expected = "5 out of 5 required items collected";

        assertEquals(expected, inventoryStateQuest.getShortDescription());
    }
}

