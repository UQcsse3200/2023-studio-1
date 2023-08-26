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
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxFollowDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not following).
   * @param viewDistance Maximum distance from the entity at which following can start.
   * @param maxFollowDistance Maximum distance from the entity while following before giving up.
   */
  public FollowTask(Entity target, int priority, float viewDistance, float maxFollowDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxFollowDistance = maxFollowDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition(), (float) 1.5);
    movementTask.create(owner);
    movementTask.start();

    this.owner.getEntity().getEvents().trigger("followStart");
  }

  @Override
  public void update() {
    //Stops follow if entity is too close to target
    if(getDistanceToTarget() <= 1.50){
      stop();
    } else {
      movementTask.setTarget(target.getPosition());
      movementTask.update();
      if (movementTask.getStatus() != Status.ACTIVE) {
        movementTask.start();
      }
    }
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
    this.owner.getEntity().getEvents().trigger("followStop");
  }

  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }

    return getInactivePriority();
  }

  private float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxFollowDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

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
