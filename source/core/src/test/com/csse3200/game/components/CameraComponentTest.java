package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(GameExtension.class)
public class CameraComponentTest {
    Entity camEntity;
    CameraComponent cameraComponent;
    Camera camera;
    Entity entity;
    Entity entityWithPhysics;
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());

        camEntity = new Entity().addComponent(new CameraComponent());
        cameraComponent = camEntity.getComponent(CameraComponent.class);
        camera = cameraComponent.getCamera();

        entity = new Entity();
        entity.setScale(1f, 1f); // default scale, added for clarity
        entityWithPhysics = new Entity().addComponent(new PhysicsComponent());
        entity.setScale(1f, 1f);
    }

    @Test
    void shouldSetTrackEntity() {
        assertNull(cameraComponent.getTrackEntity());

        cameraComponent.setTrackEntity(entity);
        assertEquals(cameraComponent.getTrackEntity(), entity);
    }
    @Test
    void shouldFollowEntity() {
        Vector3 cameraPos;

        // test current position is default position
        cameraComponent.update();
        cameraPos = camera.position;
        assertEquals(0f, cameraPos.x);
        assertEquals(0f, cameraPos.y);

        // test camera position updates when new entity is set
        entity.setPosition(new Vector2(1f, 1f));
        cameraComponent.setTrackEntity(entity);
        cameraComponent.update();
        cameraPos = camera.position;
        assertEquals(1.5f, cameraPos.x); // setPosition moves bottom left of entity, so add scale
        assertEquals(1.5f, cameraPos.y);

        // test camera position update when entity is moved
        entity.setPosition(1.5f, 1.5f);
        cameraComponent.update();
        cameraPos = camera.position;
        assertEquals(2f, cameraPos.x);
        assertEquals(2f, cameraPos.y);
    }

    @Test
    void shouldFollowEntityWithPhysics() {
        Vector3 cameraPos;
        Body body = entityWithPhysics.getComponent(PhysicsComponent.class).getBody();

        // test camera position updates when new entity is set
        body.setTransform(new Vector2(5f, 5f), 0f);
        cameraComponent.setTrackEntity(entityWithPhysics);
        cameraComponent.update();
        cameraPos = camera.position;
        assertEquals(5f, cameraPos.x); // setTransform moves center, compare center
        assertEquals(5f, cameraPos.y);

        // test camera position update when entity is moved
        body.setTransform(new Vector2(7f, 3f), 0f);
        cameraComponent.update();
        cameraPos = camera.position;
        assertEquals(7f, cameraPos.x);
        assertEquals(3f, cameraPos.y);
    }

    @Test
    void shouldFollowWhenEntityChanges() {
        Vector3 cameraPos;
        Body body = entityWithPhysics.getComponent(PhysicsComponent.class).getBody();

        // test camera position updates when new entity is set
        entity.setPosition(new Vector2(1f, 1f));
        cameraComponent.setTrackEntity(entity);
        cameraComponent.update();
        cameraPos = camera.position;
        assertEquals(1.5f, cameraPos.x); // setPosition moves bottom left of entity, so add scale
        assertEquals(1.5f, cameraPos.y);

        // test camera position updates when new entity is set
        body.setTransform(new Vector2(-1f, -1f), 0f);
        cameraComponent.setTrackEntity(entityWithPhysics);
        cameraComponent.update();
        cameraPos = camera.position;
        assertEquals(-1f, cameraPos.x); // setTransform moves center, compare center
        assertEquals(-1f, cameraPos.y);
    }
}
