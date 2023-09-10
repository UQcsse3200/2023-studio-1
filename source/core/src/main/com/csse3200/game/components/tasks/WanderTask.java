package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class WanderTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);

  /** The range in which the entity can move from its starting position. */
  private final Vector2 wanderRange;
  /** The duration in seconds to wait between wandering. */
  private final float waitTime;
  /** The starting position of the task. */
  private Vector2 startPos;
  /** The task responsible for movement. */
  private MovementTask movementTask;
  /** The task responsible for waiting. */
  private WaitTask waitTask;
  /** The currently active task (movement or waiting). */
  private Task currentTask;


  /**
   * @param wanderRange Distance in X and Y the entity can move from its position when start() is
   *     called.
   * @param waitTime How long in seconds to wait between wandering.
   */
  public WanderTask(Vector2 wanderRange, float waitTime) {
    this.wanderRange = wanderRange;
    this.waitTime = waitTime;
  }

  /**
   * Gets the priority level of the wander task.
   * @return The priority level (1 for low priority).
   */
  @Override
  public int getPriority() {
    return 1; // Low priority task
  }

  /**
   * Starts the wander task by initializing movement and waiting tasks, and triggering relevant events.
   */
  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition();

    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);
    movementTask = new MovementTask(getRandomPosInRange());
    movementTask.create(owner);

    movementTask.start();
    this.owner.getEntity().getEvents().trigger("walkStart");
    currentTask = movementTask;

    this.owner.getEntity().getEvents().trigger("wanderStart");
  }

  /**
   * Updates the wander task by handling task switching based on the current task's status.
   */
  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask) {
        startWaiting();
      } else {
        startMoving();
      }
    }
    currentTask.update();
  }

  /**
   * Switches to the waiting task and triggers relevant events.
   */
  private void startWaiting() {
    logger.debug("Starting waiting");
    swapTask(waitTask);
    this.owner.getEntity().getEvents().trigger("idleStart");
  }

  /**
   * Switches to the movement task, sets a new target, and triggers relevant events.
   */
  private void startMoving() {
    logger.debug("Starting moving");
    movementTask.setTarget(getRandomPosInRange());
    swapTask(movementTask);
    this.owner.getEntity().getEvents().trigger("walkStart");
  }

  /**
   * Stops the current task and switches to a new task.
   *
   * @param newTask The new task to switch to.
   */
  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  /**
   * Generates a random position within the specified wander range, centered around the starting position.
   *
   * @return A random position within the wander range.
   */
  private Vector2 getRandomPosInRange() {
    Vector2 halfRange = wanderRange.cpy().scl(0.5f);
    Vector2 min = startPos.cpy().sub(halfRange);
    Vector2 max = startPos.cpy().add(halfRange);
    return RandomUtils.random(min, max);
  }
}
