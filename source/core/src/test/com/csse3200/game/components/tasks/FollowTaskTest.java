package com.csse3200.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class FollowTaskTest {
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldMoveTowardsTarget() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        AITaskComponent ai = new AITaskComponent().addTask(new FollowTask(target, 10, 5, 10,1.5f,
                Vector2Utils.ONE));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance < initialDistance);
    }

    @Test
    void shouldStopAtStoppingDistance() {
        Entity target = new Entity();
        target.setPosition(0, 1f);
        float stoppingDistance = 0.5f;
        AITaskComponent ai = new AITaskComponent().addTask(new FollowTask(target, 10, 10, 10,
                stoppingDistance, Vector2Utils.ONE));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 1.6f);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 10; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());

        //Entity should stop within 0.01 from stopping distance
        assertTrue(Math.abs(stoppingDistance - newDistance) < 0.2);
    }

    @Test
    void shouldFollowOnlyWhenInDistance() {
        Entity target = new Entity();
        target.setPosition(0f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);

        FollowTask followTask = new FollowTask(target, 10, 5, 10, 1.5f, Vector2Utils.ONE);
        followTask.create(() -> entity);

        // Not currently active, target is too far, should have negative priority
        assertTrue(followTask.getPriority() < 0);

        // When in view distance, should give higher priority
        target.setPosition(0f, 4f);
        assertEquals(10, followTask.getPriority());

        // When active, should follow if within follow distance
        target.setPosition(0f, 8f);
        followTask.start();
        assertEquals(10, followTask.getPriority());

        // When active, should not follow outside follow distance
        target.setPosition(0f, 12f);
        assertTrue(followTask.getPriority() < 0);
    }


    @Test
    void shouldNotFollowInStoppingDistance() {
        Entity target = new Entity();
        target.setPosition(0f, 6f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 5f);

        FollowTask followTask = new FollowTask(target, 10, 5, 10, 2f, Vector2Utils.ONE);
        followTask.create(() -> entity);

        // Not currently active, target is within stopping distance, should have negative priority
        assertTrue(followTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}
