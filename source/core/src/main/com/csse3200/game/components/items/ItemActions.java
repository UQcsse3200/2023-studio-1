package com.csse3200.game.components.items;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

public class ItemActions extends Component {

    private GameMap map;

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
    public boolean use(Vector2 playerPos, Vector2 mousePos, Entity item, GameMap map) {
        this.map = map;
        ItemComponent type = item.getComponent(ItemComponent.class);
        // Wasn't an item or did not have ItemComponent class
        if (type == null) {
            return false;
        }
        // Add your item here!!!
        boolean resultStatus;
        TerrainTile tile = getTileAtPosition(playerPos, mousePos);
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
    private TerrainTile getTileAtPosition(Vector2 playerPos, Vector2 mousePos) {
        Vector2 pos = getAdjustedPos(playerPos, mousePos);
        return map.getTile(Math.round(pos.x), Math.round(pos.y));
    }

    /**
     * Gets the correct position for the player to interact with based off of the mouse position. Will always return 1 tile to the left, right, 
     * up, down, diagonal left up, diagonal right up, diagonal left down, diagonal right down of the player.
     * @param playerPos the position of the player
     * @param mousePos the position of the mouse
     * @return a vector of the position where the player should hit
     */
    private Vector2 getAdjustedPos(Vector2 playerPos, Vector2 mousePos) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();


        int playerXPos = width/2;
        int playerYPos = height/2;

        int xDelta = 0;
        int yDelta = 0;

        if (playerXPos - 24 > mousePos.x){
            xDelta -= 1;
        } else if (playerXPos + 24 < mousePos.x) {
            xDelta += 1;
        }

        if (playerYPos + 48 < mousePos.y) {
            yDelta -= 1;
        } else if (playerYPos - 48 > mousePos.y) {
            yDelta += 1;
        }
        return new Vector2(playerPos.x + xDelta, playerPos.y + yDelta);
    }

    /**
     * Waters the tile at the given position. 
     * @param tile the tile to be interacted with
     * @param item a reference to a watering can
     * @return if watering was successful return true else return false
     */
    private boolean water(TerrainTile tile, Entity item) {
        boolean tileWaterable = isCropTile(tile.getCropTile());
        if (!tileWaterable) {
            return false;
        }

        // A water amount of 0.5 was recommended by team 7
        tile.getCropTile().getEvents().trigger("water", 0.5);
        item.getComponent(WateringCanLevelComponent.class).incrementLevel(-5);
        return true;
    }

    /**
     * Harvests the tile at the given position
     * @param tile the tile to be interacted with
     * @return if harvesting was successful return true else return false
     */
    private boolean harvest(TerrainTile tile) {
        boolean tileHarvestable = isCropTile(tile.getCropTile());
        if (tileHarvestable) {
            tile.getCropTile().getEvents().trigger("harvest");
            return true;
        } 
        return false;
    }

    /**
     * Shovels the tile at the given position
     * @param tile the tile to be interacted with
     * @return if shoveling was successful return true else return false
     */
    private boolean shovel(TerrainTile tile) {
        if (tile.getCropTile() != null) {
            tile.getCropTile().getEvents().trigger("destroy");
            tile.setUnOccupied();
            tile.setCropTile(null);
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
        TerrainTile tile = getTileAtPosition(playerPos, mousePos);
        if (tile.getCropTile() != null || !tile.isTillable()) {
            return false;
        }
        // Make a new tile
        Vector2 newPos = getAdjustedPos(playerPos, mousePos);

        Entity cropTile = createTerrainEntity(newPos);
        tile.setCropTile(cropTile);
        tile.setOccupied();
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
