package com.csse3200.game.missions.rewards;

import com.csse3200.game.components.ClueComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class ClueReward extends ItemReward {

    private final Entity clueItem;

    public ClueReward(Entity clueItem) {
        super(List.of(clueItem));
        this.clueItem = clueItem;
    }

    @Override
    public void collect() {
        super.collect();

        ClueComponent clueComponent = clueItem.getComponent(ClueComponent.class);

        if (clueComponent != null) {
            List<String> possibleLocations = clueComponent.getPossibleLocations();

            if (!possibleLocations.isEmpty()) {
                // Generate debris for the first location in the list
                String location = possibleLocations.get(0);
                generateTileAndDebris(location);

                // Remove the first location from the list
                possibleLocations.remove(0);
            }
        }
    }

    private void generateTileAndDebris(String location) {
        // Assuming you have a method generateDebris that creates a single piece of debris
      //  Entity debris = generateDebris(location);

        // Assuming you have a way to add the debris entity to the game
      //  ServiceLocator.getGameArea().addEntity(debris);
    }

//    private Entity generateDebris(String location) {
//        // Implement logic to generate a single piece of debris based on location
//        // Create the relevant entity for debris and return it
//        return
//    }
}
