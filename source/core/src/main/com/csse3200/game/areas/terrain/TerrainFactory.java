package com.csse3200.game.areas.terrain;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int GRASS1_TILE_COUNT = 30;
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private static final Map<Character,String> charToTileImageMap;
  static {
    Map<Character,String> tempMapA=new HashMap<>();
    tempMapA.put('g',"images/grass_1.png");
    tempMapA.put('G',"images/grass_2.png");
    tempMapA.put('f',"images/grass_3.png");
    tempMapA.put('b',"images/beach_1.png");
    tempMapA.put('B',"images/beach_2.png");
    tempMapA.put('c',"images/beach_3.png");
    tempMapA.put('d',"images/deepWater_1.png");
    tempMapA.put('D',"images/deepWater_2.png");
    tempMapA.put('s',"images/desert_1.png");
    tempMapA.put('C',"images/desert_2.png");
    tempMapA.put('S',"images/desert_3.png");
    tempMapA.put('/',"images/dirt_1.png");
    tempMapA.put('r',"images/dirt_2.png");
    tempMapA.put('R',"images/dirt_3.png");
    tempMapA.put('^',"images/dirtPathTop.png");
    tempMapA.put('>',"images/dirtPathRight.png");
    tempMapA.put('<',"images/dirtPathLeft.png");
    tempMapA.put('v',"images/dirtPathBottom.png");
    tempMapA.put('%',"images/gravel.png");
    tempMapA.put('i',"images/ice_1.png");
    tempMapA.put('I',"images/ice_2.png");
    tempMapA.put('l',"images/lava_1.png");
    tempMapA.put('L',"images/lavaGround_1.png");
    tempMapA.put('m',"images/lavaGround_2.png");
    tempMapA.put('M',"images/lavaGround_3.png");
    tempMapA.put('w',"images/water_1.png");
    tempMapA.put('W',"images/water_2.png");
    tempMapA.put('!',"images/water_3.png");
    tempMapA.put('#',"images/flowingWater_1.png");
    tempMapA.put('p',"images/snow_1.png");
    tempMapA.put('P',"images/snow_2.png");
    tempMapA.put('@',"images/snow_3.png");
    tempMapA.put('&',"images/stone_1.png");
    tempMapA.put('+',"images/stonePath_1.png");
    charToTileImageMap = Collections.unmodifiableMap(tempMapA);
  }
  private static final Map<Character,TerrainTile.TerrainCategory> charToTileTypeMap;
  static {
    Map<Character,TerrainTile.TerrainCategory> tempMapB = new HashMap<>();
    tempMapB.put('g', TerrainTile.TerrainCategory.GRASS);
    tempMapB.put('G',TerrainTile.TerrainCategory.GRASS);
    tempMapB.put('f',TerrainTile.TerrainCategory.GRASS);
    tempMapB.put('b', TerrainTile.TerrainCategory.BEACHSAND);
    tempMapB.put('B',TerrainTile.TerrainCategory.BEACHSAND);
    tempMapB.put('c',TerrainTile.TerrainCategory.BEACHSAND);
    tempMapB.put('d', TerrainTile.TerrainCategory.DEEPWATER);
    tempMapB.put('D',TerrainTile.TerrainCategory.DEEPWATER);
    tempMapB.put('s', TerrainTile.TerrainCategory.DESERT);
    tempMapB.put('C',TerrainTile.TerrainCategory.DESERT);
    tempMapB.put('S',TerrainTile.TerrainCategory.DESERT);
    tempMapB.put('/', TerrainTile.TerrainCategory.DIRT);
    tempMapB.put('r',TerrainTile.TerrainCategory.DIRT);
    tempMapB.put('R',TerrainTile.TerrainCategory.DIRT);
    tempMapB.put('^', TerrainTile.TerrainCategory.PATH);
    tempMapB.put('>',TerrainTile.TerrainCategory.PATH);
    tempMapB.put('<',TerrainTile.TerrainCategory.PATH);
    tempMapB.put('v',TerrainTile.TerrainCategory.PATH);
    tempMapB.put('%', TerrainTile.TerrainCategory.GRAVEL);
    tempMapB.put('i', TerrainTile.TerrainCategory.ICE);
    tempMapB.put('I',TerrainTile.TerrainCategory.ICE);
    tempMapB.put('l', TerrainTile.TerrainCategory.LAVA);
    tempMapB.put('L', TerrainTile.TerrainCategory.LAVAGROUND);
    tempMapB.put('m',TerrainTile.TerrainCategory.LAVAGROUND);
    tempMapB.put('M',TerrainTile.TerrainCategory.LAVAGROUND);
    tempMapB.put('w', TerrainTile.TerrainCategory.SHALLOWWATER);
    tempMapB.put('W',TerrainTile.TerrainCategory.SHALLOWWATER);
    tempMapB.put('!',TerrainTile.TerrainCategory.SHALLOWWATER);
    tempMapB.put('#',TerrainTile.TerrainCategory.FLOWINGWATER);
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
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {                      // WILL NEED TO REVAMP THIS FUNCTION
    ResourceService resourceService = ServiceLocator.getResourceService();
     ArrayList<TextureRegion> TRList = new ArrayList<TextureRegion>();
    switch (terrainType) {
            case FOREST_DEMO:

        TextureRegion orthoGrass =
            new TextureRegion(resourceService.getAsset("images/grass_1.png", Texture.class));
        TextureRegion orthoTuft =
            new TextureRegion(resourceService.getAsset("images/grass_2.png", Texture.class));
        TextureRegion orthoRocks =
            new TextureRegion(resourceService.getAsset("images/grass_3.png", Texture.class));
            TRList.add(orthoGrass);
            TRList.add(orthoTuft);
            TRList.add(orthoRocks);
        return createForestDemoTerrain(0.5f, TRList);
      /*case FOREST_DEMO_ISO:
        TextureRegion isoGrass =
            new TextureRegion(resourceService.getAsset("images/iso_grass_1.png", Texture.class));
        TextureRegion isoTuft =
            new TextureRegion(resourceService.getAsset("images/iso_grass_2.png", Texture.class));
        TextureRegion isoRocks =
            new TextureRegion(resourceService.getAsset("images/iso_grass_3.png", Texture.class));
        return createForestDemoTerrain(1f, isoGrass, isoTuft, isoRocks);
      case FOREST_DEMO_HEX:
        TextureRegion hexGrass =
            new TextureRegion(resourceService.getAsset("images/hex_grass_1.png", Texture.class));
        TextureRegion hexTuft =
            new TextureRegion(resourceService.getAsset("images/hex_grass_2.png", Texture.class));
        TextureRegion hexRocks =
            new TextureRegion(resourceService.getAsset("images/hex_grass_3.png", Texture.class));
        return createForestDemoTerrain(1f, hexGrass, hexTuft, hexRocks);*/
      default:
        return null;
    }
  }

  private TerrainComponent createForestDemoTerrain(
      float tileWorldSize, ArrayList<TextureRegion> TRList) {
    GridPoint2 tilePixelSize = new GridPoint2(TRList.get(0).getRegionWidth(), TRList.get(0).getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, TRList);
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
   * Get the Cell in TiledMap, use for cell interaction
   * such as get and set tile, rotating Cell
   * @param tiledMap the tile map that have the Cell
   * @param x x coordinate (0 -> MAP_SIZE.x -1)
   * @param y y coordinate (0 -> MAP_SIZE.y -1)
   * @return the Cell
   */
  private Cell getCell(TiledMap tiledMap , int x, int y) {
    return ((TiledMapTileLayer)tiledMap.getLayers().get(0)).getCell(x, y);
  }


  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, ArrayList<TextureRegion> TRList) {
    TiledMap tiledMap = new TiledMap();
    ArrayList<TerrainTile> TTlist = new ArrayList<TerrainTile>();
    TerrainTile grassTile1 = new TerrainTile(TRList.get(0), TerrainTile.TerrainCategory.GRASS); // TerrainTile.TerrainCategory.GRASS does not change anything currently
    TerrainTile grassTile2 = new TerrainTile(TRList.get(1), TerrainTile.TerrainCategory.GRASS); // TerrainTile.TerrainCategory.GRASS does not change anything currently
    TerrainTile grassTile3 = new TerrainTile(TRList.get(2), TerrainTile.TerrainCategory.GRASS); // TerrainTile.TerrainCategory.GRASS does not change anything currently

    TTlist.add(grassTile1);
    TTlist.add(grassTile2);
    TTlist.add(grassTile3);

    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    //fillTiles(layer, MAP_SIZE, TTlist.get(0));

    try {
      fillTilesWithFile(layer, MAP_SIZE, TTlist, "configs/titleMap.txt");
    } catch (FileNotFoundException e) {
      System.out.println("fillTilesWithFile -> File Not Found error!");
    } catch (IOException e) {
      System.out.println("fillTilesWithFile -> Readfile error!");
    } catch (Exception e) {
      System.out.println("fillTilesWithFile -> Testcase error!: " + e);
    }

    //fillTilesAtRandom(layer, MAP_SIZE, grassTile3, GRASS3_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  /**
   * Will be used to create a TiledMap for the SpaceGame.
   *
   * Will probably need some variables from our planned MapClass (Unless if this instantiates the MapClass)
   *
   * @return
   */
  private TiledMap createSpaceGameTiles(GridPoint2 tileSize) { // Will probably need more variables than this
    TiledMap tiledMap = new TiledMap(); // MapClass will likely be an extension of TiledMap
    // MapClass will either instantiate the grid, or the grid is set after instantiation
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);
    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTilesWithFile(TiledMapTileLayer layer, GridPoint2 mapSize, ArrayList<TerrainTile> TTList, String path) throws IOException,
          FileNotFoundException {
    // open file to read and read each character
      BufferedReader bf = new BufferedReader(new FileReader(path));
      String line;
      int x_pos = 0, y_pos = 0;
        // checking for end of file
    for (line = bf.readLine(); line != null; x_pos++, line = bf.readLine(), y_pos++) {
        for (x_pos = 0; x_pos < line.length(); x_pos++) {
          //Cell cell = layer.getCell(x_pos, y_pos);
          GridPoint2 point = new GridPoint2(x_pos, y_pos);
          Cell cell = new Cell();
          switch (line.charAt(point.x)) {
            case 'x':
              cell.setTile(TTList.get(0));
              System.out.print("x");
              break;
            case 'y':
              cell.setTile(TTList.get(1));
              System.out.print("y");
              break;
            default:
              cell.setTile(TTList.get(2));
              System.out.print("e");
              break;
          }
          System.out.println("");
          layer.setCell(point.x, point.y, cell);
        }
    }
      // closing bufferreader object
      bf.close();
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX,

    DESERT_DEMO
  }
}
