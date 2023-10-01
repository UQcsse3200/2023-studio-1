package com.csse3200.game.components.placeables;

import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlaceableFactory;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;

public class PlaceableEvents extends Component {

    @Override
    public void create() {
        entity.getEvents().addListener("destroy", this::destroyPlaceable);
    }

    void destroyPlaceable(TerrainTile tile) {
        Entity placedItem = tile.getOccupant();
        // Check if the placeable is a chest and if there is items in that chest
        InventoryComponent chestInventory = placedItem.getComponent(InventoryComponent.class);
        if (chestInventory != null){
            if (chestInventory.getInventory().size() >= 1) {
                return;
            }
        }
        // Notify any placeable entities to destroy their connections
        entity.getEvents().trigger("destroyConnections");
        // Removes references from the tile to the placeable and set it as unoccupied
        tile.removeOccupant();
        // Spawn in the item that corresponds to the removed placeable
        Entity droppedItem = FactoryService.getItemFactories().get(placedItem.getType().toString()).get();
        ServiceLocator.getGameArea().spawnEntity(droppedItem);
        droppedItem.setPosition(placedItem.getPosition());
        // Remove the placeable from the game area
        ServiceLocator.getGameArea().removeEntity(placedItem);
    }
}
