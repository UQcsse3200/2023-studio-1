package com.csse3200.game.components.player;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class ItemPickupComponentTest {

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
    }

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerGameArea(new TestGameArea());
        /* Create two test entities (one that picks up the other) */
        picker = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new InventoryComponent(new ArrayList<Entity>()))
                .addComponent(new ItemPickupComponent());
        pickupItem = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ITEM))
                .addComponent(new ItemComponent("Shovel", ItemType.SHOVEL));

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
}