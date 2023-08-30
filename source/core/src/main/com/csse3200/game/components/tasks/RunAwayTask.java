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
  /** The entity to run away from. */
  private final Entity target;
  /** Task priority when running away (-1 when not running away). */
  private final int priority;
  /** Maximum distance from the entity at which running away can start. */
  private final float viewDistance;
  /** Maximum distance from the entity while run away before giving up. */
  private final float maxRunDistance;
  /** The physics engine for raycasting. */
  private final PhysicsEngine physics;
  /** The debug renderer for visualization. */
  private final DebugRenderer debugRenderer;
  /** Raycast hit information. */
  private final RaycastHit hit = new RaycastHit();
  /** The movement task for running away. */
  private MovementTask movementTask;
  /** The speed at which entity runs away from target. */
  private Vector2 runSpeed;

  /**
   * @param target The entity to run from.
   * @param priority Task priority when running (0 when not running away).
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

  /**
   * Starts the chase task by initializing the movement task and triggering the run events.
   */
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

  /**
   * Updates the run away task by updating the movement task and restarting it if necessary.
   */
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

  /**
   * Stops the run away task and the associated movement task, and triggers the "runAwayStop" event.
   */
  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
    this.owner.getEntity().getEvents().trigger("runAwayStop");
  }

  /**
   * Gets the priority level of the run away task based on the current status and conditions.
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
   * Determines the priority when the run away task is active based on the distance to the target
   * and the visibility of the target entity.
   *
   * @return The active priority level or -1 if conditions are not met.
   */
  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxRunDistance || !isTargetVisible()) {
      return -1; // Too far, stop running away
    }
    return priority;
  }

  /**
   * Determines the priority when the run away task is inactive based on the distance to the target
   * and the visibility of the target entity.
   *
   * @return The inactive priority level or -1 if conditions are not met.
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
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

  /**
   * Gets the movement task associated with this run away task.
   *
   * @return The movement task instance.
   */
  public MovementTask getMovementTask(){
    return movementTask;
  }
}
