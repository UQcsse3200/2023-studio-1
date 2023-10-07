package com.csse3200.game.missions.rewards;

import java.util.List;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * An ItemReward class for when a player completes a Quest and receives reward items as a result
 */
public class ItemReward extends Reward {

    /**
     * Array of reward items for the player to receive.
     */
    private final List<Entity> rewardItems;

    /**
     * Creates a new reward with a list of reward items for player to receive on collect() method call.
     * @param rewardItems Array of entities added to the reward.
     */
    public ItemReward(List<Entity> rewardItems) {
        super();
        this.rewardItems = rewardItems;
    }

    /**
     * When called, changes the isCollected variable to true and adds the reward items to the player's inventory.
     */
    @Override
    public void collect() {
        setCollected();

        Entity player = ServiceLocator.getGameArea().getPlayer();
        InventoryComponent inventory = player.getComponent(InventoryComponent.class);
        for (Entity item : rewardItems) {
            ServiceLocator.getGameArea().spawnEntity(item);
            inventory.addItem(item);
        }
    }
}
