package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TimedTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.utils.math.RandomUtils;

/**
 * A panic task that causes the owner's entity to move randomly within a specified range for a certain duration.
 * The task is triggered by an event and has a priority that determines its execution order.
 */
public class PanicTask extends TimedTask implements PriorityTask {
    /** Range that entity will randomly move during panic state. */
    private final Vector2 panicRange;
    /** Speed of movement during panic state. */
    private final Vector2 panicSpeed;
    /** Movement task for panic movement. */
    private MovementTask movementTask;
    /** Starting position when panic task starts. */
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

    /**
     * Starts the panic task by initializing the movement task and triggering the "runStart" event.
     */
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
    public void triggerActivePriority() {
        super.triggerActivePriority();
        this.owner.getEntity().getEvents().trigger("startEffect", "panicStart");
    }

    /**
     * Updates the panic task by checking if movement has finished, and if so, restarting the movement task.
     */
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

    /**
     * Stops the panic task and the associated movement task.
     */
    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
        this.owner.getEntity().getEvents().trigger("stopEffect", "panicStart");
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
