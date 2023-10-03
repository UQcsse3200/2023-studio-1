package com.csse3200.game.areas.terrain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Factory for creating game terrains. */
public class TerrainFactory {
    /** GridPoint2 instance representing the size of the map */
    private GridPoint2 mapSize;
    /** The file path for the SpaceGameArea map file */
    protected static final String MAP_PATH = "configs/Map.txt"; //protected for testing
    /** The camera of the map */
    private final OrthographicCamera camera;
    /** The orientation of the camera */
    private final TerrainOrientation orientation;
    /** The tile size constant used for each tile in the TiledMap class */
    protected static final float WORLD_TILE_SIZE = 1f;
    /** HashMap used to map characters in map files to texture regions for the individual tiles */
    private final Map<Character, TextureRegion> charToTextureMap = new HashMap<>();
    /** HashMap used to map characters in map files to file paths containing their textures */
    protected static final Map<Character, String> charToTileImageMap; //protected for testing
    private static final Logger logger = LoggerFactory.getLogger(TerrainFactory.class);

    static {
        Map<Character, String> tempMapA = new HashMap<>();
        tempMapA.put('g', "images/grass_1.png");
        tempMapA.put('G', "images/grass_2.png");
        tempMapA.put('f', "images/grass_3.png");
        tempMapA.put('b', "images/beach_1.png");
        tempMapA.put('B', "images/beach_2.png");
        tempMapA.put('c', "images/beach_3.png");
        tempMapA.put('d', "images/deepWater_1.png");
        tempMapA.put('D', "images/deepWater_2.png");
        tempMapA.put('s', "images/desert_1.png");
        tempMapA.put('C', "images/desert_2.png");
        tempMapA.put('S', "images/desert_3.png");
        tempMapA.put('/', "images/dirt_1.png");
        tempMapA.put('r', "images/dirt_2.png");
        tempMapA.put('R', "images/dirt_3.png");
        tempMapA.put('^', "images/dirtPathTop.png");
        tempMapA.put('<', "images/dirtPathLeft.png");
        tempMapA.put('>', "images/dirtPathRight.png");
        tempMapA.put('v', "images/dirtPathBottom.png");
        tempMapA.put('%', "images/gravel_1.png");
        tempMapA.put('i', "images/ice_1.png");
        tempMapA.put('I', "images/ice_2.png");
        tempMapA.put('l', "images/lava_1.png");
        tempMapA.put('L', "images/lavaGround_1.png");
        tempMapA.put('m', "images/lavaGround_2.png");
        tempMapA.put('M', "images/lavaGround_3.png");
        tempMapA.put('w', "images/water_1.png");
        tempMapA.put('W', "images/water_2.png");
        tempMapA.put('!', "images/water_3.png");
        tempMapA.put('#', "images/flowingWater_1.png");
        tempMapA.put('p', "images/snow_1.png");
        tempMapA.put('P', "images/snow_2.png");
        tempMapA.put('@', "images/snow_3.png");
        tempMapA.put('&', "images/stone_1.png");
        tempMapA.put('+', "images/stonePath_1.png");
        charToTileImageMap = Collections.unmodifiableMap(tempMapA);
    }
    /** Hashmap used to map characters in map files to TerrainCategory types */
    private static final Map<Character, TerrainTile.TerrainCategory> charToTileTypeMap;
    static {
        Map<Character, TerrainTile.TerrainCategory> tempMapB = new HashMap<>();
        tempMapB.put('g', TerrainTile.TerrainCategory.GRASS);
        tempMapB.put('G', TerrainTile.TerrainCategory.GRASS);
        tempMapB.put('f', TerrainTile.TerrainCategory.GRASS);
        tempMapB.put('b', TerrainTile.TerrainCategory.BEACHSAND);
        tempMapB.put('B', TerrainTile.TerrainCategory.BEACHSAND);
        tempMapB.put('c', TerrainTile.TerrainCategory.BEACHSAND);
        tempMapB.put('d', TerrainTile.TerrainCategory.DEEPWATER);
        tempMapB.put('D', TerrainTile.TerrainCategory.DEEPWATER);
        tempMapB.put('s', TerrainTile.TerrainCategory.DESERT);
        tempMapB.put('C', TerrainTile.TerrainCategory.DESERT);
        tempMapB.put('S', TerrainTile.TerrainCategory.DESERT);
        tempMapB.put('/', TerrainTile.TerrainCategory.DIRT);
        tempMapB.put('r', TerrainTile.TerrainCategory.DIRT);
        tempMapB.put('R', TerrainTile.TerrainCategory.DIRT);
        tempMapB.put('^', TerrainTile.TerrainCategory.PATH);
        tempMapB.put('>', TerrainTile.TerrainCategory.PATH);
        tempMapB.put('<', TerrainTile.TerrainCategory.PATH);
        tempMapB.put('v', TerrainTile.TerrainCategory.PATH);
        tempMapB.put('%', TerrainTile.TerrainCategory.GRAVEL);
        tempMapB.put('i', TerrainTile.TerrainCategory.ICE);
        tempMapB.put('I', TerrainTile.TerrainCategory.ICE);
        tempMapB.put('l', TerrainTile.TerrainCategory.LAVA);
        tempMapB.put('L', TerrainTile.TerrainCategory.LAVAGROUND);
        tempMapB.put('m', TerrainTile.TerrainCategory.LAVAGROUND);
        tempMapB.put('M', TerrainTile.TerrainCategory.LAVAGROUND);
        tempMapB.put('w', TerrainTile.TerrainCategory.SHALLOWWATER);
        tempMapB.put('W', TerrainTile.TerrainCategory.SHALLOWWATER);
        tempMapB.put('!', TerrainTile.TerrainCategory.SHALLOWWATER);
        tempMapB.put('#', TerrainTile.TerrainCategory.FLOWINGWATER);
        tempMapB.put('p', TerrainTile.TerrainCategory.SNOW);
        tempMapB.put('P', TerrainTile.TerrainCategory.SNOW);
        tempMapB.put('@', TerrainTile.TerrainCategory.SNOW);
        tempMapB.put('&', TerrainTile.TerrainCategory.ROCK);
        tempMapB.put('+', TerrainTile.TerrainCategory.PATH);
        charToTileTypeMap = Collections.unmodifiableMap(tempMapB);
    }

    /** List of strings storing map texture file paths */
    private static final String[] mapTextures = {
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/beach_1.png",
            "images/beach_2.png",
            "images/beach_3.png",
            "images/deepWater_1.png",
            "images/deepWater_2.png",
            "images/desert_1.png",
            "images/desert_2.png",
            "images/desert_3.png",
            "images/dirt_1.png",
            "images/dirt_2.png",
            "images/dirt_3.png",
            "images/dirtPathTop.png",
            "images/dirtPathRight.png",
            "images/dirtPathBottom.png",
            "images/dirtPathLeft.png",
            "images/gravel_1.png",
            "images/ice_1.png",
            "images/ice_2.png",
            "images/lava_1.png",
            "images/lavaGround_1.png",
            "images/lavaGround_2.png",
            "images/lavaGround_3.png",
            "images/water_1.png",
            "images/water_2.png",
            "images/water_3.png",
            "images/flowingWater_1.png",
            "images/snow_1.png",
            "images/snow_2.png",
            "images/snow_3.png",
            "images/stone_1.png",
            "images/stonePath_1.png"
    };

    /**
     * Create a terrain factory with Orthogonal orientation.
     *
     * @param cameraComponent Camera to render terrains to. Must be orthographic.
     */
    public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
  }

    /**
     * Create a terrain factory
     *
     * @param cameraComponent Camera to render terrains to. Must be orthographic.
     * @param orientation     orientation to render terrain at.
     */
    public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
        this.camera = (OrthographicCamera) cameraComponent.getCamera();
        this.orientation = orientation;
    }

    public static String[] getMapTextures() {
        return mapTextures;
    }

    /**
     * Loads textures into the charToTextureMap based upon the images within the charToTileImageMap and resourceService.
     */
    public void loadTextures(){
        ResourceService resourceService = ServiceLocator.getResourceService();
        for (Map.Entry<Character, String> entry: charToTileImageMap.entrySet()) {
            charToTextureMap.put(entry.getKey(), new TextureRegion(resourceService.getAsset(entry.getValue(),
                    Texture.class)));
        }
    }

    /**
     * Returns a copy of the MAP_SIZE variable.
     *
     * @return GridPoint2 instance representing Map Size.
     */
    public GridPoint2 getMapSize() {
        return mapSize.cpy();
    }

    /**
     * Initiates the create terrain process for the SpaceGameArea.
     *
     * @param tiledMap the TiledMap instance of the game map.
     * @return Terrain component which renders the terrain.
     */
    public TerrainComponent createSpaceGameTerrain(TiledMap tiledMap) {
        return createGameTerrain(tiledMap, MAP_PATH);
    }

    /**
     * Initiates the create terrain process for play testing different maps.
     *
     * @param tiledMap the TiledMap instance of the test game map.
     * @param testMapFilePath the file path of the test map file to be loaded.
     * @return Terrain component which renders the terrain.
     */
    public TerrainComponent createTestTerrain(TiledMap tiledMap, String testMapFilePath) {
        return createGameTerrain(tiledMap, testMapFilePath);
    }

    /**
     * Finishes the create terrain process, loading the map into the TiledMap instance.
     *
     * @param tiledMap the TiledMap instance of the test game map.
     * @param mapFilePath the file path of the map file to be loaded.
     */
    public void loadTiledMap(TiledMap tiledMap, String mapFilePath) {                                       //Duplicate code with below function, should fix sprint 4
        loadTextures();
        GridPoint2 tilePixelSize = new GridPoint2(charToTextureMap.get('g').getRegionWidth(),
                charToTextureMap.get('g').getRegionHeight());
        createGameTiles(tilePixelSize, tiledMap, mapFilePath);
    }

    /**
     * Finishes the create terrain process, initiating the map loading and returning the instantiated TerrainComponent.
     *
     * @param tiledMap the TiledMap instance of the game map.
     * @param mapFilePath the file path of the map file to load.
     * @return Terrain component which renders the terrain.
     */
    private TerrainComponent createGameTerrain(TiledMap tiledMap, String mapFilePath) {
        loadTextures();
        GridPoint2 tilePixelSize = new GridPoint2(charToTextureMap.get('g').getRegionWidth(),
                charToTextureMap.get('g').getRegionHeight());
        createGameTiles(tilePixelSize, tiledMap, mapFilePath);
        TiledMapRenderer renderer = createRenderer(tiledMap, WORLD_TILE_SIZE / tilePixelSize.x);
        return new TerrainComponent(camera, tiledMap, renderer, orientation, WORLD_TILE_SIZE);
    }

    /**
     * Creates the TiledMapRenderer instance used in the TerrainComponent class.
     *
     * @param tiledMap the TiledMap instance of the game map.
     * @param tileScale the calculated tileScale value.
     * @return a TiledMapRenderer instance.
     */
    private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
        return switch (orientation) {
            case ORTHOGONAL -> new OrthogonalTiledMapRenderer(tiledMap, tileScale);
            case ISOMETRIC -> new IsometricTiledMapRenderer(tiledMap, tileScale);
            case HEXAGONAL -> new HexagonalTiledMapRenderer(tiledMap, tileScale);
            default -> null;
        };
    }

    /**
     * This function will be used to create a TiledMap using the file.
     *
     * @param tileSize the size of the tile.
     * @param tiledMap the TiledMap.
     * @param mapFilePath the file path of the map file being loaded into the game.
     */
    private void createGameTiles(GridPoint2 tileSize, TiledMap tiledMap, String mapFilePath) {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(Gdx.files.internal(mapFilePath).read()));
            String line1;
            String line2;
            String line;
            line1 = bf.readLine();
            line2 = bf.readLine();

            setMapSize(line1,line2);

            TiledMapTileLayer layer = new TiledMapTileLayer(mapSize.x, mapSize.y, tileSize.x, tileSize.y);

            int xPos = 0;
            int yPos = mapSize.y - 1;
            // checking for end of file
            for (line = bf.readLine(); line != null; xPos++, line = bf.readLine(), yPos--) {
                for (xPos = line.length() -1; xPos >= 0; xPos--) {
                    GridPoint2 point = new GridPoint2(xPos, yPos);
                    layer.setCell(point.x, point.y, new Cell().setTile(
                            new TerrainTile(charToTextureMap.get(line.charAt(point.x)),
                                    charToTileTypeMap.get(line.charAt(point.x)))));
                }
            }

            tiledMap.getLayers().add(layer);
        } catch (FileNotFoundException e) {
            logger.error("fillTilesWithFile -> File Not Found error!");
        } catch (IOException e) {
            logger.error("fillTilesWithFile -> Readfile error!");
        } catch (Exception e) {
            String log = String.format("fillTilesWithFile -> Testcase error!: %s", e);
            logger.error(log);
        }
    }

    /**
     * This function will be used to set the MAP_SIZE.
     *
     * @param line1 the first line in the file (x parameter).
     * @param line2 the second line in the file (y parameter).
     */
    private void setMapSize(String line1, String line2) {
        int xMapSize = 0;
        int yMapSize = 0;
        // read 2 first lines
        if (isNumeric(line1)) {
            xMapSize = Integer.parseInt(line1);
        } else {
            logger.error("Can't read x -> Incorrect input file!");
        }
        if (isNumeric(line2)) {
            yMapSize = Integer.parseInt(line2);
        } else {
            logger.error("Can't read y -> Incorrect input file!");
        }
        mapSize = new GridPoint2(xMapSize, yMapSize);
    }

    /**
     * This function will be used to check if a string is numeric.
     *
     * @param strNum the string to be checked.
     * @return false if the string is not numeric, else return true.
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}