package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.utils.math.Vector2Utils;

/** Follows a target entity until they get too far away or line of sight is lost */
public class FollowTask extends ChaseTask {
  /** Distance to target before stopping. */
  private final float stoppingDistance;

  /**
   * @param target The entity to follow.
   * @param priority Task priority when following (0 when not following).
   * @param viewDistance Maximum distance from the entity at which following can start.
   * @param maxFollowDistance Maximum distance from the entity while following before giving up.
   * @param stoppingDistance The distance at which the entity stops following target
   */
  public FollowTask(Entity target, int priority, float viewDistance, float maxFollowDistance,
                    float stoppingDistance, Vector2 speed) {
    super(target, priority, viewDistance, maxFollowDistance, speed);
    this.stoppingDistance = stoppingDistance;
  }

  /**
   * Starts the follow task by initializing the movement task and triggering the "followStart" event.
   */
  @Override
  public void start() {
    status = Status.ACTIVE;
    setMovementTask(new MovementTask(getTarget().getCenterPosition(), (float) 1.5));
    getMovementTask().create(owner);
    getMovementTask().start();

    this.owner.getEntity().getEvents().trigger("followStart");
  }

  /**
   * Updates the follow task by updating the movement task and stopping it if the entity is too close to the target.
   */
  @Override
  public void update() {
    //Stops follow if entity is too close to target
    if(getDistanceToTarget() <= stoppingDistance){
      stop();
    } else {
      getMovementTask().setTarget(getTarget().getCenterPosition());
      getMovementTask().update();
      if (getMovementTask().getStatus() != Status.ACTIVE) {
        this.owner.getEntity().getEvents().trigger("followStart");
        getMovementTask().start();
      }
    }
  }

  /**
   * Stops the follow task and the associated movement task, and triggers the "followStop" event.
   */
  @Override
  public void stop() {
    super.stop();
    this.owner.getEntity().getEvents().trigger("followStop");
  }

  /**
   * Determines the priority when the follow task is inactive based on the distance to the target,
   * the visibility of the target entity, and the stopping distance.
   *
   * @return The inactive priority level or -1 if conditions are not met.
   */
  @Override
  protected int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < getViewDistance() && isTargetVisible() && dst > stoppingDistance) {
      return getRawPriority();
    }
    return -1;
  }
}
