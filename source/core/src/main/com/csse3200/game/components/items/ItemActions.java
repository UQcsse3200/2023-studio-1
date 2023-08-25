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
     * @param pos position of the target
     * @param item item to use/ interact with tile
     * @return if interaction with tile was success reutnr true else return false. 
     */
    boolean use(Vector2 pos, Entity item) {
        ItemComponent type = item.getComponent(ItemComponent.class);
        // Wasn't an item or did not have ItemComponent class
        if (type == null) {
            return false;
        }
        // Add your item here!!!
        boolean resultStatus;
        switch (type.getItemType()) {
            case HOE -> {
                resultStatus = hoe(pos);
                return resultStatus;
            }
            case SHOVEL -> {
                resultStatus = shovel(pos);
                return resultStatus;
            }
            case SCYTHE -> {
                resultStatus = harvest(pos);
                return resultStatus;
            }
            case WATERING_CAN -> {
                resultStatus = water(pos);
                return resultStatus;
            }
            default -> {
                return false;
            }
        }
    }


    /**
     * Gets the tile at the given position. else returns null
     * @param pos position on the map being querieds
     * @return Entity of tile at location else returns null
     */
    private Entity getTileAtPosition(Vector2 pos) {
        //TODO Needs to query the map with given position and return a tile
        return null;
    }


    /**
     * Waters the tile at the given position. 
     * @param pos position of the tile being watered
     * @return if watering was successful return true else return false
     */
    private boolean water(Vector2 pos) {
        Entity tile = getTileAtPosition(pos);
        boolean tileWaterable = isCropTile(tile);
        if (!tileWaterable) {
            return false;
        }

        // A water amount of 0.5 was recommended by team 7
        tile.getEvents().trigger("water", 0.5);
        // TODO Need to reduce watering can capacity (idk how or where this should be)
        return true;
    }

    /**
     * Harvests the tile at the given position
     * @param pos position of the tile being harvested
     * @return if harvesting was successful return true else return false
     */
    private boolean harvest(Vector2 pos) {
        Entity tile = getTileAtPosition(pos);
        tile.getEvents().trigger("harvest");
        boolean tileHarvestable = isCropTile(tile);
        if (tileHarvestable) {
            //TODO need to return true on success and harvest here. 
            return true;
        } 
        return false;
    }

    /**
     * Shovels the tile at the given position
     * @param pos position of the tile being shoveled
     * @return if shoveling was successful return true else return false
     */
    private boolean shovel(Vector2 pos) {
        Entity tile = getTileAtPosition(pos);
        //TODO destroy tile at pos. if tile destroyed return true else return false
        return false;
    }


    /**
     * Hoes the tile at the given position
     * @param pos position of the tile being hoed
     * @return if hoeing was successful return true else return false
     */
    private boolean hoe(Vector2 pos) {
        Entity tile = getTileAtPosition(pos);
        if (tile != null) {
            return false;
        }
        // Make a new tile
        tile = createTerrainEntity(pos);

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
