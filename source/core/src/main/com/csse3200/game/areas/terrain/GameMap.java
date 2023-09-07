package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;

/** the GameMap class is used to store and easily access and manage the components related to the game map */
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
     * Returns the size of the map
     * @return a copy of the GridPoint2 instance which contains the dimensions of the map
     */
    public GridPoint2 getMapSize() {
        return terrainFactory.getMapSize();
    }

    /**
     * Gets the TerrainTile at the specified GridPoint2 position. The x and y values in the GridPoint2 class directly
     * correspond to the tile's position in the map layer.
     *
     * @param gridPoint The GridPoint2 instance representing the target TerrainTile's position.
     * @return TerrainTile instance at the specified position.
     */
    public TerrainTile getTile(GridPoint2 gridPoint) {
        return (TerrainTile) getCell(gridPoint.x, gridPoint.y).getTile();
    }

    /**
     * Gets the TerrainTile at the specified Vector2 position. The x and y float values in the Vector2 class are
     * transformed so that they correspond to the integer positions of the TerrainTile in the map layer.
     *
     * This transformation involves multiplying the float values by 2 to account for the 0.5f tile size, and then           WILL HAVE TO UPDATE if below comment is implemented
     * flooring the result and casting it to an integer.
     * @param vector The Vector2 instance representing the target TerrainTile's position.
     * @return TerrainTile instance at the specified position.
     */
    public TerrainTile getTile(Vector2 vector) {
        int x = (int) Math.floor((vector.x+1)*2);
        int y = (int) Math.floor((vector.y+1)*2 - 1); // SHOULD ADJUST these lines so they instead divide by the tile size from the terrainComponent
        return (TerrainTile) getCell(x, y).getTile();

        //return getTile(vectorToTileCoordinates(vector)); // need to test if this works
        // if above does work, the code above it can be removed
    }

    /**
     * Converts a Vector2 instance into a GridPoint2 instance representing the same TerrainTile position on the map
     * layer. The float values in the Vector2 instance are transformed to integer x and y values for the GridPoint2
     * instance.
     * @param vector The Vector2 instance being used to create a corresponding GridPoint2 instance.
     * @return the new GridPoint2 instance.
     */
    public GridPoint2 vectorToTileCoordinates(Vector2 vector) {
        int x = (int) Math.floor(vector.x / 0.5);
        int y = (int) Math.floor(vector.y / 0.5);                 // SHOULD ADJUST these lines so they instead divide by the tile size from the terrainComponent
        return new GridPoint2(x, y);
    }

    /**
     * Converts a GridPoint2 instance into a Vector2 instance representing the same TerrainTile position on the map
     * layer. The integer values in the GridPoint2 class which directly correspond to the TerrainTile coordinates
     * are transformed for the new Vector2 instance.
     * @param gridPoint2 The GridPoint2 instance being used to create a corresponding Vector2 instance.
     * @return the new Vector2 instance.
     */
    public Vector2 tileCoordinatesToVector(GridPoint2 gridPoint2) {
        float x = (float) (gridPoint2.x * 0.5);
        float y = (float) (gridPoint2.y * 0.5);            // SHOULD ADJUST these lines so they multiply by the tile size from the terrainComponent
        return new Vector2(x, y);
    }

    /**
     * Returns the Cell object in the TiledMap corresponding to the provided coordinates. Used as a helper function to
     * reduce the complexity of other methods involved with coordinates and vectors.
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @return the Cell at the specified position
     */
    private TiledMapTileLayer.Cell getCell(int x, int y) {
        // ADD A CHECK FOR INTS BEING OUT OF BOUNDS OF MAP                                                      HUNTER DO THIS
        //throw new IndexOutOfBoundsException("Bad Input: Coordinate position out of bounds");
        return ((TiledMapTileLayer) this.tiledMap.getLayers().get(0)).getCell(x, y);
    }
}