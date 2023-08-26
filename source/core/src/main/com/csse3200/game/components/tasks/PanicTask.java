package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.ai.tasks.TimedTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * A panic task that causes the owner's entity to move randomly within a specified range for a certain duration.
 * The task is triggered by an event and has a priority that determines its execution order.
 */
public class PanicTask extends TimedTask implements PriorityTask {
    private final Vector2 panicRange;
    private final Vector2 panicSpeed;
    private MovementTask movementTask;
    private Vector2 startPos;

    /**
     * Constructs a PanicTask with the specified parameters.
     *
     * @param trigger    The event trigger that activates this task.
     * @param duration   The duration for which the panic behavior will occur.
     * @param priority   The priority of the panic task.
     * @param panicRange The range within which the entity will move randomly.
     * @param panicSpeed The speed at which the entity will move during panic.
     */
    public PanicTask(String trigger, float duration, int priority, Vector2 panicRange, Vector2 panicSpeed) {
        super(trigger, duration, priority);
        this.panicSpeed = panicSpeed;
        this.panicRange = panicRange;
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        movementTask = new MovementTask(getRandomPosInRange(), panicSpeed);

        movementTask.create(owner);
        movementTask.start();

        this.owner.getEntity().getEvents().trigger("runStart");
    }

    @Override
    public void update() {
        super.update();

        // Once movement finishes, start again
        if (!this.owner.getEntity().getComponent(PhysicsMovementComponent.class).getMoving()) {
            movementTask.setTarget(getRandomPosInRange());
            movementTask.start();
        }

        movementTask.update();
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

    /**
     * Generates a random position within the specified panic range, centered around the starting position.
     *
     * @return A random position within the panic range.
     */
    private Vector2 getRandomPosInRange() {
        Vector2 halfRange = panicRange.cpy().scl(0.5f);
        Vector2 min = startPos.cpy().sub(halfRange);
        Vector2 max = startPos.cpy().add(halfRange);
        return RandomUtils.random(min, max);
    }
}
