package com.csse3200.game.ai.tasks;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.ai.tasks.Task.Status;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class TimedTaskTest {
    private Entity entity;
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(1f); // one second passes each frame
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        entity = new Entity();
    }

    @Test
    void checkPriorityTriggeredOnEvent() {
        TimedTask task = new TimedTask("trigger", 1f, 1);
        task.create(() -> entity);
        task.update();

        assertEquals(-1, task.getPriority()); // inactive priority
        entity.getEvents().trigger("trigger");
        assertEquals(1, task.getPriority());
    }

    @Test
    public void testTaskPriorityResetsAfterDuration() {
        // Arrange
        TimedTask task = new TimedTask("trigger", 5.0f, 10);

        // Act
        task.create(() -> entity);
        task.triggerActivePriority();
        task.start(); // assume aiTaskComponent starts task, component already tested
        for (int i = 0; i < 6; i++) {
            task.update();
        }

        // priority set back to inactive priority
        assertEquals(-1, task.getPriority());
    }

    @Test
    public void testTaskStops() {
        TimedTask task = new TimedTask("trigger", 2.0f, 10);

        task.create(() -> entity);
        task.triggerActivePriority();
        task.start();
        task.update();

        task.stop();
        assertEquals(task.getPriority(), -1);
        assertEquals(task.getStatus(), Status.INACTIVE);
    }

    @Test
    public void testTaskGetsOverruledByHigherPriorityTask() {
        TimedTask lowPriorityTask = new TimedTask("low", 10f, 1);
        TimedTask highPriorityTask = new TimedTask("high", 10f, 2);

        entity.addComponent(new AITaskComponent().addTask(lowPriorityTask).addTask(highPriorityTask));
        entity.create();

        entity.getEvents().trigger("low");
        entity.update();
        assertEquals(Status.ACTIVE, lowPriorityTask.getStatus());

        entity.getEvents().trigger("high");
        entity.update();
        assertEquals(Status.INACTIVE, lowPriorityTask.getStatus());
        assertEquals(Status.ACTIVE, highPriorityTask.getStatus());

        entity.getEvents().trigger("low");
        assertEquals(Status.INACTIVE, lowPriorityTask.getStatus());
        assertEquals(Status.ACTIVE, highPriorityTask.getStatus());
    }
}
