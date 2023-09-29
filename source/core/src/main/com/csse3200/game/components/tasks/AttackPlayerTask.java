package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

/**
 * Notes from UniversalTze
 * Main idea for this hostile was that it would just wander until the player popped into frame.
 * If player is in frame and within chase distance, hostile will try to attack the player.
 * When in attacking distance, hostile will attack the player
 * If player gets out of sight or outside max chase distance, hostile will just continue to wonder until it comes
 * back.
 *
 * (To be deleted after implemented......)
 */
public class AttackPlayerTask extends DefaultTask implements PriorityTask {
    /** The entity to chase and attack */
    private final Entity target;
    /** Task priority when attacking (-1 when not). */
    private final int priority;
    /** Speed the entity can chase the player. */
    private final float runspeed;
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

    private final float attackDistance;

    public AttackPlayerTask(Entity target, int priority, float runspeed, float maxChaseDistance) {
        this.target = target;
        this.priority = priority;
        this.runspeed = runspeed;
        this.maxChaseDistance = maxChaseDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        this.attackDistance = 1.5f;
    }

    @Override
    public void start() {
        if (!isTargetVisible() && maxChaseDistance < getDistanceToTarget()) {
            stop();
        }
        status = Status.ACTIVE;
        moveToTarget(getTarget());
        if (inRange()) {
            //attack the player
            //Need vidyut to write the attack player function with animations.
        }
    }

    @Override
    public void update() {
       //need to check if the player is dead.
        if (target.getComponent(CombatStatsComponent.class).isDead()) {
            //do something here end the game ???
            status = Status.FINISHED;
            stop(); //stop the movement.
        }
        if (!isTargetVisible() || getDistanceToTarget() > maxChaseDistance) {
            stop();
        }
        if (status == Status.INACTIVE && isTargetVisible() && getDistanceToTarget() < maxChaseDistance) {
            start();
        }
        //can assume that status is active, player is within distance and is visible.
        moveToTarget(getTarget());
        if (inRange()) {
            //attack the player
            //Need vidyut to write the attack player function with animations.
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

    private void moveToTarget(Entity target) {
        Vector2 targetVec = new Vector2();
        targetVec.x = owner.getEntity().getCenterPosition().x +
                (target.getCenterPosition().x - owner.getEntity().getCenterPosition().x);
        targetVec.y = owner.getEntity().getCenterPosition().y +
                (target.getCenterPosition().y - owner.getEntity().getCenterPosition().y);

        setMovementTask(new MovementTask(targetVec, this.runspeed));
        getMovementTask().create(owner);
        getMovementTask().start();
    }

    protected float getDistanceToTarget() {
        return owner.getEntity().getCenterPosition().dst(target.getCenterPosition());
    }

    /**
     * Gets the priority level of the chase task based on the current status and conditions.
     *
     * @return The priority level.
     */
    @Override
    public int getPriority() {
        if (isTargetVisible() && getDistanceToTarget() < maxChaseDistance) {
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
     * Gets the movement task associated with this attack player task.
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
     * Getter for the target entity that this task is related to
     *
     * @return target entity
     */
    private Entity getTarget() {
        return target;
    }

    private boolean inRange() {
        if (getDistanceToTarget() < this.attackDistance) { //distance for hostile to start attacking player
            return true;
        }
        return false;
    }
}

