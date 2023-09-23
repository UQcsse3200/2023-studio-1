package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;

/** The entity finds a living plant and begins attacking it. */
public class AttackPlantTask extends DefaultTask implements PriorityTask {

    /** Task priority when chasing (-1 when not chasing). */
    private final int priority;
    /** The physics engine for raycasting. */
    private final PhysicsEngine physics;
    /** The debug renderer for visualization. */
    private final DebugRenderer debugRenderer;
    /** The movement task for chasing. */
    private MovementTask movementTask;
    /** The speed at which entity moves to the target. */
    private Vector2 moveSpeed;
    /** The current target of the hostile */
    private Entity currentTarget;

    /**
     * @param priority Task priority when chasing (0 when not chasing).
     */
    public AttackPlantTask(int priority, Vector2 moveSpeed) {
        this.priority = priority;
        this.moveSpeed = moveSpeed;
        this.currentTarget = null;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    @Override
    public void start() {
        status = Status.ACTIVE;
        currentTarget = getNearestPlant();
        if (currentTarget == null) {
            stop();
        }
        moveToTarget(currentTarget);
        if (getMovementTask().isAtTarget()) {
            // Attack the plant
            attackPlant();
        } else {
            // Move to the target
            moveToTarget(currentTarget);
        }
    }

    @Override
    public void update() {
        // Check if plant is dead
        System.out.println("Update " + currentTarget.getComponent(PlantComponent.class).getPlantName());
        currentTarget = getNearestPlant();
        if (currentTarget == null) {
            stop();
        }

        // There is a plant to target
        moveToTarget(currentTarget);
        // Check if we are already at the plant
        if (movementTask.isAtTarget()) {
            attackPlant();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }


    @Override
    public int getPriority() {
        Entity target = getNearestPlant();
        if (target == null) {
            return -1;
        }
        return priority;
    }



    private void attackPlant() {
        // Attack the plant
        movementTask.stop();

        this.owner.getEntity().getEvents().trigger("attack");
        currentTarget.getEvents().trigger("attack");
        String currentDirection = movementTask.getDirection();
        this.owner.getEntity().getEvents().trigger("directionChange", currentDirection);

        System.out.println(currentTarget.getComponent(PlantComponent.class).getPlantHealth());
        System.out.println(currentTarget.getComponent(PlantComponent.class).getPlantName());
//        currentTarget.getComponent(PlantComponent.class)
//                .increasePlantHealth(-(this.owner.getEntity()
//                        .getComponent(CombatStatsComponent.class).getBaseAttack()));
    }

    private void moveToTarget(Entity target) {
        Vector2 targetVec = new Vector2();
        targetVec.x = owner.getEntity().getCenterPosition().x +
                (target.getCenterPosition().x - owner.getEntity().getCenterPosition().x);
        targetVec.y = owner.getEntity().getCenterPosition().y +
                (target.getCenterPosition().y - owner.getEntity().getCenterPosition().y);

        setMovementTask(new MovementTask(targetVec, moveSpeed));
        getMovementTask().create(owner);
        getMovementTask().start();
    }


    /**
     * Finds the nearest plant
     *
     * @return The nearest plant entity, null if there are no plants.
     */
    private Entity getNearestPlant() {
        Array<Entity> entities = ServiceLocator.getEntityService().getEntityList();
        int size = entities.size;


        HashMap<Entity, Float> plantDistances = new HashMap<Entity, Float>();

        // Find a target
        for (int i = 0; i < size; i++) {
            Entity entity = entities.get(i);
            if (entity.getType() == EntityType.Plant) {
                Float dist = owner.getEntity().getCenterPosition().dst(entity.getCenterPosition());
                plantDistances.put(entity, dist);
            }
        }

        System.out.println(plantDistances.size());

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

        System.out.println(closestPlant.getComponent(PlantComponent.class).getPlantName());

        return closestPlant;
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

}
