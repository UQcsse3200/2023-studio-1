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

/** Factory for creating game terrains. */
public class TerrainFactory {
  /** GridPoint2 instance representing the size of the map */
  private GridPoint2 MAP_SIZE;
  /** The file path for the SpaceGameArea map file */
  protected static final String mapPath = "configs/Map.txt"; //protected for testing
  /** The camera of the map */
  private final OrthographicCamera camera;
  /** The orientation of the camera */
  private final TerrainOrientation orientation;
  /** The tile size constant used for each tile in the TiledMap class */
  private static final float worldTileSize = 1f;
  /** HashMap used to map characters in map files to texture regions for the individual tiles */
  private Map<Character, TextureRegion> charToTextureMap = new HashMap<>();
  /** HashMap used to map characters in map files to file paths containing their textures */
  protected static final Map<Character, String> charToTileImageMap; //protected for testing
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

  /**
   * Create a terrain factory with Orthogonal orientation
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
   * @param orientation     orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Loads textures into the charToTextureMap based upon the images within the charToTileImageMap and resourceService
   */
  public void loadTextures(){
    ResourceService resourceService = ServiceLocator.getResourceService();
    for (Map.Entry<Character, String> entry: charToTileImageMap.entrySet()) {
      charToTextureMap.put(entry.getKey(), new TextureRegion(resourceService.getAsset(entry.getValue(),Texture.class)));
      }
  }

  /**
   * Returns a copy of the MAP_SIZE variable
   *
   * @return GridPoint2 instance representing Map Size
   */
  public GridPoint2 getMapSize() {
    return MAP_SIZE.cpy();
  }

  /**
   * Initiates the create terrain process for the SpaceGameArea.
   *
   * @param tiledMap the TiledMap instance of the game map
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createSpaceGameTerrain(TiledMap tiledMap) {
    return createGameTerrain(tiledMap, mapPath);
  }

  /**
   * Initiates the create terrain process for JUnit testing.                                                HUNTER TO CONTINUE THIS
   *
   * @param tiledMap the TiledMap instance of the test game map
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTestTerrain(TiledMap tiledMap, String testMapFilePath) {
    return createGameTerrain(tiledMap, testMapFilePath);
  }

  /**
   * Finishes the create terrain process, initiating the map loading and returning the instantiated TerrainComponent.
   *
   * @param tiledMap the TiledMap instance of the game map
   * @return Terrain component which renders the terrain
   */
  private TerrainComponent createGameTerrain(TiledMap tiledMap, String mapFilePath) {
    loadTextures();
    GridPoint2 tilePixelSize = new GridPoint2(charToTextureMap.get('g').getRegionWidth(),
            charToTextureMap.get('g').getRegionHeight());
    createGameTiles(tilePixelSize, tiledMap, mapFilePath);
    TiledMapRenderer renderer = createRenderer(tiledMap, worldTileSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, worldTileSize);
  }

  /**
   * Creates the TiledMapRenderer instance used in the TerrainComponent class.
   *
   * @param tiledMap the TiledMap instance of the game map
   * @param tileScale the calculated tileScale value
   * @return a TiledMapRenderer instance
   */
  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case ISOMETRIC:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case HEXAGONAL:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  /**
   * This function will be used to create a TiledMap using the file
   * 
   * @param tileSize the size of the tile
   * @param tiledMap the TiledMap
   */
  private void createGameTiles(GridPoint2 tileSize, TiledMap tiledMap, String mapFilePath) {
      try {
          BufferedReader bf = new BufferedReader(new InputStreamReader(Gdx.files.internal(mapFilePath).read()));
          String line1, line2, line;
          line1 = bf.readLine();
          line2 = bf.readLine();

          setMapSize(line1,line2);

          TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

          // y_pos = 100 and x_pos = 100 lets map generate correctly
          //int x_pos = 0, y_pos = 99;
          int x_pos = 0, y_pos = MAP_SIZE.y - 1;
          // checking for end of file
          for (line = bf.readLine(); line != null; x_pos++, line = bf.readLine(), y_pos--) {
              for (x_pos = line.length() -1; x_pos >= 0; x_pos--) {
                  GridPoint2 point = new GridPoint2(x_pos, y_pos);
                  layer.setCell(point.x, point.y, new Cell().setTile(
                          new TerrainTile(charToTextureMap.get(line.charAt(point.x)),
                                  charToTileTypeMap.get(line.charAt(point.x)))));
              }
          }
          // closing buffer reader object
          bf.close();

          tiledMap.getLayers().add(layer);
      } catch (FileNotFoundException e) {
          System.out.println("fillTilesWithFile -> File Not Found error!");
      } catch (IOException e) {
          System.out.println("fillTilesWithFile -> Readfile error!");
      } catch (Exception e) {
          System.out.println("fillTilesWithFile -> Testcase error!: " + e);
      }
  }

  /**
   * This function will be used to set the MAP_SIZE
   *
   * @param line1 the first line in the file (x parameter)
   * @param line2 the second line in the file (y parameter)
   */
  private void setMapSize(String line1, String line2) {
    int x_MapSize = 0, y_MapSize = 0;
    // read 2 first lines
    if (isNumeric(line1)) {
      x_MapSize = Integer.parseInt(line1);
    } else {
      System.out.println("Can't read x -> Incorrect input file!");
    }
    if (isNumeric(line2)) {
      y_MapSize = Integer.parseInt(line2);
    } else {
      System.out.println("Can't read y -> Incorrect input file!");
    }

    MAP_SIZE = new GridPoint2(x_MapSize, y_MapSize);
  }

  /**
   * This function will be used to check if a string is numeric
   *
   * @param strNum the string to be checked
   * @return false if the string is not numeric, else return true
   */
  public static boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      int value = Integer.parseInt(strNum);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }
}