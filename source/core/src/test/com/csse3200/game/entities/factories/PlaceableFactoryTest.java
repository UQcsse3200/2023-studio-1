package com.csse3200.game.entities.factories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.csse3200.game.components.AuraLightComponent;
import com.csse3200.game.components.inventory.InventoryDisplay;
import com.csse3200.game.components.inventory.InventoryDisplayManager;
import com.csse3200.game.components.placeables.ChestComponent;
import com.csse3200.game.components.placeables.FenceComponent;
import com.csse3200.game.components.placeables.LightController;
import com.csse3200.game.components.placeables.PlaceableEvents;
import com.csse3200.game.components.placeables.SprinklerComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
public class PlaceableFactoryTest {

    @BeforeEach
    public void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerInventoryDisplayManager(new InventoryDisplayManager(null));

        /* Mock our ResourceService for the textures */


        ResourceService mockResourceService = mock(ResourceService.class);
        when(mockResourceService.getAsset(anyString(), any())).thenReturn(null);
        ServiceLocator.registerResourceService(mockResourceService);
       
    }

    void baseComponentsAssertion(Entity e) {
        assertNotNull(e.getComponent(PlaceableEvents.class));
        assertNotNull(e.getComponent(PhysicsComponent.class));
        assertNotNull(e.getComponent(HitboxComponent.class));
        assertNotNull(e.getComponent(ColliderComponent.class));
    }

    @Test
    public void shouldCreateBase() {
        Entity e = PlaceableFactory.createBasePlaceable(EntityType.TRACTOR);
        this.baseComponentsAssertion(e);
    }

    @Test
    public void shouldCreateFence() {
        Entity e = PlaceableFactory.createFence();
        
        assertTrue(EntityType.FENCE == e.getType());
        assertNotNull(e.getComponent(FenceComponent.class));
        this.baseComponentsAssertion(e);
    }

    @Test
    public void shouldCreateGate() {
        Entity e = PlaceableFactory.createGate();
        
        assertTrue(EntityType.GATE == e.getType());
        assertNotNull(e.getComponent(FenceComponent.class));
        this.baseComponentsAssertion(e);
    }

    @Test
    public void shouldCreateSprinkler() {
        Entity e = PlaceableFactory.createSprinkler();
        
        assertTrue(EntityType.SPRINKLER == e.getType());
        assertNotNull(e.getComponent(SprinklerComponent.class));
        this.baseComponentsAssertion(e);
    }

    @Test
    public void shouldCreatePump() {
        Entity e = PlaceableFactory.createPump();
        
        assertTrue(EntityType.PUMP == e.getType());
        assertNotNull(e.getComponent(SprinklerComponent.class));
        this.baseComponentsAssertion(e);
    }
    
} 
