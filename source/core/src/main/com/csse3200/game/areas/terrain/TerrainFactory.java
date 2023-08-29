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
  private static final GridPoint2 MAP_SIZE = new GridPoint2(100, 100);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int GRASS1_TILE_COUNT = 30;
  private static final String path1 = "source/core/assets/configs/Map.txt"; // change this path if u can't open the file
  private static final String path2 = "configs/Map.txt";
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private static final Map<Character,String> charToTileImageMap;
  static {
    Map<Character,String> tempMapA=new HashMap<>();
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
    tempMapA.put('v',"images/dirtPathBottom.png");
    tempMapA.put('<',"images/dirtPathLeft.png");
    tempMapA.put('>',"images/dirtPathRight.png");
    tempMapA.put('^',"images/dirtPathTop.png");
    tempMapA.put('#',"images/flowingWater_1.png");
    tempMapA.put('%',"images/gravel_1.png");
    tempMapA.put('g',"images/grass_1.png");
    tempMapA.put('G',"images/grass_2.png");
    tempMapA.put('f',"images/grass_3.png");
    tempMapA.put('i',"images/ice_1.png");
    tempMapA.put('I',"images/ice_2.png");
    tempMapA.put('l',"images/lava_1.png");
    tempMapA.put('L',"images/lavaground_1.png");
    tempMapA.put('m',"images/lavaground_2.png");
    tempMapA.put('M',"images/lavaground_3.png");
    tempMapA.put('w',"images/water_1.png");
    tempMapA.put('W',"images/water_2.png");
    tempMapA.put('!',"images/water_3.png");
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

  public GridPoint2 getMapSize() {
    return MAP_SIZE.cpy();
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TiledMap tiledMap) {
    ResourceService resourceService = ServiceLocator.getResourceService();
     ArrayList<TextureRegion> TRList = new ArrayList<TextureRegion>();
     //this should be implemented instead in the future and then rest of code edited accordingly
     /*for (String tile: charToTileImageMap.values()) {
       TRList.add(new TextureRegion(resourceService.getAsset(tile,Texture.class)));
     }
*/
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
      TRList.add(new TextureRegion(resourceService.getAsset(charToTileImageMap.get('<'), Texture.class)));
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

  private void createGameTiles(GridPoint2 tileSize, ArrayList<TextureRegion> TRList, TiledMap tiledMap) {
    //TiledMap tiledMap = new TiledMap();
    ArrayList<TerrainTile> TTlist = new ArrayList<TerrainTile>();

    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    try {
      fillTilesWithFile(layer, MAP_SIZE, TRList, path1, path2);
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
   * @param TRList the list of Texture Region
   * @return the cell
   */
  private static Cell cellCreator(GridPoint2 point, String line, ArrayList<TextureRegion> TRList) {
    Cell cell = new Cell();
          switch (line.charAt(point.x)) {
            case 'g':
              cell.setTile(new TerrainTile(TRList.get(0), charToTileTypeMap.get('g')));
              break;
            case 'G':
              cell.setTile(new TerrainTile(TRList.get(1), charToTileTypeMap.get('G')));
              break;
            case 'f':
              cell.setTile(new TerrainTile(TRList.get(2), charToTileTypeMap.get('f')));
              break;
            case 'b':
              cell.setTile(new TerrainTile(TRList.get(3), charToTileTypeMap.get('b')));
              break;
            case 'B':
              cell.setTile(new TerrainTile(TRList.get(4), charToTileTypeMap.get('B')));
              break;
            case 'c':
              cell.setTile(new TerrainTile(TRList.get(5), charToTileTypeMap.get('c')));
              break;
            case 'd':
              cell.setTile(new TerrainTile(TRList.get(6), charToTileTypeMap.get('d')));
              break;
            case 'D':
              cell.setTile(new TerrainTile(TRList.get(7), charToTileTypeMap.get('D')));
              break;
            case 's':
              cell.setTile(new TerrainTile(TRList.get(8), charToTileTypeMap.get('s')));
              break;
            case 'C':
              cell.setTile(new TerrainTile(TRList.get(9), charToTileTypeMap.get('C')));
              break;
            case 'S':
              cell.setTile(new TerrainTile(TRList.get(10), charToTileTypeMap.get('S')));
              break;
            case '/':
              cell.setTile(new TerrainTile(TRList.get(11), charToTileTypeMap.get('/')));
              break;
            case 'r':
              cell.setTile(new TerrainTile(TRList.get(12), charToTileTypeMap.get('r')));
              break;
            case 'R':
              cell.setTile(new TerrainTile(TRList.get(13), charToTileTypeMap.get('R')));
              break;
            case '^':
              cell.setTile(new TerrainTile(TRList.get(14), charToTileTypeMap.get('^')));
              break;
            case '<':
              cell.setTile(new TerrainTile(TRList.get(15), charToTileTypeMap.get('>')));
              break;
            case '>':
              cell.setTile(new TerrainTile(TRList.get(16), charToTileTypeMap.get('>')));
              break;
            case 'v':
              cell.setTile(new TerrainTile(TRList.get(17), charToTileTypeMap.get('v')));
              break;
            case '%':
              cell.setTile(new TerrainTile(TRList.get(18), charToTileTypeMap.get('%')));
              break;
            case 'i':
              cell.setTile(new TerrainTile(TRList.get(19), charToTileTypeMap.get('i')));
              break;
            case 'I':
              cell.setTile(new TerrainTile(TRList.get(20), charToTileTypeMap.get('I')));
              break;
            case 'l':
              cell.setTile(new TerrainTile(TRList.get(21), charToTileTypeMap.get('l')));
              break;
            case 'L':
              cell.setTile(new TerrainTile(TRList.get(22), charToTileTypeMap.get('L')));
              break;
            case 'm':
              cell.setTile(new TerrainTile(TRList.get(23), charToTileTypeMap.get('m')));
              break;
            case 'M':
              cell.setTile(new TerrainTile(TRList.get(24), charToTileTypeMap.get('M')));
              break;
            case 'w':
              cell.setTile(new TerrainTile(TRList.get(25), charToTileTypeMap.get('w')));
              break;
            case 'W':
              cell.setTile(new TerrainTile(TRList.get(26), charToTileTypeMap.get('W')));
              break;
            case '!':
              cell.setTile(new TerrainTile(TRList.get(27), charToTileTypeMap.get('!')));
              break;
            case '#':
              cell.setTile(new TerrainTile(TRList.get(28), charToTileTypeMap.get('#')));
              break;
            case 'p':
              cell.setTile(new TerrainTile(TRList.get(29), charToTileTypeMap.get('p')));
              break;
            case 'P':
              cell.setTile(new TerrainTile(TRList.get(30), charToTileTypeMap.get('P')));
              break;
            case '@':
              cell.setTile(new TerrainTile(TRList.get(31), charToTileTypeMap.get('@')));
              break;
            case '&':
              cell.setTile(new TerrainTile(TRList.get(32), charToTileTypeMap.get('&')));
              break;
            case '+':
              cell.setTile(new TerrainTile(TRList.get(33), charToTileTypeMap.get('+')));
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
   * @param TRList the list of TextureRegion
   * @param path1 the path of the file
   * @throws IOException when there is an error reading the file
   * @throws FileNotFoundException when file is not found
   */
  private static void fillTilesWithFile(TiledMapTileLayer layer, GridPoint2 mapSize, ArrayList<TextureRegion> TRList,
                                        String path1, String path2) throws IOException,
          FileNotFoundException {
    // open file to read and read each character
    BufferedReader bf;
    //needs to be improved, just done because different file paths required when run from desktop launcher vs gradle
    try {
      bf = new BufferedReader(new FileReader(path1));
    } catch (FileNotFoundException e) {
      bf = new BufferedReader(new FileReader(path2));
    }
      String line;
    //y_pos = 100 and x_pos = 100 lets map generate correctly
      int x_pos = 0, y_pos = 100;
        // checking for end of file
    for (line = bf.readLine(); line != null; x_pos++, line = bf.readLine(), y_pos--) {
        for (x_pos = line.length() - 1; x_pos > 0; x_pos--) {
          //Cell cell = layer.getCell(x_pos, y_pos); // uncomment this if u want to update instead of replace
          GridPoint2 point = new GridPoint2(x_pos, y_pos);
          layer.setCell(point.x, point.y, cellCreator(point, line, TRList));
        }
    }
      // closing buffer reader object
      bf.close();
  }
}
