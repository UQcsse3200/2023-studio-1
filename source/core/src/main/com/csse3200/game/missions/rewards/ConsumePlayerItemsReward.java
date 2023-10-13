package com.csse3200.game.missions.rewards;

import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.Map;

/**
 * A ConsumePlayerItemsReward class for when the player completes a {@link com.csse3200.game.missions.quests.Quest}
 * which should result in a certain number of inventory items to be removed from the inventory.
 */
public class ConsumePlayerItemsReward extends Reward {

    /**
     * A {@link Map} mapping item names to item quantities, representing the names of items to remove, and the quantity
     * of that item to remove.
     */
    private final Map<String, Integer> itemsToRemove;

    /**
     * Creates a {@link ConsumePlayerItemsReward} reward, which, when collected, removes a specified number of items
     * from the player's inventory.
     *
     * @param toRemove A {@link Map} mapping item names to item quantities, representing the names of items to remove,
     *                 and the quantity of that item to remove.
     */
    public ConsumePlayerItemsReward(Map<String, Integer> toRemove) {
        this.itemsToRemove = toRemove;
    }

    /**
     * When called, changes the isCollected variable to true and removes the specified items to the player's inventory.
     */
    @Override
    public void collect() {
        setCollected();

        Entity player = ServiceLocator.getGameArea().getPlayer();
        InventoryComponent inventory = player.getComponent(InventoryComponent.class);
        if (inventory == null) {
            return;
        }
        
        for (Map.Entry<String, Integer> itemQuantity : itemsToRemove.entrySet()) {
            for (int i = 0; i < itemQuantity.getValue(); i++) {
                inventory.removeItem(itemQuantity.getKey());
            }
        }
    }

}
