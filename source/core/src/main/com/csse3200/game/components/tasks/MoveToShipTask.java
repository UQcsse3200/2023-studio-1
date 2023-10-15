package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * The entity finds a ship and moves towards it.
 *
 * Adapted from Team 4's MoveToPlantTask.
 * */
public class MoveToShipTask extends DefaultTask implements PriorityTask {
    private MovementTask movementTask;
    private final int priority;
    private final Vector2 speed;
    private Entity currentTarget;
    private final float stoppingDistance;

    private boolean isDigging;
    private boolean isHiding;
    private boolean isEating;

    private long startedDiggingAt;

    /**
     * @param priority Task priority when moving (-1 when not moving)
     * @param speed The speed at which to move to ship.
     */
    public MoveToShipTask(int priority, Vector2 speed, float stoppingDistance) {
        this.priority = priority;
        this.speed = speed;
        this.stoppingDistance = stoppingDistance;
        this.isDigging = false;
        this.isHiding = false;
    }

    @Override
    public void create(TaskRunner taskRunner) {
        super.create(taskRunner);

        owner.getEntity().getEvents().addListener("hidingUpdated", this::setHiding);
        owner.getEntity().getEvents().addListener("eatingUpdated", this::setEating);
    }

    private void setEating(boolean isEating) {
        this.isEating = isEating;
        if (isEating) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
            if (movementTask != null) movementTask.stop();
        } else {
            if (movementTask != null) movementTask.start();
        }
    }

    private void setHiding(boolean isHiding) {
        this.isHiding = isHiding;
        if (isHiding) {
            owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NONE);
            owner.getEntity().getComponent(HitboxComponent.class).setLayer(PhysicsLayer.NONE);
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
            if (movementTask != null) movementTask.stop();
        } else {
            owner.getEntity().getComponent(HitboxComponent.class).setLayer(PhysicsLayer.NPC);
            owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NPC);
            if (movementTask != null) movementTask.start();
        }
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
            setMovementTask(new MovementTask(currentTarget.getCenterPosition(), speed, stoppingDistance));
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
        if (status == Status.INACTIVE || movementTask == null) {
            return;
        }

        if (isHiding || isEating || movementTask.getStatus() == Status.FINISHED) {
            status = movementTask.getStatus();
            if (isDigging) {
                isDigging = false;
                owner.getEntity().getEvents().trigger("diggingUpdated", false);
            }
            return;
        }

        if (movementTask.getStatus() == Status.FAILED) {
            // stuck, dig under obstacle
            owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NONE);
            movementTask.start();
            isDigging = true;
            startedDiggingAt = ServiceLocator.getTimeSource().getTime();
            owner.getEntity().getEvents().trigger("diggingUpdated", true);
            movementTask.update();
            return;
        } else if (isDigging && ServiceLocator.getTimeSource().getTime() - startedDiggingAt >= 1000L) {
            TerrainTile tile = ServiceLocator.getGameArea().getMap().getTile(owner.getEntity().getPosition());
            if (canStopDiggingOnTile(tile)) {
                isDigging = false;
                owner.getEntity().getEvents().trigger("diggingUpdated", false);
            }
            startedDiggingAt = ServiceLocator.getTimeSource().getTime();
        }

        float distanceToTarget = owner.getEntity().getCenterPosition().dst(currentTarget.getCenterPosition());
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(distanceToTarget > stoppingDistance);

        movementTask.update();
    }

    private boolean canStopDiggingOnTile(TerrainTile tile) {
        return tile.isTraversable() && (
                        !tile.isOccupied() ||
                                (tile.getOccupant() != null && tile.getOccupant().getType() == EntityType.TILE)
                );
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
}
