package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;

/** Runs from a target entity until they get too far away or line of sight is lost */
public class RunAwayTask extends ChaseTask {
  /** Maximum distance from the entity while run away before giving up. */
  private final float maxRunDistance;

  /** The speed at which entity runs away from target. */
  private Vector2 runSpeed;

  /**
   * @param target The entity to run from.
   * @param priority Task priority when running (0 when not running away).
   * @param viewDistance Maximum distance from the entity at which running away can start.
   * @param maxRunDistance Maximum distance from the entity before stopping.
   */
  public RunAwayTask(Entity target, int priority, float viewDistance, float maxRunDistance, Vector2 runSpeed) {
    super(target, priority, viewDistance, maxRunDistance);
    this.maxRunDistance = maxRunDistance;
    this.runSpeed = runSpeed;
  }

  /**
   * Starts the chase task by initializing the movement task and triggering the run events.
   */
  @Override
  public void start() {
    status = Status.ACTIVE;
    Vector2 targetVec = new Vector2();
    targetVec.x = owner.getEntity().getCenterPosition().x +
            (owner.getEntity().getCenterPosition().x - getTarget().getCenterPosition().x);
    targetVec.y = owner.getEntity().getCenterPosition().y +
            (owner.getEntity().getCenterPosition().y - getTarget().getCenterPosition().y);
    setMovementTask(new MovementTask(targetVec, runSpeed));
    getMovementTask().create(owner);
    getMovementTask().start();

    this.owner.getEntity().getEvents().trigger("runAwayStart");
    this.owner.getEntity().getEvents().trigger("runStart");
  }

  /**
   * Updates the RunAwayTask by updating the movement task and restarting it if necessary.
   */
  @Override
  public void update() {
    Vector2 targetVec = new Vector2();
    targetVec.x = owner.getEntity().getCenterPosition().x +
            (owner.getEntity().getCenterPosition().x - getTarget().getCenterPosition().x);
    targetVec.y = owner.getEntity().getCenterPosition().y +
            (owner.getEntity().getCenterPosition().y - getTarget().getCenterPosition().y);
    getMovementTask().setTarget(targetVec);
    getMovementTask().update();
    if (getMovementTask().getStatus() != Status.ACTIVE) {
      getMovementTask().start();
    }
  }

  /**
   * Stops the RunAwayTask and the associated movement task, and triggers the "runAwayStop" event.
   */
  @Override
  public void stop() {
    super.stop();
    this.owner.getEntity().getEvents().trigger("runAwayStop");
  }

  /**
   * Determines the priority when the RunAwayTask is active based on the distance to the target
   * and the visibility of the target entity.
   *
   * @return The active priority level or -1 if conditions are not met.
   */
  @Override
  protected int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxRunDistance || !isTargetVisible()) {
      return -1; // Too far, stop running away
    }
    return getRawPriority();
  }
}
