package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.DirectionUtils;
import com.csse3200.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

  private final GameTime gameTime;
  private Vector2 target;
  private float stopDistance = 0.01f;
  private long lastTimeMoved;
  private Vector2 lastPos;
  private PhysicsMovementComponent movementComponent;
  private String currentDirection;
  private Vector2 speed = Vector2Utils.ONE;

  public MovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
    this.currentDirection = DirectionUtils.RIGHT;
  }

  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  public MovementTask(Vector2 target, Vector2 speed) {
    this(target);
    this.speed = speed;
  }

  @Override
  public void create(TaskRunner taskRunner) {
    super.create(taskRunner);
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
  }

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
    this.owner.getEntity().getEvents().trigger("directionChange", currentDirection);
  }

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
        this.owner.getEntity().getEvents().trigger("directionChange", currentDirection);
      }

      checkIfStuck();
    }
  }

  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
  }

  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false);
    movementComponent.setMaxSpeed(Vector2Utils.ONE);
    logger.debug("Stopping movement");
  }

  private boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target) <= stopDistance;
  }

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

  private boolean didMove() {
    return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
  }

  private String getDirection() {
    Vector2 currentPosition = owner.getEntity().getPosition();
    return currentPosition.sub(target).x < 0 ? DirectionUtils.RIGHT : DirectionUtils.LEFT;
  }
}
