package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;

import java.util.HashMap;

public class GameMap {

    private final TerrainFactory terrainFactory;
    private final TiledMap tiledMap;
    private final EventHandler eventHandler;

    public GameMap(TerrainFactory terrainFactory) {
        this.terrainFactory = terrainFactory;
        this.tiledMap = new TiledMap();
        this.eventHandler = new EventHandler();
    }

    public TerrainFactory getTerrainFactory() {
        return terrainFactory;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Get the Cell in TiledMap, use for cell interaction
     * such as get and set tile, rotating Cell, and accessing the contents of the cell
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return the Cell
     */
    private TiledMapTileLayer.Cell getCell(int x, int y) {
        return ((TiledMapTileLayer) this.tiledMap.getLayers().get(0)).getCell(x, y);
    }

    /**
     *                  NEED TO MAKE THIS FUNCTION MORE VERSATILE
     *   such as using position on screen etc. instead of x and y coords which only correlate with the 2d array of cells
     * @param x
     * @param y
     * @return
     */
    public TerrainTile getTile (int x, int y) {
        return (TerrainTile) getCell(x, y).getTile();
    }

    /**
     * Returns the terrain category at a specified tile
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return the TerrainCategory of the specified tile
     */
    public TerrainTile.TerrainCategory getTileTerrainCategory(int x, int y) {
        return this.getTile(x, y).getTerrainCategory();
    }

    /**
     * Sets the terrainCategory at the specified tile to the provided terrainCategory
     * @param terrainCategory terrinCategory to set the TerrainTile to
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     */
    public void setTileTerrainCategory(TerrainTile.TerrainCategory terrainCategory, int x, int y) {
        this.getTile(x, y).setTerrainCategory(terrainCategory);
    }

    /**
     * Returns whether the specified terrain is traversable
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return true if the tile is traversable and false if not
     */
    public boolean isTileTraversable(int x, int y) {
        return this.getTile(x, y).isTraversable();
    }

    /**
     * Returns whether the specified terrain is occupied
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return true if the tile is occupied and false if not
     */
    public boolean isTileOccupied(int x, int y) {
        return this.getTile(x, y).isOccupied();
    }

    /**
     * Sets the specified tile as being occupied
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     */
    public void setTileOccupied(int x, int y) {
        this.getTile(x, y).setOccupied();
    }

    /**
     * Sets the specified tile as being unoccupied
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     */
    public void setTileUnoccupied(int x, int y) {
        this.getTile(x, y).setUnOccupied();
    }

    /**
     * Returns whether the specified terrain is tillable
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return true if the tile is tillable and false if not
     */
    public boolean isTileTillable(int x, int y) {
        return this.getTile(x, y).isTillable();
    }
}


