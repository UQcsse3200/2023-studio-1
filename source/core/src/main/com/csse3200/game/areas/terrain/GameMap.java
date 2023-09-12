package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.SpaceGameArea;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

/** the GameMap class is used to store and easily access and manage the components related to the game map */
public class GameMap {
    /** The terrainFactory used for creating the game map */
    private final TerrainFactory terrainFactory;
    /** The TiledMap that stores the created map and its TerrainTile instances */
    private final TiledMap tiledMap;
    /** The TerrainComponent used to render the terrain of the map */
    private TerrainComponent terrainComponent;
    /** The logger used to log information for debugging and info */
    private static final Logger logger = LoggerFactory.getLogger(SpaceGameArea.class);

    /**
     * Creates a new GameMap instance, setting the terrainFactory and instantiating a new TiledMap instance.
     * @param terrainFactory the terrainFactory instantiated in the SpaceGameArea.
     */
    public GameMap(TerrainFactory terrainFactory) {
        this.terrainFactory = terrainFactory;
        this.tiledMap = new TiledMap();
    }

    /**
     * Returns the TerrainFactory instance stored in the GameMap class.
     * @return the terrainFactory variable.
     */
    public TerrainFactory getTerrainFactory() {
        return terrainFactory;
    }

    /**
     * Returns the TiledMap instance stored in the GameMap class.
     * @return the tiledMap variable.
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * Returns the TerrainComponent instance stored in the GameMap class.
     * @return the terrainComponent variable.
     * @throws Exception when the TerrainComponent has not been set.
     */
    public TerrainComponent getTerrainComponent() throws Exception {
        if (this.terrainComponent != null) {
            return this.terrainComponent;
        } else {
            throw new Exception("TerrainComponent has not been set in the GameMap class yet");
        }
    }

    /**
     * Sets the TerrainComponent.
     * @param terrainComponent the terrainComponent to be set.
     */
    public void setTerrainComponent(TerrainComponent terrainComponent) {
        this.terrainComponent = terrainComponent;
    }

    /**
     * Returns a GridPoint2 instance that contains the size of the map.
     * @return a copy of the GridPoint2 instance which contains the dimensions of the map.
     */
    public GridPoint2 getMapSize() {
        return terrainFactory.getMapSize().cpy();
    }

    /**
     * Gets the TerrainTile at the specified GridPoint2 position. The x and y values in the GridPoint2 class directly
     * correspond to the tile's position in the map layer.
     *
     * @param gridPoint The GridPoint2 instance representing the target TerrainTile's position.
     * @return TerrainTile instance at the specified position IF the gridpoint is within the bounds of the map, null
     *         otherwise
     */
    public TerrainTile getTile(GridPoint2 gridPoint) {
        TiledMapTileLayer.Cell cell = getCell(gridPoint.x, gridPoint.y);

        if (cell != null) {
            return (TerrainTile) cell.getTile();
        }

        return null;
    }

    /**
     * Gets the TerrainTile at the specified Vector2 position. The x and y float values in the Vector2 class are
     * transformed so that they correspond to the integer positions of the TerrainTile in the map layer.
     *
     * If using the Vector2 position variable from the Entity class, it is important to remember that the vector points
     * to the bottom left of the entity , not the centre of the entity (This is important to know if the entity uses
     * animations since the entity size may be larger than the actual sprite). The Entity class provides the
     * getPosition() and getCentredPosition() methods to help retrieve an appropriate Vector2 instance to use.
     *
     *
     * @param vector The Vector2 instance representing the target TerrainTile's position.
     * @return TerrainTile instance at the specified position IF the vector is within the bounds of the map, null
     *         otherwise
     */
    public TerrainTile getTile(Vector2 vector) {
        return getTile(vectorToTileCoordinates(vector));
    }

    /**
     * Converts a GridPoint2 instance into a Vector2 instance representing the same TerrainTile position on the map
     * layer. The integer values in the GridPoint2 class which directly correspond to the TerrainTile coordinates
     * are transformed for the new Vector2 instance.
     * @param gridPoint2 The GridPoint2 instance being used to create a corresponding Vector2 instance.
     * @return the new Vector2 instance.
     */
    public Vector2 tileCoordinatesToVector(GridPoint2 gridPoint2) {
        float tileSize = this.terrainComponent.getTileSize();
        float x = (float) (gridPoint2.x * tileSize);
        float y = (float) (gridPoint2.y * tileSize);
        return new Vector2(x, y);
    }

    /**
     * Converts a Vector2 instance into a GridPoint2 instance representing the same TerrainTile position on the map
     * layer. The float values in the Vector2 instance are transformed to integer x and y values for the GridPoint2
     * instance.
     * @param vector The Vector2 instance being used to create a corresponding GridPoint2 instance.
     * @return the new GridPoint2 instance.
     */
    public GridPoint2 vectorToTileCoordinates(Vector2 vector) {
        float tileSize = this.terrainComponent.getTileSize();
        int x = (int) Math.floor(vector.x / tileSize);
        int y = (int) Math.floor(vector.y / tileSize);
        return new GridPoint2(x, y);
    }

    /**
     * Returns the Cell object in the TiledMap corresponding to the provided coordinates. Used as a helper function to
     * reduce the complexity of other methods involved with coordinates and vectors.
     * @param x x coordinate (0 -> MAP_SIZE.x -1)
     * @param y y coordinate (0 -> MAP_SIZE.y -1)
     * @throws ArrayIndexOutOfBoundsException exception if the coordinates are not in the bound of the map
     * @return the Cell at the specified position IF the coordinates are within the bounds of the map, null otherwise
     *         (an error message will be logged when coordinates outside the map are passed to the function).
     */
    private TiledMapTileLayer.Cell getCell(int x, int y){
        GridPoint2 mapBounds = this.getMapSize();
        int xMin = 0;
        int xMax = mapBounds.x;
        int yMin = 0;
        int yMax = mapBounds.y;

        if (x < xMin || x >= xMax || y < yMin || y >= yMax) {
            logger.info("The provided coordinates (" + x + "," + y + ") do not fall" + "within the map bounds of x:" +
                    xMin + "-" + xMax + " and y:" + yMin + "-" + yMax);
            return null;
        }

        return ((TiledMapTileLayer) this.tiledMap.getLayers().get(0)).getCell(x, y);
    }

    /**
     * Retrieves a list of grid coordinates representing traversable tiles on the map.
     * @return An ArrayList of GridPoint2 objects containing the coordinates of traversable tiles.
     */
    public ArrayList<GridPoint2> getTraversableTileCoordinates() {
        return traversableTileCoordinatesHelper(true);
    }

    /**
     * Retrieves a list of grid coordinates representing non-traversable tiles on the map.
     * @return An ArrayList of GridPoint2 objects containing the coordinates of non-traversable tiles.
     */
    public ArrayList<GridPoint2> getNonTraversableTileCoordinates() {
        return traversableTileCoordinatesHelper(false);
    }

    /**
     * Helper function to retrieve a list of grid coordinates based on traversability.
     * @param isTraversable A Boolean flag indicating whether to retrieve traversable (true) or non-traversable (false)
     *                      tiles.
     * @return An ArrayList of GridPoint2 objects containing the coordinates of tiles based on the specified
     *         traversability.
     */
    private ArrayList<GridPoint2> traversableTileCoordinatesHelper(Boolean isTraversable) {
        GridPoint2 mapBounds = this.getMapSize();
        int xMax = mapBounds.x;
        int yMax = mapBounds.y;

        ArrayList<GridPoint2> tileCoordinatesList = new ArrayList<>();

        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                GridPoint2 tileGridPoint = new GridPoint2(x, y);
                if (getTile(tileGridPoint).isTraversable() == isTraversable) {
                    tileCoordinatesList.add(tileGridPoint);
                }
            }
        }

        return tileCoordinatesList;
    }
}