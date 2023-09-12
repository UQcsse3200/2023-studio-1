package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainCropTileFactory;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.function.Function;


/** SpaceGameArea is the area used for the initial game version */
public class SpaceGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(SpaceGameArea.class);

  private static final int NUM_GHOSTS = 5;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final GridPoint2 QUESTGIVER_SPAWN = new GridPoint2(20, 20);
  private static final GridPoint2 QUESTGIVERIND_SPAWN = new GridPoint2(20, 24);
  private static final GridPoint2 TRACTOR_SPAWN = new GridPoint2(15, 15);

  private static final GridPoint2 TOOL_SPAWN = new GridPoint2(15, 10);// temp!!!
  private static final GridPoint2 TOOL_SPAWN2 = new GridPoint2(15, 15);// temp!!!
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
          "images/tool_shovel.png",
          "images/egg.png",
          "images/milk.png",

          "images/tool_hoe.png",
          "images/tool_scythe.png",
          "images/tool_watering_can.png",
          "images/animals/chicken.png",
          "images/animals/cow.png",
          "images/cropTile.png",
          "images/cropTile_fertilised.png",
          "images/watered_cropTile.png",
          "images/watered_cropTile_fertilised.png",
          "images/overwatered_cropTile.png",
          "images/overwatered_cropTile_fertilised.png",

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
          "images/stonePath_1.png",
          "images/tractor.png",
          "images/fertiliser.png",
          "images/plants/aloevera_seedbag.png",
          "images/plants/atomic_algae_seed.png",
          "images/plants/cosmic_cob_seed.png",
          "images/plants/deadly_nightshade_seed.png",
          "images/plants/hammer_plant_seed.png",
          "images/plants/horticultural_heater_seed.png",
          "images/plants/space_snapper_seed.png",
          "images/plants/tobacco_seed.png",
          "images/plants/Corn.png",
          "images/invisible_sprite.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/player.atlas", "images/ghostKing.atlas",
          "images/animals/chicken.atlas", "images/animals/cow.atlas", "images/tractor.atlas",
          "images/animals/astrolotl.atlas", "images/animals/oxygen_eater.atlas", "images/questgiver.atlas",
          "images/missionStatus.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg", "sounds/car-horn-6408.mp3"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;
  private final GameMap gameMap;

  private Entity player;
  private final ClimateController climateController;
  private Entity tractor;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public SpaceGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
    gameMap = new GameMap(terrainFactory);
    climateController = new ClimateController();
    ServiceLocator.registerGameArea(this);
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();
    spawnInvisibleObstacle();// spawn invisible obstacle on the non-traversable area of the map

    spawnCrop(); // temp

    player = spawnPlayer();
    player.getComponent(PlayerActions.class).setGameMap(gameMap);

    // TODO:

    tractor = spawnTractor();
    spawnQuestgiver();
    spawnChickens();
    spawnCows();
    spawnAstrolotl();
    spawnOxygenEater();

    spawnTool(ItemType.WATERING_CAN);
    spawnTool(ItemType.SHOVEL);
    spawnTool(ItemType.SCYTHE);
    spawnTool(ItemType.HOE);
    spawnTool(ItemType.FERTILISER);
    spawnTool(ItemType.SEED);
    spawnTool(ItemType.FOOD);

    //playMusic();
  }

  public Entity getPlayer() {
    return player;
  }
  public ClimateController getClimateController() {
    return climateController;
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(this.gameMap.getTiledMap());
    this.gameMap.setTerrainComponent(terrain);
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

    /**
     * Spawn invisible obstacle on the non-traversable area of the map
     */
   private void spawnInvisibleObstacle() {
     ArrayList<GridPoint2> Non_Traversable_Obs = this.gameMap.getNonTraversableTileCoordinates();
     for (int i = 0; i < Non_Traversable_Obs.size(); i++) {
       GridPoint2 Pos = Non_Traversable_Obs.get(i);
       Entity invisible_obs = ObstacleFactory.createInvisibleObstacle();
       spawnEntityAt(invisible_obs, Pos, true, false);
     }
   }

  private Entity spawnCrop() {
    GridPoint2 pos = new GridPoint2(10, 11);
    Entity newPlayer = TerrainCropTileFactory.createTerrainEntity(0,0);
    spawnEntityAt(newPlayer, pos, true, true);
    Entity plant = FactoryService.getPlantFactories().get("Cosmic Cob").apply(newPlayer.getComponent(CropTileComponent.class));
    ServiceLocator.getEntityService().register(plant);
    newPlayer.getComponent(CropTileComponent.class).setPlant(plant);
    return newPlayer;
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnQuestgiver() {
    Entity newQuestgiver = QuestgiverFactory.createQuestgiver();
    spawnEntityAt(newQuestgiver, QUESTGIVER_SPAWN, true, true);

    Entity newQuestgiverIndicator = QuestgiverFactory.createQuestgiverIndicator(newQuestgiver);
    spawnEntityAt(newQuestgiverIndicator, QUESTGIVERIND_SPAWN, true, true);
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
      case FERTILISER:
        newTool = ItemFactory.createFertiliser();
        spawnEntityAt(newTool, randomPos, true, true);
        break;
      case SEED:
        newTool = ItemFactory.createAloeVeraSeed();
        spawnEntityAt(newTool, randomPos, true, true);
        break;
      case FOOD:
        newTool = ItemFactory.createCowFood();
        spawnEntityAt(newTool, randomPos, true, true);
        break;
    }
  }

  /**
   * Spawns the Tractor Entity be calling upon it's factory
   *
   * @return a reference to the tractor
   */
  private Entity spawnTractor() {
    Entity newTractor = TractorFactory.createTractor(player);
    spawnEntityAt(newTractor, TRACTOR_SPAWN, true, true);
    return newTractor;
  }

  private void spawnChickens() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 10; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity chicken = NPCFactory.createChicken(player);
      spawnEntityAt(chicken, randomPos, true, true);
    }
  }


  private void spawnCows() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 5; i++) {
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

  private void spawnOxygenEater() {
    GridPoint2 minPos = new GridPoint2(2, 2);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 5; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity oxygenEater = NPCFactory.createOxygenEater(player);
      spawnEntityAt(oxygenEater, randomPos, true, true);
    }
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
   * Returns the tractor entity
   */
  public Entity getTractor() {
    return tractor;
  }


  /**
   * Returns the game map
   */
  public GameMap getMap() {
    return gameMap;
  }
}
