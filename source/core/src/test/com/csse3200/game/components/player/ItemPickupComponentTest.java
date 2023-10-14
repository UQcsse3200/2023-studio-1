package com.csse3200.game.components.player;

import java.util.ArrayList;

import com.csse3200.game.entities.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class ItemPickupComponentTest {

    private Entity picker;
    private Entity pickupItem;

    public class TestGameArea extends GameArea {
        @Override
        public void create() {
            // Don't do anything because I love this game engine :)
        }

        /**
         * @return
         */
        @Override
        public Entity getPlayer() {
            return null;
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

    String[] texturePaths = {"images/tool_shovel.png"};

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextures(texturePaths);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerGameArea(new TestGameArea());
        ServiceLocator.getResourceService().loadAll();
        /* Create two test entities (one that picks up the other) */
        picker = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new InventoryComponent(new ArrayList<>()))
                .addComponent(new ItemPickupComponent());
        pickupItem = new Entity(EntityType.ITEM)
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent("Shovel", ItemType.SHOVEL, "images/tool_shovel.png"));

        picker.create();
        pickupItem.create();
    }

    @Test
    void shouldCreateComponent() {
        assertNotEquals(null, picker.getComponent(ItemPickupComponent.class));
    }

    @Test
    void shouldPickupItem() {
        // Try add the item to inventory
        picker.getEvents().trigger("collisionStart", picker.getComponent(HitboxComponent.class).getFixture(),
                pickupItem.getComponent(HitboxComponent.class).getFixture());
        assertTrue(picker.getComponent(InventoryComponent.class).hasItem(pickupItem));
    }

    @Test
    void shouldNotPickupItem() {
        // Try add the item to inventory
        picker.getEvents().trigger("collisionStart", pickupItem.getComponent(HitboxComponent.class).getFixture(),
                picker.getComponent(HitboxComponent.class).getFixture());
        assertFalse(picker.getComponent(InventoryComponent.class).hasItem(pickupItem));

        pickupItem.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.OBSTACLE);
        picker.getEvents().trigger("collisionStart", picker.getComponent(HitboxComponent.class).getFixture(),
                picker.getComponent(HitboxComponent.class).getFixture());
        assertFalse(picker.getComponent(InventoryComponent.class).hasItem(pickupItem));
    }

    @Test
    void shouldNotPickupNotItem() {
        Entity notItem = new Entity().addComponent(new HitboxComponent());
        // Try add the item to inventory
        picker.getEvents().trigger("collisionStart", notItem.getComponent(HitboxComponent.class).getFixture(),
                picker.getComponent(HitboxComponent.class).getFixture());
        assertFalse(picker.getComponent(InventoryComponent.class).hasItem(notItem));

        notItem.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.OBSTACLE);
        picker.getEvents().trigger("collisionStart", picker.getComponent(HitboxComponent.class).getFixture(),
                picker.getComponent(HitboxComponent.class).getFixture());
        assertFalse(picker.getComponent(InventoryComponent.class).hasItem(notItem));
    }

    @Test
    void inventoryFull() {
        InventoryComponent inv = picker.getComponent(InventoryComponent.class);
        for (int i = 0; i < 30; i++) {
            inv.addItem(new Entity(EntityType.ITEM).addComponent(new ItemComponent(String.valueOf(i), ItemType.EGG, "images/tool_shovel.png")));
        }
        assertTrue(inv.isFull());
        assertFalse(inv.addItem(new Entity(EntityType.ITEM).addComponent(new ItemComponent("full", ItemType.EGG, "images/tool_shovel.png"))));
    }
}