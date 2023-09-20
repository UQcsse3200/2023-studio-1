package com.csse3200.game.components.tasks;

import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;


public class TamedFollowTask extends FollowTask {
    /**
     * More will be done for this class
     */

    //The animal who will have this task.
    Entity animal = this.owner.getEntity();

    private String favouriteFood;

    public TamedFollowTask(Entity target, int priority, float viewDistance, float maxFollowDistance, float
            stoppingDistance, String favouriteFood) {
        super(target, priority, viewDistance, maxFollowDistance,stoppingDistance);
        this.favouriteFood = favouriteFood;
    }

    @Override
    public void start() {
        //animal needs to be tamed first and player needs to be holding correct item
        //for status to be active.
        if (this.owner.getEntity().getComponent(TamableComponent.class).isTamed()) {
            status = Status.ACTIVE;
            setMovementTask(new MovementTask(getTarget().getCenterPosition(), (float) 1.5));
            getMovementTask().create(owner);
            getMovementTask().start();
            this.owner.getEntity().getEvents().trigger("TamedfollowStart");
        }
    }

    /**
     * Stops the Tamed follow task and the associated movement task, and triggers the "TamedfollowStop" event.
     */
    @Override
    public void stop() {
        super.stop();
        this.owner.getEntity().getEvents().trigger("TamedfollowStop");
    }

    /**
     * Updates the Tamed follow task by updating the movement task and stopping it if the entity is too close to the target.
     * or player is no longer holding favor
     */
    @Override
    public void update() {
        Entity playerCurItem = getTarget().getComponent(InventoryComponent.class).getHeldItem();
        //if status is active, check if player is holding onto animal's favourite food, else status becomes inactive
        if ((playerCurItem.getComponent(ItemComponent.class) == null) ||
                !(playerCurItem.getComponent(ItemComponent.class).getItemName().equals(favouriteFood)) ||
                getDistanceToTarget() <= getStoppingDistance()) {
            stop();
        } else {
            getMovementTask().setTarget(getTarget().getCenterPosition());
            getMovementTask().update();
            if (getMovementTask().getStatus() != Status.ACTIVE) {
                this.owner.getEntity().getEvents().trigger("TamedfollowStart");
                getMovementTask().start();
            }
        }
    }
}
