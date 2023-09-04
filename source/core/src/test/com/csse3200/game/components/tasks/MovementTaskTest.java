package com.csse3200.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;
import com.csse3200.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class MovementTaskTest {
  @BeforeEach
  void beforeEach() {
    RenderService renderService = new RenderService();
    renderService.setDebug(mock(DebugRenderer.class));
    ServiceLocator.registerRenderService(renderService);
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerPhysicsService(new PhysicsService());
    when(gameTime.getTime()).thenReturn(0L);
  }

  @Test
  void shouldMoveOnStart() {
    Vector2 target = new Vector2(10f, 10f);
    MovementTask task = new MovementTask(target);
    Entity entity = new Entity(type).addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.create();

    task.create(() -> entity);
    task.start();
    assertTrue(movementComponent.getMoving());
    assertEquals(target, movementComponent.getTarget());
    assertEquals(Status.ACTIVE, task.getStatus());
  }

  @Test
  void shouldStopWhenClose() {
    MovementTask task = new MovementTask(new Vector2(10f, 10f), 2f);
    Entity entity = new Entity(type).addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.setPosition(5f, 5f);
    entity.create();

    task.create(() -> entity);
    task.start();
    task.update();
    assertTrue(movementComponent.getMoving());
    assertEquals(Status.ACTIVE, task.getStatus());

    entity.setPosition(10f, 9f);
    task.update();
    assertFalse(movementComponent.getMoving());
    assertEquals(Status.FINISHED, task.getStatus());
  }

  @Test
  public void testSetSpeedChangesSpeed() {
    int framesElapsed1 = 0;
    int framesElapsed2 = 0;

    MovementTask task = new MovementTask(new Vector2(1f, 1f));
    Entity entity = new Entity(type).addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.create();
    entity.setPosition(0f, 0f);

    task.create(() -> entity);
    task.start();

    while (task.getStatus() == Status.ACTIVE) {
      task.update();
      entity.earlyUpdate();
      entity.update();
      ServiceLocator.getPhysicsService().getPhysics().update();

      framesElapsed1 += 1;
      System.out.println(entity.getPosition());
    }
    entity.setPosition(0f, 0f);

    MovementTask fasterTask = new MovementTask(new Vector2(1f, 1f), new Vector2(3f, 3f));
    fasterTask.create(() -> entity);
    fasterTask.start();

    while (fasterTask.getStatus() == Status.ACTIVE) {
      fasterTask.update();
      entity.earlyUpdate();
      entity.update();
      ServiceLocator.getPhysicsService().getPhysics().update();

      framesElapsed2 += 1;
    }

    // Verify that setMaxSpeed is called with the expected argument
    assertTrue(framesElapsed1 > framesElapsed2);
  }

  @Test
  void shouldTriggerChangeDirection() {
    // create movement task to right of entity
    MovementTask task = new MovementTask(new Vector2(10f, 10f));
    Entity entity = new Entity(type).addComponent(new PhysicsMovementComponent());

    entity.setPosition(5f, 5f);
    entity.create();

    // Register callbacks
    EventListener1<String> callback = (EventListener1<String>)mock(EventListener1.class);
    entity.getEvents().addListener("directionChange", callback);

    task.create(() -> entity);
    task.start();

    verify(callback).handle(DirectionUtils.RIGHT);

    // set target to left of entity
    task.setTarget(new Vector2(0f, 0f));
    task.update();

    verify(callback).handle(DirectionUtils.LEFT);
  }

  @Test
  void shouldStop() {
    MovementTask task = new MovementTask(new Vector2(10f, 10f), new Vector2(2f, 2f));
    Entity entity = new Entity(type)
            .addComponent(new PhysicsComponent());

    PhysicsMovementComponent movementComponent = spy(new PhysicsMovementComponent());
    entity.addComponent(movementComponent);
    entity.create();

    entity.setPosition(5f, 5f);

    task.create(() -> entity);
    task.start();
    task.update();
    task.stop();

    // Verify that setMaxSpeed is called with the expected argument
    verify(movementComponent).setMaxSpeed(eq(Vector2Utils.ONE));
    assertFalse(movementComponent.getMoving());
  }

  @Test
  void checkEntityStuck() {
    // set speed to 0 to ensure entity is stuck
    MovementTask task = new MovementTask(new Vector2(1f, 1f), new Vector2(0f,0f));
    Entity entity = new Entity(type).addComponent(new PhysicsComponent());
    PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
    entity.addComponent(movementComponent);
    entity.create();
    entity.setPosition(0f, 0f);

    task.create(() -> entity);
    task.start();
    task.update();
    assertEquals(Status.ACTIVE, task.getStatus());

    // simulate time passing
    when(ServiceLocator.getTimeSource().getTimeSince(anyLong())).thenReturn(501L);
    task.update();
    assertEquals(Status.FAILED, task.getStatus());
  }
}

