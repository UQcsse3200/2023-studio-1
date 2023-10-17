package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.inventory.InventoryDisplayManager;
import com.csse3200.game.components.placeables.FenceComponent;
import com.csse3200.game.components.placeables.PlaceableEvents;
import com.csse3200.game.components.placeables.SprinklerComponent;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class PlaceableFactoryTest {

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerInventoryDisplayManager(new InventoryDisplayManager(null));

        /* Mock our ResourceService for the textures */


        ResourceService mockResourceService = mock(ResourceService.class);
        when(mockResourceService.getAsset(anyString(), any())).thenReturn(null);
        TextureAtlas mockTextureAtlas = mock(TextureAtlas.class);
        when(mockTextureAtlas.findRegion(anyString())).thenReturn(null);
        when(mockResourceService.getAsset(anyString(), eq(TextureAtlas.class))).thenReturn(mockTextureAtlas);
        ServiceLocator.registerResourceService(mockResourceService);
       
    }

    void baseComponentsAssertion(Entity e) {
        assertNotNull(e.getComponent(PlaceableEvents.class));
        assertNotNull(e.getComponent(PhysicsComponent.class));
        assertNotNull(e.getComponent(HitboxComponent.class));
        assertNotNull(e.getComponent(ColliderComponent.class));
    }

    @Test
    void shouldCreateBase() {
        Entity e = PlaceableFactory.createBasePlaceable(EntityType.TRACTOR);
        this.baseComponentsAssertion(e);
    }

    @Test
    void shouldCreateFence() {
        Entity e = PlaceableFactory.createFence();

	    assertSame(EntityType.FENCE, e.getType());
        assertNotNull(e.getComponent(FenceComponent.class));
        this.baseComponentsAssertion(e);
    }

    @Test
    void shouldCreateGate() {
        Entity e = PlaceableFactory.createGate();

	    assertSame(EntityType.GATE, e.getType());
        assertNotNull(e.getComponent(FenceComponent.class));
        this.baseComponentsAssertion(e);
    }

    @Test
    void shouldCreateSprinkler() {
        Entity e = PlaceableFactory.createSprinkler();

	    assertSame(EntityType.SPRINKLER, e.getType());
        assertNotNull(e.getComponent(SprinklerComponent.class));
        this.baseComponentsAssertion(e);
    }

    @Test
    void shouldCreatePump() {
        Entity e = PlaceableFactory.createPump();

	    assertSame(EntityType.PUMP, e.getType());
        assertNotNull(e.getComponent(SprinklerComponent.class));
        this.baseComponentsAssertion(e);
    }
    
} 
