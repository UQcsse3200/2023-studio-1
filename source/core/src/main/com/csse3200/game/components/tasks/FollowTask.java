package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;


/** Follows a target entity until they get too far away or line of sight is lost */
public class FollowTask extends ChaseTask {
  /** Distance to target before stopping. */
  private final float stoppingDistance;
  /** Speed to follow the player. */
  private final Vector2 speed;

  /**
   * @param target The entity to follow.
   * @param priority Task priority when following (0 when not following).
   * @param viewDistance Maximum distance from the entity at which following can start.
   * @param maxFollowDistance Maximum distance from the entity while following before giving up.
   * @param stoppingDistance The distance at which the entity stops following target.
   * @param speed The speed at which the entity follows the player.
   */
  public FollowTask(Entity target, int priority, float viewDistance, float maxFollowDistance,
                    float stoppingDistance, Vector2 speed) {
    super(target, priority, viewDistance, maxFollowDistance, speed);
    this.stoppingDistance = stoppingDistance;
    this.speed = speed;
  }


  /**
   * @param target The entity to follow.
   * @param priority Task priority when following (0 when not following).
   * @param viewDistance Maximum distance from the entity at which following can start.
   * @param maxFollowDistance Maximum distance from the entity while following before giving up.
   * @param stoppingDistance The distance at which the entity stops following target.
   * @param speed The speed at which the entity follows the player.
   * @param checkVisibility  Checks to see if the entity will consider obstacles in its path.
   */
  public FollowTask(Entity target, int priority, float viewDistance, float maxFollowDistance,
                    float stoppingDistance, Vector2 speed, boolean checkVisibility) {
    super(target, priority, viewDistance, maxFollowDistance, speed, checkVisibility);
    this.stoppingDistance = stoppingDistance;
    this.speed = speed;
  }

  /**
   * Starts the follow task by initializing the movement task and triggering the "followStart" event.
   */
  @Override
  public void start() {
    status = Status.ACTIVE;
    setMovementTask(new MovementTask(getTarget().getCenterPosition(), speed, 1.5f));
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
    if(getDistanceToTarget() <= stoppingDistance) {
      owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
      status = Status.INACTIVE;
      owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
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
    owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
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
