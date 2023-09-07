package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.TaskRunner;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * This is to provide the behaviour of an animal when it has tamed. Currently,
 * this class sets a max distance the animal can wonder when tamed.
 */
public class TamedTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);
    private final Entity target;
    private final Entity animal;
    private final int priority;

    private MovementTask movementTask;
    //max distance the animal can wonder away from the user in both x and y directions.
    private final float TamedAnimalDistance = 50.00f;
    private Vector2 animalPace;

    /**
     * The constructor for the TamedTask. This provides behaviour for when the animal has been tamed.
     * The animal entity must have a tameable component.
     * @param user The main player entity
     * @param animal The animal that has been tamed. IT Must have the Tameable component
     * @param priority Priority level of task
     * @param pace The speed the animal moves at when completing this task
     */
    public TamedTask(Entity user, Entity animal, int priority, Vector2 pace) {
        this.target = user;
        this.animal = animal;
        this.priority = priority;
        this.animalPace = pace;
        //If animal does not have a tameable component or is not tamed, then return.
        if (!checkTameComponent(this.animal) || !this.checkTameStatus(this.animal)) {
            return;
        }
    }

    /**
     * This is to check to see if the animal has a Tameable component.
     * @param animal The entity to check
     * @return True if the animal has a Tameable component. Else it will return False
     */
    private boolean checkTameComponent(Entity animal) {
        if (animal.getComponent(TamableComponent.class) == null) {
            return false;
        }
        return true;
    }

    /**
     * This is to check the tame status of the animal.
     * @param animal The entity to be tested to see if has been tamed.
     * @return True if animal has been tamed, else false.
     */
    private boolean checkTameStatus(Entity animal) {
        if (animal.getComponent(TamableComponent.class).isTamed()) {
            return true;
        }
        return false;
    }

    /**
     * Finds the distance between the user and the animal in the x direction.
     * @param user The main player entity
     * @param animal The Tamed animal entity
     * @return
     */
    private float findDistanceX(Entity user, Entity animal) {
        float userX = user.getCenterPosition().x;
        float animalX = animal.getCenterPosition().x;
        return Math.abs(userX - animalX);
    }

    private float findDistanceY(Entity user, Entity animal) {
        float userY = user.getCenterPosition().x;
        float animalY = animal.getCenterPosition().x;
        return Math.abs(userY - animalY);
    }

    @Override
    public void start() {
        //This one needs to be modified
        super.start();
        Vector2 targetLoc = new Vector2();
        float upperboundX;
        float upperboundY;
        if (findDistanceX(this.target, this.animal) > TamedAnimalDistance) {
            upperboundX = TamedAnimalDistance;
        } else {
            upperboundX = this.target.getCenterPosition().x;
        }
        if (findDistanceY(this.target, this.animal) > TamedAnimalDistance) {
            upperboundY = TamedAnimalDistance;
        }
        else {
            upperboundY = this.target.getCenterPosition().y;
        }
        //gets a random position in both x and y coordinates that is below 50.
        //That's if my math is correct
        targetLoc.x = this.target.getPosition().x + (random.nextFloat() * upperboundX + 1);
        targetLoc.y = this.target.getPosition().y + (random.nextFloat() * upperboundY + 1);
        movementTask = new MovementTask(targetLoc, this.animalPace);
        movementTask.create((TaskRunner) this.animal);
        movementTask.start();
        ((TaskRunner)this.animal).getEntity().getEvents().trigger("TamedIdle");
    }

    @Override
    public void update() {
        if (!this.animal.getComponent(TamableComponent.class).isTamed()) {
            this.stop();
        }
        Vector2 targetLoc = new Vector2();
        float upperboundX;
        float upperboundY;
        if (findDistanceX(this.target, this.animal) > TamedAnimalDistance) {
            upperboundX = TamedAnimalDistance;
        } else {
            upperboundX = this.target.getCenterPosition().x;
        }
        if (findDistanceY(this.target, this.animal) > TamedAnimalDistance) {
            upperboundY = TamedAnimalDistance;
        }
        else {
            upperboundY = this.target.getCenterPosition().y;
        }
        //gets a random position in both x and y coordinates that is below 50.
        //That's if my math is correct
        targetLoc.x = this.target.getPosition().x + (random.nextFloat() * upperboundX + 1);
        targetLoc.y = this.target.getPosition().y + (random.nextFloat() * upperboundY + 1);
        movementTask.setTarget(targetLoc);
        movementTask.update();
    }

    @Override
    public int getPriority() {
        if (animal.getComponent(TamableComponent.class).isTamed()) {
            return priority;
        }
        return -1;
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
        this.owner.getEntity().getEvents().trigger("NoMoreTamedIdle");
    }
}
