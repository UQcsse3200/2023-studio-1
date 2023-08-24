package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.ai.tasks.TimedTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.utils.math.Vector2Utils;

public class PanicTask extends TimedTask implements PriorityTask {
    private final Vector2 panicRange;
    private final Vector2 panicSpeed;
    private MovementTask movementTask;
    private Vector2 startPos;

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

        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.setTarget(getRandomPosInRange());
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

    private Vector2 getRandomPosInRange() {
        Vector2 halfRange = panicRange.cpy().scl(0.5f);
        Vector2 min = startPos.cpy().sub(halfRange);
        Vector2 max = startPos.cpy().add(halfRange);
        return RandomUtils.random(min, max);
    }
}
