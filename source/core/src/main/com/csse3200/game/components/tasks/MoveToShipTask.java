package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.npc.ShipEaterScareComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * The entity finds a ship and moves towards it.
 *
 * Adapted from Team 4's MoveToPlantTask.
 * */
public class MoveToShipTask extends DefaultTask implements PriorityTask {

    /** The follow task for moving to the ship */
    private MovementTask movementTask;
    /** Task priority when moving to ship (-1 when no ships exist) */
    private final int priority;
    /** Speed at which to move to the ship */
    private Vector2 speed;
    /** Current target to move towards */
    private Entity currentTarget;
    /** Distance to target before stopping */
    private final float stoppingDistance;
    private boolean diggingUnderObstacle;
    private long startedDiggingAt;

    /**
     * @param priority Task priority when moving (-1 when not moving)
     * @param speed The speed at which to move to ship.
     */
    public MoveToShipTask(int priority, Vector2 speed, float stoppingDistance) {
        this.priority = priority;
        this.speed = speed;
        this.stoppingDistance = stoppingDistance;
        this.diggingUnderObstacle = false;
    }

    /**
     * Starts the move to ship task by initialising the target ship and triggering the movement
     * task.
     */
    @Override
    public void start() {
        super.start();

        // Look for the nearest ship entity
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        for (int i = 0; i < entities.size; i++){
            Entity entity = entities.get(i);
            if (entity.getType() == EntityType.SHIP) {
                currentTarget = entity;
                break;
            }
        }


        // Check if there are any ships in the game
        if (currentTarget == null) {
            stop();
        } else {
            // Start a movement task towards the ship
            setMovementTask(new MovementTask(getEntityTargetVector(currentTarget), speed, 1f));
            getMovementTask().create(owner);
            getMovementTask().start();
            this.owner.getEntity().getEvents().trigger("moveToShipStart");
        }
    }

    /**
     * Stop moving to the ship if already there.
     */
    @Override
    public void update() {
        if (movementTask.getStatus() == Status.FINISHED) {
            status = Status.FINISHED;
            return;
        }

        if (owner.getEntity().getComponent(ShipEaterScareComponent.class).getIsHiding()) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
            return;
        }

        if (movementTask.getStatus() == Status.FAILED) {
            // stuck, dig under obstacle
            owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NONE);
            movementTask.start();
            diggingUnderObstacle = true;
            startedDiggingAt = ServiceLocator.getTimeSource().getTime();
            owner.getEntity().getEvents().trigger("digging");
            movementTask.update();
            return;
        } else if (diggingUnderObstacle && ServiceLocator.getTimeSource().getTime() - startedDiggingAt >= 2000L) {
            owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NPC);
            diggingUnderObstacle = false;
            owner.getEntity().getEvents().trigger("walkStart");
        } else if (!diggingUnderObstacle) {
            owner.getEntity().getEvents().trigger("walkStart");
        }

        Vector2 ownerPos = owner.getEntity().getCenterPosition();
        // Stop the movement if already at the ship
        float distanceToTarget = ownerPos.dst(currentTarget.getCenterPosition());
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(distanceToTarget > stoppingDistance);

        movementTask.update();
    }

    /**
     * Stops the move to ship task and the associated movement task.
     */
    @Override
    public void stop() {
        super.stop();
        if (movementTask != null) {
            movementTask.stop();
        }
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
        this.owner.getEntity().getEvents().trigger("moveToShipStop");
    }

    /**
     * Gets the priority level of the move to ship task.
     *
     * @return The priority level.
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * Gets the movement task associated with this run away task.
     *
     * @return The movement task instance.
     */
    private MovementTask getMovementTask(){
        return movementTask;
    }

    /**
     * Setter for movementTask
     *
     * @param movementTask the movement task that is assigned
     */
    private void setMovementTask(MovementTask movementTask) {
        this.movementTask = movementTask;
    }

    /**
     * Calculates a vector towards the target entity for movement.
     *
     * @param target the target to return a vector towards.
     * @return the vector towards the entity.
     */
    private Vector2 getEntityTargetVector(Entity target) {
        Vector2 targetVec = new Vector2();
        targetVec.x = owner.getEntity().getCenterPosition().x +
                (target.getPosition().x - owner.getEntity().getCenterPosition().x);
        targetVec.y = owner.getEntity().getCenterPosition().y +
                (target.getPosition().y - owner.getEntity().getCenterPosition().y);
        return targetVec;
    }
}
