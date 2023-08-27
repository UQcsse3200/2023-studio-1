package com.csse3200.game.components.items;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

public class ItemActions extends Component {

    @Override
    public void create() {
        // Just in case we need constructor for later
    }

    /**
     * Uses the item at the given position
     * @param playerPos the position of the player
     * @param mousePos the position of the mouse
     * @param item item to use/ interact with tile
     * @return if interaction with tile was success return true else return false.
     */
    public boolean use(Vector2 playerPos, Vector2 mousePos, Entity item) {
        ItemComponent type = item.getComponent(ItemComponent.class);
        // Wasn't an item or did not have ItemComponent class
        if (type == null) {
            return false;
        }
        // Add your item here!!!
        boolean resultStatus;
        Entity tile = getTileAtPosition(playerPos, mousePos);
        switch (type.getItemType()) {
            case HOE -> {
                resultStatus = hoe(playerPos, mousePos);
                return resultStatus;
            }
            case SHOVEL -> {
                resultStatus = shovel(tile);
                return resultStatus;
            }
            case SCYTHE -> {
                resultStatus = harvest(tile);
                return resultStatus;
            }
            case WATERING_CAN -> {
                resultStatus = water(tile, item);
                return resultStatus;
            }
            default -> {
                return false;
            }
        }
    }



    /**
     * Gets the tile at the given position. else returns null
     * @param playerPos the position of the player
     * @param mousePos the position of the mouse
     * @return Entity of tile at location else returns null
     */
    private Entity getTileAtPosition(Vector2 playerPos, Vector2 mousePos) {
        //TODO Needs to query the map with given position and return a tile
        Vector2 pos = getAdjustedPos(playerPos, mousePos);
        return null;
    }

    /**
     * Gets the correct position for the player to interact with
     * @param playerPos the position of the player
     * @param mousePos the position of the mouse
     * @return a vector of the position where the player should hit
     */
    private Vector2 getAdjustedPos(Vector2 playerPos, Vector2 mousePos) {
        //TODO the if statement stuff to get the correct position
        return playerPos;
    }


    /**
     * Waters the tile at the given position. 
     * @param tile the tile to be interacted with
     * @param item a reference to a watering can
     * @return if watering was successful return true else return false
     */
    private boolean water(Entity tile, Entity item) {
        boolean tileWaterable = isCropTile(tile);
        if (!tileWaterable) {
            return false;
        }

        // A water amount of 0.5 was recommended by team 7
        tile.getEvents().trigger("water", 0.5);
        item.getComponent(WateringCanLevelComponent.class).incrementLevel(-5);
        return true;
    }

    /**
     * Harvests the tile at the given position
     * @param tile the tile to be interacted with
     * @return if harvesting was successful return true else return false
     */
    private boolean harvest(Entity tile) {
        tile.getEvents().trigger("harvest");
        boolean tileHarvestable = isCropTile(tile);
        if (tileHarvestable) {
            //TODO need to return true on success and harvest here.
            // Harvest is done above this?
            return true;
        } 
        return false;
    }

    /**
     * Shovels the tile at the given position
     * @param tile the tile to be interacted with
     * @return if shoveling was successful return true else return false
     */
    private boolean shovel(Entity tile) {
        //TODO destroy tile at pos. if tile destroyed return true else return false
        if (tile != null) {
            tile.getEvents().trigger("destroy");
            return true;
        }
        return false;
    }


    /**
     * Hoes the tile at the given position
     * @param playerPos the position of the player
     * @param mousePos the position of the mouse
     * @return if hoeing was successful return true else return false
     */
    private boolean hoe(Vector2 playerPos, Vector2 mousePos) {
        Entity tile = getTileAtPosition(playerPos, mousePos);
        if (tile != null) {
            return false;
        }
        // Make a new tile
        tile = createTerrainEntity(getAdjustedPos(playerPos, mousePos));

        //TODO Add tile to the map for further actions
        
        return true;
    }


    /**
     * Checks if the tile is harvestable by checking if it is a CropTile
     * @param tile tile being checked
     * @return true if tile is harvestable else false
     */
    private boolean isCropTile(Entity tile) {
      if (tile.getComponent(CropTileComponent.class) != null) {
        return true;
      }
      return false;
    }
}
