package com.csse3200.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
public class PanicTaskTest {
    private Entity entity;
    private PhysicsMovementComponent movementComponent;
    private PanicTask panicTask;
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(1f / 60f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        panicTask = new PanicTask("panicTrigger", 1f, 10, new Vector2(1f, 1f), new Vector2(2f, 2f));
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(panicTask);
        movementComponent = spy(new PhysicsMovementComponent());
        entity = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(aiTaskComponent)
                .addComponent(movementComponent);
    }

    @Test
    public void testStartMovesEntity() {
        entity.create();
        entity.setPosition(0f, 0f);
        EventListener0 callback = mock(EventListener0.class);
        entity.getEvents().addListener("runStart", callback);

        Vector2 position = entity.getPosition();
        entity.getEvents().trigger("panicTrigger");

        // Run the game for a few cycles
        for (int i = 0; i < 10; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            entity.earlyUpdate();
            entity.update();
        }

        // Check speed changed, position changed, eventTriggered
        verify(movementComponent, atLeast(1)).setMaxSpeed(new Vector2(2f, 2f));
        verify(callback).handle();
        assertNotEquals(position, entity.getPosition());
    }

    @Test
    public void testPanicTaskUpdate() {
        entity.create();
        entity.setPosition(0f, 0f);
        entity.getEvents().trigger("panicTrigger");

        // increase delta to decrease loop size
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(0.25f);
        for(int i = 0; i < 10; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            entity.earlyUpdate();
            entity.update();
        }

        // Verify that the PanicTask renews the movement task, and sets new physicsMovementDirection
        verify(movementComponent, atLeast(3)).setTarget(any(Vector2.class));
    }

    @Test
    public void testPanicFinishes() {
        entity.create();
        entity.setPosition(0f, 0f);
        entity.getEvents().trigger("panicTrigger");

        // increase delta to decrease loop size
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(0.25f);
        for(int i = 0; i < 10; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            entity.earlyUpdate();
            entity.update();
        }

        assertEquals(Status.INACTIVE, panicTask.getStatus());
    }
}
