package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

/** Follows a target entity until they get too far away or line of sight is lost */
public class FollowTask extends DefaultTask implements PriorityTask {
  /** The entity to chase. */
  private final Entity target;
  /** Task priority when following (-1 when not following). */
  private final int priority;
  /** Maximum distance from the entity at which following can start. */
  private final float viewDistance;
  /** Maximum distance from the entity while chasing before giving up. */
  private final float maxFollowDistance;
  /** The physics engine for raycasting. */
  private final PhysicsEngine physics;
  /** The debug renderer for visualization. */
  private final DebugRenderer debugRenderer;
  /** Raycast hit information. */
  private final RaycastHit hit = new RaycastHit();
  /** The movement task for following. */
  private MovementTask movementTask;
  /** Distance to target before stopping. */
  private final float stoppingDistance;

  /**
   * @param target The entity to follow.
   * @param priority Task priority when following (0 when not following).
   * @param viewDistance Maximum distance from the entity at which following can start.
   * @param maxFollowDistance Maximum distance from the entity while following before giving up.
   * @param stoppingDistance The distance at which the entity stops following target
   */
  public FollowTask(Entity target, int priority, float viewDistance, float maxFollowDistance, float stoppingDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxFollowDistance = maxFollowDistance;
    this.stoppingDistance = stoppingDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  /**
   * Starts the follow task by initializing the movement task and triggering the "followStart" event.
   */
  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition(), (float) 1.5);
    movementTask.create(owner);
    movementTask.start();

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
      movementTask.setTarget(target.getPosition());
      movementTask.update();
      if (movementTask.getStatus() != Status.ACTIVE) {
        this.owner.getEntity().getEvents().trigger("followStart");
        movementTask.start();
      }
    }
  }

  /**
   * Stops the follow task and the associated movement task, and triggers the "followStop" event.
   */
  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
    this.owner.getEntity().getEvents().trigger("followStop");
  }

  /**
   * Gets the priority level of the follow task based on the current status and conditions.
   *
   * @return The priority level.
   */
  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }

    return getInactivePriority();
  }

  /**
   * Calculates the distance between the owner's entity and the target entity.
   *
   * @return The distance between the owner's entity and the target entity.
   */
  private float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  /**
   * Determines the priority when the follow task is active based on the distance to the target
   * and the visibility of the target entity.
   *
   * @return The active priority level or -1 if following conditions are not met.
   */
  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxFollowDistance || !isTargetVisible()) {
      return -1; // Too far, stop following
    }
    return priority;
  }

  /**
   * Determines the priority when the follow task is inactive based on the distance to the target,
   * the visibility of the target entity, and the stopping distance.
   *
   * @return The inactive priority level or -1 if conditions are not met.
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible() && dst > stoppingDistance) {
      return priority;
    }
    return -1;
  }


  /**
   * Checks if the target entity is visible from the owner's entity position by performing a raycast.
   *
   * @return True if the target entity is visible, false otherwise.
   */
  private boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = target.getCenterPosition();

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point);
      return false;
    }
    debugRenderer.drawLine(from, to);
    return true;
  }
}
