package com.csse3200.game.missions.rewards;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.ItemPickupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ConsumePlayerItemsRewardTest {

    ConsumePlayerItemsReward consumePlayerItemsReward;

    InventoryComponent inventoryComponent;

    @BeforeEach
    public void init() {
        GameArea gameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(gameArea);

        MissionManager missionManager = mock(MissionManager.class);
        ServiceLocator.registerMissionManager(missionManager);

        Entity player = mock(Entity.class);


        when(player.getComponent(InventoryComponent.class)).thenReturn(inventoryComponent);

    }

    @AfterEach
    public void clearServiceLocator() {
        ServiceLocator.clear();
    }

    @Test
    public void singleItem() {
        GameArea gameArea = mock(SpaceGameArea.class);
        ServiceLocator.registerGameArea(gameArea);

        MissionManager missionManager = mock(MissionManager.class);
        ServiceLocator.registerMissionManager(missionManager);

        Entity player = mock(Entity.class);
        when(ServiceLocator.getGameArea().getPlayer()).thenReturn(player);

        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2);
        items.put("item2", 1);
        consumePlayerItemsReward = new ConsumePlayerItemsReward(items);
        inventoryComponent = mock(InventoryComponent.class);
        when(player.getComponent(InventoryComponent.class)).thenReturn(inventoryComponent);

        when(inventoryComponent.getItemCount("item1")).thenReturn(3);
        when(inventoryComponent.getItemCount("item2")).thenReturn(1);
        assertFalse(consumePlayerItemsReward.isCollected());
        consumePlayerItemsReward.collect();
        assertTrue(consumePlayerItemsReward.isCollected());
        verify(inventoryComponent, times(2)).removeItem("item1");
        verify(inventoryComponent, times(1)).removeItem("item2");
    }
}
