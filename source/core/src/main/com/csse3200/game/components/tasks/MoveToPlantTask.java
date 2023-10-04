package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** The entity finds a living plant and moves towards it */
public class MoveToPlantTask extends DefaultTask implements PriorityTask {

    /** The follow task for moving to the plant */
    private MovementTask movementTask;
    /** Task priority when moving to plant (-1 when no plants exist) */
    private final int priority;
    /** Speed at which to move to the plant */
    private Vector2 speed;
    /** Current target to move towards */
    private Entity currentTarget;
    /** Distance to target before stopping */
    private final float stoppingDistance;

    /**
     * @param priority Task priority when moving (-1 when not moving)
     * @param speed The speed at which to move to plant.
     */
    public MoveToPlantTask(int priority, Vector2 speed, float stoppingDistance) {
        this.priority = priority;
        this.speed = speed;
        this.stoppingDistance = stoppingDistance;
    }

    /**
     * Starts the move to plant task by initialising the target plant and triggering the movement
     * task.
     */
    @Override
    public void start() {
        super.start();

        // Look for the nearest plant entity
        currentTarget = getNearestPlant();

        // Check if there are any plants in the game
        if (currentTarget == null) {
            stop();
        } else {
            // Start a movement task towards the plant
            setMovementTask(new MovementTask(getEntityTargetVector(currentTarget), speed, 1f));
            getMovementTask().create(owner);
            getMovementTask().start();
            this.owner.getEntity().getEvents().trigger("moveToPlantStart");
        }
    }

    /**
     * Updates the move to plant task by checking if the current target is dead. If the current
     * target is not dead, it will continue to move to it until it has arrived. If the current
     * target is dead, it will switch to a new target if one exists.
     */
    @Override
    public void update() {
        // Check what the entity is currently targeting
        Entity plant = getNearestPlant();

        if (plant == null) {
            // There are no plants left
            stop();
            return;
        }
        // If the current target is still the same, do nothing
        if (currentTarget == plant) {
            // Stop the movement if already at the plant
            float distanceToTarget =
                    owner.getEntity().getCenterPosition().dst(currentTarget.getCenterPosition());

            if (distanceToTarget <= stoppingDistance) {
                owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
            } else {
                owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
            }
            return;
        }

        // The current target is gone and the movement task should target a new plant
        currentTarget = plant;
        getMovementTask().setTarget(getEntityTargetVector(currentTarget));
        getMovementTask().update();
        if (getMovementTask().getStatus() != Status.ACTIVE) {
            this.owner.getEntity().getEvents().trigger("moveToPlantStart");
            getMovementTask().start();
        }
    }

    /**
     * Stops the move to plant task and the associated movement task.
     */
    @Override
    public void stop() {
        super.stop();
        if (movementTask != null) {
            movementTask.stop();
        }
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
        this.owner.getEntity().getEvents().trigger("moveToPlantStop");
    }

    /**
     * Gets the priority level of the move to plant task.
     *
     * @return The priority level.
     */
    @Override
    public int getPriority() {
        if (getNearestPlant() == null) {
            return -1;
        }
        return priority;
    }

    /**
     * Finds the nearest plant
     *
     * @return The nearest plant entity, null if there are no plants.
     */
    private Entity getNearestPlant() {
        Array<Entity> entities = ServiceLocator.getEntityService().getEntityList();
        int size = entities.size;

        HashMap<Entity, Float> plantDistances = new HashMap<>();

        // Find a target
        for (int i = 0; i < size; i++) {
            Entity entity = entities.get(i);
            if (entity.getType() == EntityType.PLANT) {
                Float dist = owner.getEntity().getCenterPosition().dst(entity.getCenterPosition());
                plantDistances.put(entity, dist);
            }
        }

        // Check if there are any plants
        if (plantDistances.isEmpty()) {
            return null; // No plants found
        }

        // Find the closest plant
        Entity closestPlant = null;
        Float minDistance = Float.MAX_VALUE;

        Set<Map.Entry<Entity, Float>> entries = plantDistances.entrySet();
        Iterator<Map.Entry<Entity, Float>> iterator = entries.iterator();

        for (int i = 0; i < plantDistances.size(); i++) {
            Map.Entry<Entity, Float> entry = iterator.next();
            Entity entity = entry.getKey();
            Float dist = entry.getValue();

            if (dist < minDistance) {
                minDistance = dist;
                closestPlant = entity;
            }
        }

        return closestPlant;
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
