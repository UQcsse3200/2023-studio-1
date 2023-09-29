package com.csse3200.game.components.tasks;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
  /** Represents the game time for timing calculations. */
  private final GameTime gameTime;
  /**  The target position to move towards. */
  private Vector2 target;
  /** The distance at which the task is considered finished. */
  private float stopDistance = 0.1f;
  /** The time when the entity last moved. */
  private long lastTimeMoved;
  /** The last recorded position of the entity. */
  private Vector2 lastPos;
  /** The component responsible for physics-based movement. */
  private PhysicsMovementComponent movementComponent;
  /** The current movement direction of the entity. */
  private String currentDirection;
  /** The movement speed of the entity. */
  private Vector2 speed = Vector2Utils.ONE;

  /**
   * Creates a movement task with a target position.
   *
   * @param target The target position to move towards.
   */
  public MovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
    this.currentDirection = DirectionUtils.RIGHT;
  }

  /**
   * Creates a movement task with a target position and a specified stop distance.
   *
   * @param target The target position to move towards.
   * @param stopDistance The distance at which the task is considered finished.
   */
  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  /**
   * Creates a movement task with a target position and a specified speed.
   *
   * @param target The target position to move towards.
   * @param speed The movement speed of the entity.
   */
  public MovementTask(Vector2 target, Vector2 speed) {
    this(target);
    this.speed = speed;
  }

  /**
   * Creates a movement task with a target position and a specified speed.
   *
   * @param target The target position to move towards.
   * @param speed The movement speed of the entity.
   * @param stopDistance The distance at which the task is considered finished.
   */
  public MovementTask(Vector2 target, Vector2 speed, float stopDistance) {
    this(target);
    this.speed = speed;
    this.stopDistance = stopDistance;
  }

  /**
   * Retrieves the necessary components and initializes the movement task.
   *
   * @param taskRunner The task runner associated with this task.
   */
  @Override
  public void create(TaskRunner taskRunner) {
    super.create(taskRunner);
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
  }

  /**
   * Starts the movement task by setting up the movement parameters and triggering the start event.
   */
  @Override
  public void start() {
    super.start();
    movementComponent.setMaxSpeed(speed);
    movementComponent.setTarget(target);
    movementComponent.setMoving(true);
    logger.debug("Starting movement towards {}", target);
    lastTimeMoved = gameTime.getTime();
    lastPos = owner.getEntity().getPosition();

    this.currentDirection = getDirection();
    System.out.println("Direction change start");
    this.owner.getEntity().getEvents().trigger("directionChange", currentDirection);
  }

  /**
   * Updates the movement task by checking if the target has been reached and handling movement changes.
   */
  @Override
  public void update() {
    if (isAtTarget()) {
      movementComponent.setMoving(false);
      status = Status.FINISHED;
      logger.debug("Finished moving to {}", target);
    } else {
      // If direction changes during movement task, animation must be re-triggered with new direction.
      if (!Objects.equals(currentDirection, getDirection())) {
        this.currentDirection = getDirection();
        System.out.println("Direction change update");
        this.owner.getEntity().getEvents().trigger("directionChange", currentDirection);
      }

      checkIfStuck();
    }
  }

  /**
   * Sets a new target position for the movement task.
   *
   * @param target The new target position.
   */
  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
  }

  /**
   * Stops the movement task and resets movement-related parameters.
   */
  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false);
    movementComponent.setMaxSpeed(Vector2Utils.ONE);
    logger.debug("Stopping movement");
  }

  /**
   * Checks if the entity has reached the target position.
   *
   * @return True if the entity is within the stop distance from the target, otherwise false.
   */
  public boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target) <= stopDistance;
  }

  /**
   * Checks if the entity is stuck by comparing its current position with the last recorded position.
   * If the entity hasn't moved for a certain time, the task is considered failed.
   */
  private void checkIfStuck() {
    if (didMove()) {
      lastTimeMoved = gameTime.getTime();
      lastPos = owner.getEntity().getPosition();
    } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
      movementComponent.setMoving(false);
      status = Status.FAILED;
      logger.debug("Got stuck! Failing movement task");
    }
  }

  /**
   * Checks if the entity's position has changed since the last recorded position.
   *
   * @return True if the entity's position has changed, otherwise false.
   */
  private boolean didMove() {
    return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
  }

  /**
   * Determines the direction of movement based on the current and target positions.
   *
   * @return The direction of movement ("right" or "left").
   */
  public String getDirection() {
    Vector2 currentPosition = owner.getEntity().getPosition();
    return currentPosition.sub(target).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;
  }

  /**
   * Retrieves the movement speed of the entity.
   *
   * @return The movement speed vector.
   */
  public Vector2 getSpeed() {
    return speed;
  }
}
