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
import org.slf4j.Marker;

/** Factory for creating game terrains. */
public class TerrainFactory {

  private static Logger logger = LoggerFactory.getLogger(TerrainFactory.class);
  private static GridPoint2 mapSize = new GridPoint2(1000, 1000); // this will be updated later in the code
  //protected for testing
  protected static final String mapPath = "configs/Map.txt";
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  private static final float WorldTileSize = 1f;
  private Map<Character, TextureRegion> charToTextureMap = new HashMap<>();
  //protected for testing
  protected static final Map<Character, String> charToTileImageMap;
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
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
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
  public GridPoint2 getMapSize() {
    return mapSize.cpy();
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory.
   * This can be extended
   * to add additional game terrains.
   *
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TiledMap tiledMap) {
    loadTextures();
    return createGameTerrain(WorldTileSize, tiledMap);
  }

  private TerrainComponent createGameTerrain(float tileWorldSize, TiledMap tiledMap) {
    GridPoint2 tilePixelSize = new GridPoint2(charToTextureMap.get('g').getRegionWidth(), charToTextureMap.get('g').getRegionHeight());
    createGameTiles(tilePixelSize, tiledMap);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

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
   * This function will be used to update the MAP_SIZE
   * 
   * @param line1 the first line in the file (x parameter)
   * @param line2 the second line in the file (y parameter)
   */
  private void updateMapSize(String line1, String line2) {
    int x_MapSize = 0, y_MapSize = 0;
    // read 2 first lines
    if (isNumeric(line1)) {
      x_MapSize = Integer.parseInt(line1);
    } else {
      logger.error("Can't read x -> Incorrect input file!");
    }
    if (isNumeric(line2)) {
      y_MapSize = Integer.parseInt(line2);
    } else {
      logger.error("Can't read y -> Incorrect input file!");
    }
    // update MAP_SIZE using existing function
    mapSize.add(- mapSize.x, - mapSize.y);
    mapSize.add(x_MapSize, y_MapSize);
  }

  /**
   * This function will be used to create a TiledMap using the file
   * 
   * @param tileSize the size of the tile
   * @param tiledMap the TiledMap
   */
  private void createGameTiles(GridPoint2 tileSize, TiledMap tiledMap) {
      try {
          BufferedReader bf = new BufferedReader(new InputStreamReader(Gdx.files.internal(mapPath).read()));
          String line1, line2, line;
          line1 = bf.readLine();
          line2 = bf.readLine();
          updateMapSize(line1,line2);
          TiledMapTileLayer layer = new TiledMapTileLayer(mapSize.x, mapSize.y, tileSize.x, tileSize.y);
          // y_pos = 100 and x_pos = 100 lets map generate correctly
          int y_pos = Integer.parseInt(line2) -1;
          // checking for end of file
          for (line = bf.readLine(); line != null; line = bf.readLine(), y_pos--) {
              for (int x_pos = line.length() -1; x_pos >= 0; x_pos--) {
                  // Cell cell = layer.getCell(x_pos, y_pos); // uncomment this if u want to
                  // update instead of replace
                  GridPoint2 point = new GridPoint2(x_pos, y_pos);
                  //this line replaces the entire old cell creator function
                  layer.setCell(point.x, point.y, new Cell().setTile(
                          new TerrainTile(charToTextureMap.get(line.charAt(point.x)),
                                  charToTileTypeMap.get(line.charAt(point.x)))));
              }
          }
          // closing buffer reader object
          bf.close();

          tiledMap.getLayers().add(layer);
      } catch (FileNotFoundException e) {
          logger.error("fillTilesWithFile -> File Not Found error!");
      } catch (IOException e) {
          logger.error("fillTilesWithFile -> Readfile error!");
      } catch (Exception e) {
          logger.error("fillTilesWithFile -> Testcase error!: " + e);
      }
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
      Integer.parseInt(strNum);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }
}
