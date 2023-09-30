package com.csse3200.game.missions.rewards;

import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * An PlaceableReward class for when a player completes a Quest and receives reward placeable as a result
 */
public class PlaceableReward extends Reward {
    private final List<Entity> rewardPlaceables; // The list of placeables to be rewarded

    /**
     * Constructor for the PlaceableReward class
     * @param rewardPlaceables The list of placeables to be rewarded
     */
    public PlaceableReward(List<Entity> rewardPlaceables) {
        super();
        this.rewardPlaceables = rewardPlaceables;
    }

    /**
     * Collects the reward by adding the reward placeables to the player's inventory
     */
    @Override
    public void collect() {
        Entity player = ServiceLocator.getGameArea().getPlayer();
        InventoryComponent inventory = player.getComponent(InventoryComponent.class);
        for (Entity placeable : rewardPlaceables) {
            inventory.addItem(placeable);
        }
    }
}
