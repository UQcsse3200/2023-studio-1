package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

/** Runs from a target entity until they get too far away or line of sight is lost */
public class RunAwayTask extends DefaultTask implements PriorityTask {
  private final Entity target;
  private final int priority;
  private final float viewDistance;
  private final float maxRunDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private MovementTask movementTask;
  private Vector2 runSpeed;

  /**
   * @param target The entity to run from.
   * @param priority Task priority when running (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which running away can start.
   * @param maxRunDistance Maximum distance from the entity before stopping.
   */
  public RunAwayTask(Entity target, int priority, float viewDistance, float maxRunDistance, Vector2 runSpeed) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxRunDistance = maxRunDistance;
    this.runSpeed = runSpeed;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void create(TaskRunner taskRunner) {
    super.create(taskRunner);

  }

  @Override
  public void start() {
    super.start();
    Vector2 targetVec = new Vector2();
    targetVec.x = owner.getEntity().getPosition().x +
            (owner.getEntity().getPosition().x - target.getPosition().x);
    targetVec.y = owner.getEntity().getPosition().y +
            (owner.getEntity().getPosition().y - target.getPosition().y);
    movementTask = new MovementTask(targetVec, runSpeed);
    movementTask.create(owner);
    movementTask.start();

    this.owner.getEntity().getEvents().trigger("runAwayStart");
    this.owner.getEntity().getEvents().trigger("runStart");
  }

  @Override
  public void update() {
    Vector2 targetVec = new Vector2();
    targetVec.x = owner.getEntity().getPosition().x +
            (owner.getEntity().getPosition().x - target.getPosition().x);
    targetVec.y = owner.getEntity().getPosition().y +
            (owner.getEntity().getPosition().y - target.getPosition().y);
    movementTask.setTarget(targetVec);
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
    this.owner.getEntity().getEvents().trigger("runAwayStop");
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
    if (dst > maxRunDistance || !isTargetVisible()) {
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
