package com.csse3200.game.missions.rewards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import com.csse3200.game.entities.EntityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

class ItemRewardTest {

    private Entity e1, e2, e3, e4;
    private ArrayList<Entity> items1, items2, items3;

    private ItemReward r1, r2, r3;

    public class TestGameArea extends GameArea {
        Entity player = new Entity()
                .addComponent(new InventoryComponent(new ArrayList<Entity>()));

        @Override
        public void create() {
            // do nothing
        }

        @Override
        public Entity getPlayer() {
            return player;
        }

        @Override
        public ClimateController getClimateController() {
            return null;
        }

        @Override
        public Entity getTractor() {
            return null;
        }

        @Override
        public GameMap getMap() {
            return null;
        }
    }


    @BeforeEach
    void beforeTest() {
        ServiceLocator.registerGameArea(new TestGameArea());
        ServiceLocator.registerEntityService(new EntityService());

        // empty item list
        items1 = new ArrayList<Entity>();
        r1 = new ItemReward(items1);

        // single item list
        items2 = new ArrayList<Entity>();
        e1 = new Entity();
        items2.add(e1);
        r2 = new ItemReward(items2);

        // many item list
        items3 = new ArrayList<Entity>();
        e2 = new Entity();
        e3 = new Entity();
        e4 = new Entity();
        items3.add(e2);
        items3.add(e3);
        items3.add(e4);
        r3 = new ItemReward(items3);
    }

    @AfterEach
    void afterTest() {
        ServiceLocator.clear();
    }

    @Test
    void testEmptyCollect() {
        InventoryComponent initialPlayerInventory = ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class);
        assertFalse(r1.isCollected());
        r1.collect();
        assertEquals(initialPlayerInventory, ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class));
        assertTrue(r1.isCollected());
    }

    @Test
    void testSingleCollect() {
        InventoryComponent initialPlayerInventory = ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class);
        r2.collect();
        initialPlayerInventory.addItem(e1);
        assertEquals(initialPlayerInventory, ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class));
        assertTrue(r2.isCollected());

    }

    @Test
    void testManyCollect() {
        InventoryComponent initialPlayerInventory = ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class);
        r3.collect();
        initialPlayerInventory.addItem(e2);
        initialPlayerInventory.addItem(e3);
        initialPlayerInventory.addItem((e4));
        assertEquals(initialPlayerInventory, ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class));
        assertTrue(r3.isCollected());
    }
}