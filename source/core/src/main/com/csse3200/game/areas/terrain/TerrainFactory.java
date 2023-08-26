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
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int GRASS1_TILE_COUNT = 30;
  private static final String path = "configs/testMap.txt"; // change this path if u can't open the file
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
    tempMapA.put('%',"images/gravel_1.png");
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
   * @return Terrain component which renders the terrain
   */
  // public TerrainComponent createTerrain(TerrainType terrainType) (old function declaration)
  public TerrainComponent createTerrain(TiledMap tiledMap) {
    ResourceService resourceService = ServiceLocator.getResourceService();
     ArrayList<TextureRegion> TRList = new ArrayList<TextureRegion>();
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('g'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('G'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('f'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('b'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('B'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('c'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('d'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('D'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('s'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('C'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('S'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('/'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('r'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('R'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('^'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('>'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('v'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('%'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('i'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('I'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('l'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('L'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('m'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('M'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('w'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('W'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('!'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('#'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('p'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('P'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('@'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('&'), Texture.class)));
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('+'), Texture.class)));
      return createGameTerrain(0.5f, TRList, tiledMap);
  }

  private TerrainComponent createGameTerrain(float tileWorldSize, ArrayList<TextureRegion> TRList, TiledMap tiledMap) {
    GridPoint2 tilePixelSize = new GridPoint2(TRList.get(0).getRegionWidth(), TRList.get(0).getRegionHeight());
    createGameTiles(tilePixelSize, TRList, tiledMap);
    //TiledMap tiledMap = createGameTiles(tilePixelSize, TRList);
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
   * SHOULD CREATE javadoc for what this does
   *
   * @param tileSize
   * @param TRList
   * @return
   */
  //private TiledMap createGameTiles(GridPoint2 tileSize, ArrayList<TextureRegion> TRList) {
  private void createGameTiles(GridPoint2 tileSize, ArrayList<TextureRegion> TRList, TiledMap tiledMap) {
    //TiledMap tiledMap = new TiledMap();
    ArrayList<TerrainTile> TTlist = new ArrayList<TerrainTile>();
    
    TTlist.add(new TerrainTile(TRList.get(0), charToTileTypeMap.get('g')));
    TTlist.add(new TerrainTile(TRList.get(1), charToTileTypeMap.get('G')));
    TTlist.add(new TerrainTile(TRList.get(2), charToTileTypeMap.get('f')));
    TTlist.add(new TerrainTile(TRList.get(3), charToTileTypeMap.get('b')));
    TTlist.add(new TerrainTile(TRList.get(4), charToTileTypeMap.get('B')));
    TTlist.add(new TerrainTile(TRList.get(5), charToTileTypeMap.get('c')));
    TTlist.add(new TerrainTile(TRList.get(6), charToTileTypeMap.get('d')));
    TTlist.add(new TerrainTile(TRList.get(7), charToTileTypeMap.get('D')));
    TTlist.add(new TerrainTile(TRList.get(8), charToTileTypeMap.get('s')));
    TTlist.add(new TerrainTile(TRList.get(9), charToTileTypeMap.get('C')));
    TTlist.add(new TerrainTile(TRList.get(10), charToTileTypeMap.get('S')));
    TTlist.add(new TerrainTile(TRList.get(11), charToTileTypeMap.get('/')));
    TTlist.add(new TerrainTile(TRList.get(12), charToTileTypeMap.get('r')));
    TTlist.add(new TerrainTile(TRList.get(13), charToTileTypeMap.get('R')));
    TTlist.add(new TerrainTile(TRList.get(14), charToTileTypeMap.get('^')));
    TTlist.add(new TerrainTile(TRList.get(15), charToTileTypeMap.get('>')));
    TTlist.add(new TerrainTile(TRList.get(16), charToTileTypeMap.get('v')));
    TTlist.add(new TerrainTile(TRList.get(17), charToTileTypeMap.get('%')));
    TTlist.add(new TerrainTile(TRList.get(18), charToTileTypeMap.get('i')));
    TTlist.add(new TerrainTile(TRList.get(19), charToTileTypeMap.get('I')));
    TTlist.add(new TerrainTile(TRList.get(20), charToTileTypeMap.get('l')));
    TTlist.add(new TerrainTile(TRList.get(21), charToTileTypeMap.get('L')));
    TTlist.add(new TerrainTile(TRList.get(22), charToTileTypeMap.get('m')));
    TTlist.add(new TerrainTile(TRList.get(23), charToTileTypeMap.get('M')));
    TTlist.add(new TerrainTile(TRList.get(24), charToTileTypeMap.get('w')));
    TTlist.add(new TerrainTile(TRList.get(25), charToTileTypeMap.get('W')));
    TTlist.add(new TerrainTile(TRList.get(26), charToTileTypeMap.get('!')));
    TTlist.add(new TerrainTile(TRList.get(27), charToTileTypeMap.get('#')));
    TTlist.add(new TerrainTile(TRList.get(28), charToTileTypeMap.get('p')));
    TTlist.add(new TerrainTile(TRList.get(29), charToTileTypeMap.get('P')));
    TTlist.add(new TerrainTile(TRList.get(30), charToTileTypeMap.get('@')));
    TTlist.add(new TerrainTile(TRList.get(31), charToTileTypeMap.get('&')));
    TTlist.add(new TerrainTile(TRList.get(32), charToTileTypeMap.get('+')));

    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    //fillTiles(layer, MAP_SIZE, TTlist.get(0));

    try {
      fillTilesWithFile(layer, MAP_SIZE, TTlist, path);
    } catch (FileNotFoundException e) {
      System.out.println("fillTilesWithFile -> File Not Found error!");
    } catch (IOException e) {
      System.out.println("fillTilesWithFile -> Readfile error!");
    } catch (Exception e) {
      System.out.println("fillTilesWithFile -> Testcase error!: " + e);
    }

    //fillTilesAtRandom(layer, MAP_SIZE, grassTile3, GRASS3_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    //return tiledMap;
  }

  /*
  private static void fillTilesAtRandom(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }
  */

  /**
   * This function will be used to create a cell for the TiledMap
   * by using switch case to determine which tile to use
   * @param point position of the cell
   * @param line the line of the file that is being read
   * @param TTList the list of TerrainTiles
   * @return the cell
   */
  private static Cell cellCreator(GridPoint2 point, String line, ArrayList<TerrainTile> TTList) {
    Cell cell = new Cell();
          switch (line.charAt(point.x)) {
            case 'g':
              cell.setTile(TTList.get(0));
              break;
            case 'G':
              cell.setTile(TTList.get(1));
              break;
            case 'f':
              cell.setTile(TTList.get(2));
              break;
            case 'b':
              cell.setTile(TTList.get(3));
              break;
            case 'B':
              cell.setTile(TTList.get(4));
              break;
            case 'c':
              cell.setTile(TTList.get(5));
              break;
            case 'd':
              cell.setTile(TTList.get(6));
              break;
            case 'D':
              cell.setTile(TTList.get(7));
              break;
            case 's':
              cell.setTile(TTList.get(8));
              break;
            case 'C':
              cell.setTile(TTList.get(9));
              break;
            case 'S':
              cell.setTile(TTList.get(10));
              break;
            case '/':
              cell.setTile(TTList.get(11));
              break;
            case 'r':
              cell.setTile(TTList.get(12));
              break;
            case 'R':
              cell.setTile(TTList.get(13));
              break;
            case '^':
              cell.setTile(TTList.get(14));
              break;
            case '>':
              cell.setTile(TTList.get(15));
              break;
            case 'v':
              cell.setTile(TTList.get(16));
              break;
            case '%':
              cell.setTile(TTList.get(17));
              break;
            case 'i':
              cell.setTile(TTList.get(18));
              break;
            case 'I':
              cell.setTile(TTList.get(19));
              break;
            case 'l':
              cell.setTile(TTList.get(20));
              break;
            case 'L':
              cell.setTile(TTList.get(21));
              break;
            case 'm':
              cell.setTile(TTList.get(22));
              break;
            case 'M':
              cell.setTile(TTList.get(23));
              break;
            case 'w':
              cell.setTile(TTList.get(24));
              break;
            case 'W':
              cell.setTile(TTList.get(25));
              break;
            case '!':
              cell.setTile(TTList.get(26));
              break;
            case '#':
              cell.setTile(TTList.get(27));
              break;
            case 'p':
              cell.setTile(TTList.get(28));
              break;
            case 'P':
              cell.setTile(TTList.get(29));
              break;
            case '@':
              cell.setTile(TTList.get(30));
              break;
            case '&':
              cell.setTile(TTList.get(31));
              break;
            case '+':
              cell.setTile(TTList.get(32));
              break;
            default:
              cell.setTile(null);
              break;
          }
    return cell;
  }

  /**
   * This function will be used to fill the TiledMap with tiles
   * by reading a file
   * 
   * @param layer the layer of the TiledMap
   * @param mapSize the size of the map
   * @param TTList the list of TerrainTiles
   * @param path the path of the file
   * @throws IOException when there is an error reading the file
   * @throws FileNotFoundException when file is not found
   */
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
          layer.setCell(point.x, point.y, cellCreator(point, line, TTList));
        }
    }
      // closing buffer reader object
      bf.close();
  }

  /* 
  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }
  */

  /**
   * This enum should contain the different terrains in your game, e.g. forest, cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the same level in 3
   * different orientations.
   */
  public enum TerrainType { // should be able to be deleted at some point
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX,

    DESERT_DEMO
  }
}
