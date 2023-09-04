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

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
  /** The entity to chase. */
  private final Entity target;
  /** Task priority when chasing (-1 when not chasing). */
  private final int priority;
  /** Maximum distance from the entity at which chasing can start. */
  private final float viewDistance;
  /** Maximum distance from the entity while chasing before giving up. */
  private final float maxChaseDistance;
  /** The physics engine for raycasting. */
  private final PhysicsEngine physics;
  /** The debug renderer for visualization. */
  private final DebugRenderer debugRenderer;
  /** Raycast hit information. */
  private final RaycastHit hit = new RaycastHit();
  /** The movement task for chasing. */
  private MovementTask movementTask;

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  /**
   * Starts the chase task by initializing the movement task and triggering the "chaseStart" event.
   */
  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    
    this.owner.getEntity().getEvents().trigger("chaseStart");
  }

  /**
   * Updates the chase task by updating the movement task and restarting it if necessary.
   */
  @Override
  public void update() {
    movementTask.setTarget(target.getPosition());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  /**
   * Stops the chase task and the associated movement task.
   */
  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  /**
   * Gets the priority level of the chase task based on the current status and conditions.
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
  protected float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  /**
   * Determines the priority when the chase task is active based on the distance to the target
   * and the visibility of the target entity.
   *
   * @return The active priority level or -1 if chasing conditions are not met.
   */
  protected int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  /**
   * Determines the priority when the chase task is inactive based on the distance to the target
   * and the visibility of the target entity.
   *
   * @return The inactive priority level or -1 if conditions are not met.
   */
  protected int getInactivePriority() {
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
  protected boolean isTargetVisible() {
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

  /**
   * Setter for movementTask
   *
   * @param movementTask the movement task that is assigned
   */
  public void setMovementTask(MovementTask movementTask) {
    this.movementTask = movementTask;
  }

  /**
   * Getter for the priority of this task if it's active
   *
   * @return priority of this task
   */
  public int getRawPriority() {
    return priority;
  }

  /**
   * Getter for the target entity that this task is related to
   *
   * @return target entity
   */
  public Entity getTarget() {
    return target;
  }
}
