package com.csse3200.game.areas.terrain;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
      fillTilesWithFile(layer, MAP_SIZE, TTlist,"source/core/assets/configs/titleMap.txt");
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

  private static void fillTilesWithFile(TiledMapTileLayer layer, GridPoint2 mapSize, ArrayList<TerrainTile> TTList, String path) throws IOException {
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
