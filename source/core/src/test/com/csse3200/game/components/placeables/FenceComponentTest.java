package com.csse3200.game.components.placeables;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DynamicTextureRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
public class FenceComponentTest {

    private Entity f1;
    private Entity g1;

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
            return new GameMap(new TerrainFactory(new CameraComponent()));
        }
    }

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        //ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerGameArea(new TestGameArea());

        DynamicTextureRenderComponent dtrc = mock(DynamicTextureRenderComponent.class);

        f1 = new Entity(EntityType.Fence)
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent())
                //.addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(dtrc)
                .addComponent(new FenceComponent(false));
        g1 = new Entity(EntityType.Gate)
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(dtrc)
                .addComponent(new FenceComponent(false));

        f1.create();
        g1.create();
    }

    @Test
    public void shouldCreateComponent() {

        assertNotEquals(null,f1.getComponent(FenceComponent.class));
        assertNotEquals(null, g1.getComponent(FenceComponent.class));
    }
}