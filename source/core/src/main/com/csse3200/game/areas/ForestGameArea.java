package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.Tool;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_GHOSTS = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
    "images/tree.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/hex_grass_1.png",
    "images/hex_grass_2.png",
    "images/hex_grass_3.png",
    "images/iso_grass_1.png",
    "images/iso_grass_2.png",
    "images/iso_grass_3.png",
    "images/tractor.png",
    "images/tool_shovel.png",
    "images/tool_hoe.png",
    "images/tool_scythe.png",
    "images/tool_watering_can.png",
    "images/animals/chicken.png",
    "images/animals/cow.png",
    "images/cropTile.png",
    "images/animal/astrolotl.png",
  };

  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/player.atlas", "images/ghostKing.atlas",
          "images/animals/chicken.atlas", "images/animals/cow.atlas", "images/tractor.atlas",
                  "images/animals/astrolotl.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  private Entity player;
  private Entity tractor;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
    ServiceLocator.registerGameArea(this);
  }


  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();
    //spawnTrees();   // trees are annoying
    //spawnGhosts();    // so r ghosts
    //spawnGhostKing();
    player = spawnPlayer();
    spawnChickens();
    spawnCows();
    spawnAstrolotl();

    // temp tool spawners
    spawnTool(ItemType.WATERING_CAN);
    spawnTool(ItemType.SHOVEL);
    spawnTool(ItemType.SCYTHE);
    spawnTool(ItemType.HOE);

    //playMusic();

    tractor = spawnTractor();
  }

  public Entity getPlayer() {
    return player;
  }

  private void displayUI() {
    Entity ui = new Entity();
    // Added the name of the game
    ui.addComponent(new GameAreaDisplay("Gardens of the Galaxy"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(new TiledMap()); // Added new TiledMap to prevent error, WILL NOT WORK IF YOU RUN ForestGameArea, run SpaceGameArea instead
    // terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0),
            false,
            false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y),
            false,
            false);
    // Bottom
    spawnEntityAt(
        ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);

  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  /**
   * Spawns the Tractor Entity be calling upon it's factory
   *
   * @return a reference to the tractor
   */
  private Entity spawnTractor() {
    Entity newTractor = TractorFactory.createTractor(player);
    spawnEntityAt(newTractor, PLAYER_SPAWN, true, true);
    return newTractor;
  }


  private void spawnTool(ItemType tool) {
    Entity newTool;
    // create a random places for tool to spawn
    GridPoint2 minPos = new GridPoint2(5, 5);
    GridPoint2 maxPos = new GridPoint2(20, 20);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);

    switch (tool) {
      case HOE:
        newTool = ItemFactory.createHoe();
        spawnEntityAt(newTool, randomPos, true, true);
        break;
      case SHOVEL:
        newTool = ItemFactory.createShovel();
        spawnEntityAt(newTool, randomPos, true, true);
        break;
      case SCYTHE:
        newTool = ItemFactory.createScythe();
        spawnEntityAt(newTool, randomPos, true, true);
        break;
      case WATERING_CAN:
        newTool = ItemFactory.createWateringcan();
        spawnEntityAt(newTool, randomPos, true, true);
        break;
    }
  }

  private void spawnChickens() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 2; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity chicken = NPCFactory.createChicken(player);
      spawnEntityAt(chicken, randomPos, true, true);
    }
  }

  private void spawnCows() {
    GridPoint2 minPos = new GridPoint2(2, 2);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 2; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity cow = NPCFactory.createCow(player);
      spawnEntityAt(cow, randomPos, true, true);
    }
  }

  private void spawnAstrolotl() {
    GridPoint2 minPos = new GridPoint2(2, 2);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 1; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity astrolotl = NPCFactory.createAstrolotl(player);
      spawnEntityAt(astrolotl, randomPos, true, true);
    }
  }

  private void spawnGhosts() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_GHOSTS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity ghost = NPCFactory.createGhost(player);
      spawnEntityAt(ghost, randomPos, true, true);
    }
  }

  private void spawnGhostKing() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
    Entity ghostKing = NPCFactory.createGhostKing(player);
    spawnEntityAt(ghostKing, randomPos, true, true);
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }

  /**
   * Does not set the camera to the Entity instead sets a camera variable inside of scripts
   * to do that later
   */
  public Entity getTractor() {
    return tractor;
  }
}
