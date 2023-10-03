package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;


public class TamedFollowTask extends FollowTask {
    /**
     * still needs to be added onto the chicken.......
     */

    private String favouriteFood;

    public TamedFollowTask(Entity target, int priority, float viewDistance, float maxFollowDistance, float
            stoppingDistance, String favouriteFood, Vector2 speed) {
        super(target, priority, viewDistance, maxFollowDistance,stoppingDistance, speed);
        this.favouriteFood = favouriteFood;
    }

    @Override
    protected int getInactivePriority() {
        Entity playerCurItem = getTarget().getComponent(InventoryComponent.class).getHeldItem();
        if (playerCurItem == null || playerCurItem.getComponent(ItemComponent.class) == null ||
                !playerCurItem.getComponent(ItemComponent.class).getItemName().equals(this.favouriteFood) ||
                !this.owner.getEntity().getComponent(TamableComponent.class).isTamed()) {
            return -1;
        }
        return super.getInactivePriority();
    }

    @Override
    protected int getActivePriority() {
        Entity playerCurItem = getTarget().getComponent(InventoryComponent.class).getHeldItem();
        if (playerCurItem.getComponent(ItemComponent.class) == null ||
                !playerCurItem.getComponent(ItemComponent.class).getItemName().equals(favouriteFood)) {
            return -1;
        }
        return super.getActivePriority();
    }
}
