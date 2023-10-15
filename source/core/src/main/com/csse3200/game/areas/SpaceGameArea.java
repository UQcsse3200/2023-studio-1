package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.*;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntitySpawner;
import com.csse3200.game.entities.EntitiesSpawner;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.missions.quests.QuestFactory;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.*;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/** SpaceGameArea is the area used for the initial game version */
public class SpaceGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(SpaceGameArea.class);

  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(24, 86);
  private static final GridPoint2 QUESTGIVER_SPAWN = new GridPoint2(42, 87);
  private static final GridPoint2 SHIP_SPAWN = new GridPoint2(20,85);

  private static final float WALL_WIDTH = 0.1f;
  private EntitiesSpawner hostileSpawner;

  private static final String[] texturePaths = {
          "images/tree.png",
          "images/ghost_king.png",
          "images/ghost_1.png",
          "images/hex_grass_1.png",
          "images/hex_grass_2.png",
          "images/hex_grass_3.png",
          "images/iso_grass_1.png",
          "images/iso_grass_2.png",
          "images/iso_grass_3.png",
          "images/tool_shovel.png",
          "images/fishing_rod.png",
          "images/animals/egg.png",
          "images/animals/milk.png",
          "images/animals/golden_egg.png",
          "images/animals/beef.png",
          "images/lava_eel.png",
          "images/salmon.png",
          "images/golden_fish.png",
          "images/animals/chicken_meat.png",

          "images/tool_hoe.png",
          "images/tool_scythe.png",
          "images/tool_sword.png",
          "images/tool_gun.png",
          "images/tool_watering_can.png",
          "images/animals/chicken.png",
          "images/animals/cow.png",
          "images/cropTile.png",
          "images/cropTile_fertilised.png",
          "images/watered_cropTile.png",
          "images/watered_cropTile_fertilised.png",
          "images/overwatered_cropTile.png",
          "images/overwatered_cropTile_fertilised.png",
          "images/Temp-Chest.png",
          "images/bin.png",
          "images/GOD_IS_game_ver.png",

          "images/tractor.png",
          "images/fertiliser.png",

          "images/plants/cosmic_cob/1_seedling.png",
          "images/plants/cosmic_cob/2_sprout.png",
          "images/plants/cosmic_cob/3_juvenile.png",
          "images/plants/cosmic_cob/4_adult.png",
          "images/plants/cosmic_cob/5_decaying.png",
          "images/plants/cosmic_cob/6_dead.png",
          "images/plants/cosmic_cob/item_drop.png",
          "images/plants/cosmic_cob/seedbag.png",

          "images/plants/aloe_vera/1_seedling.png",
          "images/plants/aloe_vera/2_sprout.png",
          "images/plants/aloe_vera/3_juvenile.png",
          "images/plants/aloe_vera/4_adult.png",
          "images/plants/aloe_vera/5_decaying.png",
          "images/plants/aloe_vera/6_dead.png",
          "images/plants/aloe_vera/item_drop.png",
          "images/plants/aloe_vera/seedbag.png",

          "images/plants/hammer_plant/1_seedling.png",
          "images/plants/hammer_plant/2_sprout.png",
          "images/plants/hammer_plant/3_juvenile.png",
          "images/plants/hammer_plant/4_adult.png",
          "images/plants/hammer_plant/5_decaying.png",
          "images/plants/hammer_plant/6_dead.png",
          "images/plants/hammer_plant/item_drop.png",
          "images/plants/hammer_plant/seedbag.png",

          "images/plants/space_snapper/1_seedling.png",
          "images/plants/space_snapper/2_sprout.png",
          "images/plants/space_snapper/3_juvenile.png",
          "images/plants/space_snapper/4_adult.png",
          "images/plants/space_snapper/5_decaying.png",
          "images/plants/space_snapper/6_dead.png",
          "images/plants/space_snapper/item_drop.png",
          "images/plants/space_snapper/seedbag.png",

          "images/plants/atomic_algae/1_seedling.png",
          "images/plants/atomic_algae/2_sprout.png",
          "images/plants/atomic_algae/3_juvenile.png",
          "images/plants/atomic_algae/4_adult.png",
          "images/plants/atomic_algae/5_decaying.png",
          "images/plants/atomic_algae/6_dead.png",
          "images/plants/atomic_algae/item_drop.png",
          "images/plants/atomic_algae/seedbag.png",

          "images/plants/deadly_nightshade/1_seedling.png",
          "images/plants/deadly_nightshade/2_sprout.png",
          "images/plants/deadly_nightshade/3_juvenile.png",
          "images/plants/deadly_nightshade/4_adult.png",
          "images/plants/deadly_nightshade/5_decaying.png",
          "images/plants/deadly_nightshade/6_dead.png",
          "images/plants/deadly_nightshade/item_drop.png",
          "images/plants/deadly_nightshade/seedbag.png",

          "images/plants/misc/aloe_vera_seed.png",
          "images/plants/misc/cosmic_cob_seed.png",
          "images/plants/misc/deadly_nightshade_seed.png",
          "images/plants/misc/hammer_plant_seed.png",
          "images/plants/misc/space_snapper_seed.png",


          "images/invisible_sprite.png",

          "images/Player_Hunger/hunger_bar_outline.png",
          "images/Player_Hunger/hunger_bar_fill.png",

          "images/plants/misc/atomic_algae_seed.png",
          "images/invisible_sprite.png",

          "images/progress-bar/part1day1.png",
          "images/progress-bar/part1day2.png",
          "images/progress-bar/part1day3.png",
          "images/progress-bar/part1day4.png",
          "images/progress-bar/part1day5.png",
          "images/progress-bar/part2day1.png",
          "images/progress-bar/part2day2.png",
          "images/progress-bar/part2day3.png",
          "images/progress-bar/part2day4.png",
          "images/progress-bar/part2day5.png",
          "images/progress-bar/part2day6.png",
          "images/progress-bar/part2day7.png",
          "images/progress-bar/part2day8.png",
          "images/progress-bar/part2day9.png",
          "images/progress-bar/part2day10.png",
          "images/progress-bar/part3day1.png",
          "images/progress-bar/part3day2.png",
          "images/progress-bar/part3day3.png",
          "images/progress-bar/part3day4.png",
          "images/progress-bar/part3day5.png",
          "images/progress-bar/part3day6.png",
          "images/progress-bar/part3day7.png",
          "images/progress-bar/part3day8.png",
          "images/progress-bar/part3day9.png",
          "images/progress-bar/part3day10.png",
          "images/progress-bar/part3day11.png",
          "images/progress-bar/part3day12.png",
          "images/progress-bar/part3day13.png",
          "images/progress-bar/part3day14.png",
          "images/progress-bar/part3day15.png",

          "images/Player_Hunger/hunger_bar_outline.png",
          "images/Player_Hunger/hunger_bar_fill.png",
          "images/projectiles/oxygen_eater_projectile.png",

          "images/yellowSquare.png",
          "images/yellowCircle.png",

          /* placeable */
          "images/placeable/sprinkler/pipe_null.png",
          "images/placeable/sprinkler/pump.png",
          // sprinklers - on
          "images/placeable/sprinkler/on/pipe_left.png",
          "images/placeable/sprinkler/on/pipe_right.png",
          "images/placeable/sprinkler/on/pipe_horizontal.png",
          "images/placeable/sprinkler/on/pipe_down.png",
          "images/placeable/sprinkler/on/pipe_down_left.png",
          "images/placeable/sprinkler/on/pipe_down_right.png",
          "images/placeable/sprinkler/on/pipe_down_triple.png",
          "images/placeable/sprinkler/on/pipe_up.png",
          "images/placeable/sprinkler/on/pipe_up_left.png",
          "images/placeable/sprinkler/on/pipe_up_right.png",
          "images/placeable/sprinkler/on/pipe_up_triple.png",
          "images/placeable/sprinkler/on/pipe_vertical.png",
          "images/placeable/sprinkler/on/pipe_left_triple.png",
          "images/placeable/sprinkler/on/pipe_right_triple.png",
          "images/placeable/sprinkler/on/pipe_quad.png",
          // sprinklers - off
          "images/placeable/sprinkler/off/pipe_left.png",
          "images/placeable/sprinkler/off/pipe_right.png",
          "images/placeable/sprinkler/off/pipe_horizontal.png",
          "images/placeable/sprinkler/off/pipe_down.png",
          "images/placeable/sprinkler/off/pipe_down_left.png",
          "images/placeable/sprinkler/off/pipe_down_right.png",
          "images/placeable/sprinkler/off/pipe_down_triple.png",
          "images/placeable/sprinkler/off/pipe_up.png",
          "images/placeable/sprinkler/off/pipe_up_left.png",
          "images/placeable/sprinkler/off/pipe_up_right.png",
          "images/placeable/sprinkler/off/pipe_up_triple.png",
          "images/placeable/sprinkler/off/pipe_vertical.png",
          "images/placeable/sprinkler/off/pipe_left_triple.png",
          "images/placeable/sprinkler/off/pipe_right_triple.png",
          "images/placeable/sprinkler/off/pipe_quad.png",

          "images/placeable/fences/g_d_u.png",
          "images/placeable/fences/g_d_u_o.png",
          "images/placeable/fences/g_r_l.png",
          "images/placeable/fences/g_r_l_o.png",
          "images/placeable/fences/f.png",
          "images/placeable/fences/f_d.png",
          "images/placeable/fences/f_d_u.png",
          "images/placeable/fences/f_d_l.png",
          "images/placeable/fences/f_r_d.png",
          "images/placeable/fences/f_r_l_u.png",
          "images/placeable/fences/f_u.png",
          "images/placeable/fences/f_d_l_u.png",
          "images/placeable/fences/f_l.png",
          "images/placeable/fences/f_r_d_l.png",
          "images/placeable/fences/f_r_d_u.png",
          "images/placeable/fences/f_r.png",
          "images/placeable/fences/f_l_u.png",
          "images/placeable/fences/f_r_d_l_u.png",
          "images/placeable/fences/f_r_l.png",
          "images/placeable/fences/f_r_u.png",

          "images/hostile_indicator.png",

          "images/ship/ship_debris.png",
          "images/ship/ship.png",
          "images/ship/ship_part.png",
          "images/ship/ship_clue.png",

          "images/selected.png",
          "images/itemFrame.png",
          "images/PauseMenu/Pause_Overlay.jpg",
          "images/PauseMenu/Pausenew.jpg",
          "images/fish/fish_1.png",
          "images/fish/fish_2.png",
          "images/fish/fish_3.png",
          "images/fish/fish_4.png",
          "images/fish/fish_5.png",
          "images/fish/fish_6.png",
          "images/fish/fish_7.png",
          "images/fish/fish_8.png",
          "images/fish/fish_9.png",
          "images/fish/fish_10.png",
          "images/fish/fish_11.png",
  };

  private static final String[] textureAtlasPaths = {
      "images/terrain_iso_grass.atlas", "images/GOD_IS_game_ver.atlas", "images/ghost.atlas", "images/player.atlas", "images/ghostKing.atlas",
      "images/animals/chicken.atlas", "images/animals/cow.atlas", "images/tractor.atlas",
      "images/animals/astrolotl.atlas", "images/animals/oxygen_eater.atlas", "images/questgiver.atlas",
      "images/missionStatus.atlas", "images/plants/cosmic_cob.atlas", "images/plants/aloe_vera.atlas",
      "images/plants/hammer_plant.atlas", "images/plants/space_snapper.atlas", "images/plants/atomic_algae.atlas",
      "images/plants/deadly_nightshade.atlas", "images/fireflies.atlas", "images/animals/dragonfly.atlas",
      "images/animals/bat.atlas", "images/projectiles/oxygen_eater_projectile.atlas",
      "images/ship/ship.atlas", "images/light.atlas", "images/projectiles/dragon_fly_projectile.atlas", "images/golden_trophy.atlas",
          "images/player_fishing.atlas", "images/walkietalkie.atlas", "images/animals/animal_effects.atlas", "images/cutscene.atlas"
  };
  private static final String[] soundPaths = {
          "sounds/Impact4.ogg", "sounds/car-horn-6408.mp3",
          "sounds/plants/aloeVera/click.wav", "sounds/plants/aloeVera/clickLore.wav",
          "sounds/plants/aloeVera/decay.wav", "sounds/plants/aloeVera/decayLore.wav",
          "sounds/plants/aloeVera/destroy.wav", "sounds/plants/aloeVera/destroyLore.wav",
          "sounds/plants/aloeVera/nearby.wav", "sounds/plants/aloeVera/nearbyLore.wav",
          "sounds/plants/cosmicCob/click.wav", "sounds/plants/cosmicCob/clickLore.wav",
          "sounds/plants/cosmicCob/decay.wav", "sounds/plants/cosmicCob/decayLore.wav",
          "sounds/plants/cosmicCob/destroy.wav", "sounds/plants/cosmicCob/destroyLore.wav",
          "sounds/plants/cosmicCob/nearby.wav", "sounds/plants/cosmicCob/nearbyLore.wav",
          "sounds/plants/hammerPlant/click.wav", "sounds/plants/hammerPlant/clickLore.wav",
          "sounds/plants/hammerPlant/decay.wav", "sounds/plants/hammerPlant/decayLore.wav",
          "sounds/plants/hammerPlant/destroy.wav", "sounds/plants/hammerPlant/destroyLore.wav",
          "sounds/plants/hammerPlant/nearby.wav", "sounds/plants/hammerPlant/nearbyLore.wav",
          "sounds/plants/nightshade/click.wav", "sounds/plants/nightshade/clickLore.wav",
          "sounds/plants/nightshade/decay.wav", "sounds/plants/nightshade/decayLore.wav",
          "sounds/plants/nightshade/destroy.wav", "sounds/plants/nightshade/destroyLore.wav",
          "sounds/plants/nightshade/nearby.wav", "sounds/plants/nightshade/nearbyLore.wav",
          "sounds/plants/venusFlyTrap/click.wav", "sounds/plants/venusFlyTrap/clickLore.wav",
          "sounds/plants/venusFlyTrap/decay.wav", "sounds/plants/venusFlyTrap/decayLore.wav",
          "sounds/plants/venusFlyTrap/destroy.wav", "sounds/plants/venusFlyTrap/destroyLore.wav",
          "sounds/plants/venusFlyTrap/nearby.wav", "sounds/plants/venusFlyTrap/nearbyLore.wav",
          "sounds/plants/waterWeed/click.wav", "sounds/plants/waterWeed/clickLore.wav",
          "sounds/plants/waterWeed/decay.wav", "sounds/plants/waterWeed/decayLore.wav",
          "sounds/plants/waterWeed/destroy.wav", "sounds/plants/waterWeed/destroyLore.wav",
          "sounds/plants/waterWeed/nearby.wav", "sounds/plants/waterWeed/nearbyLore.wav",
          "sounds/gate-interact.wav","sounds/tractor-start-up.wav", "sounds/shovel.wav",
          "sounds/hoe.wav", "sounds/watering-can.wav", "sounds/place.wav", "sounds/fishing-cast.wav",
          "sounds/applause.wav"
  };

  String[] skinPaths = {
          "flat-earth/skin/flat-earth-ui.json",
          "gardens-of-the-galaxy/gardens-of-the-galaxy.json",
          "gardens-of-the-galaxy-v2-orange/gardens-of-the-galaxy-v2-orange.json"
  };
  private final GameMap gameMap;

  private Entity player;
  private final ClimateController climateController;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   *
   * @param terrainFactory TerrainFactory used to create the terrain for the
   *                       GameArea.
   * @requires terrainFactory != null
   */
  public SpaceGameArea(TerrainFactory terrainFactory) {
    super();
    gameMap = new GameMap(terrainFactory);
    climateController = new ClimateController();
    ServiceLocator.registerGameArea(this);
  }

  /**
   * Create the game area, including terrain, static entities (trees), dynamic
   * entities (player)
   */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();
    spawnInvisibleObstacle();// spawn invisible obstacle on the non-traversable area of the map

    spawnShipDebris();

    player = spawnPlayer();

    spawnPlayerHighlight();
    spawnQuestgiver();

    spawnShip();

    ServiceLocator.getMissionManager().acceptQuest(QuestFactory.createFirstContactQuest());

    //Spawning behaviour for passive animals
    List<EntitySpawner> passiveSpawners = new ArrayList<>();
    passiveSpawners.add(new EntitySpawner(1, player2 -> NPCFactory.createAstrolotl(),
            0, 1, 0, 0, 10));
    passiveSpawners.add(new EntitySpawner(6, player3 -> NPCFactory.createChicken(),
            1, 4, 8, 4, 2));
    passiveSpawners.add(new EntitySpawner(5, player2 -> NPCFactory.createCow(),
            1, 3, 12, 4, 1));
    EntitiesSpawner passiveSpawner = new EntitiesSpawner(passiveSpawners);
    passiveSpawner.setGameAreas(this);

    //Initial spawns
    passiveSpawner.spawnNow();
    passiveSpawner.startPeriodicSpawning();

    //Spawning behaviour for hostiles
    List<EntitySpawner> hostileSpawners = new ArrayList<>();
    hostileSpawners.add(new EntitySpawner(3, player1 -> NPCFactory.createOxygenEater(),
            0, 1, 5, 5, 2));
    hostileSpawners.add(new EntitySpawner(5, player1 -> NPCFactory.createDragonfly(),
            0, 2, 5, 5, 3));
    hostileSpawners.add(new EntitySpawner(7, player1 -> NPCFactory.createBat(),
            0, 1, 5, 5, 2));


    hostileSpawner = new EntitiesSpawner(hostileSpawners);
    hostileSpawner.setGameAreas(this);
  }

  /**
   * Getter for hostileSpawner in this game area
   *
   * @return EntitiesSpawner for hostiles
   */
  public EntitiesSpawner getHostileSpawner() {
    return hostileSpawner;
  }

  @Override
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
    gameMap.createTerrainComponent();
    terrain = gameMap.getTerrainComponent();
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
     ArrayList<GridPoint2> nonTraversableObs = (ArrayList<GridPoint2>) this.gameMap.getNonTraversableTileCoordinates();
     for (int i = 0; i < nonTraversableObs.size(); i++) {
       GridPoint2 pos = nonTraversableObs.get(i);
       Entity invisibleObstacle = ObstacleFactory.createInvisibleObstacle();
       spawnEntityAt(invisibleObstacle, pos, true, false);
     }
   }

  /**
   * Spawns the initial Ship Debris randomly around the Player Ship's location.
   * Random position generation adapted from Team 1's spawnTool() below.
   */
  private void spawnShipDebris() {

    GridPoint2 minPos = new GridPoint2(SHIP_SPAWN.x - 7, SHIP_SPAWN.y - 5);
    GridPoint2 maxPos = new GridPoint2(SHIP_SPAWN.x + 7, SHIP_SPAWN.y + 7);

    List<GridPoint2> clearedTilesAroundShip = List.of(
            SHIP_SPAWN,
            new GridPoint2(SHIP_SPAWN.x, SHIP_SPAWN.y - 1),
            new GridPoint2(SHIP_SPAWN.x, SHIP_SPAWN.y + 1),
            new GridPoint2(SHIP_SPAWN.x - 1, SHIP_SPAWN.y - 1),
            new GridPoint2(SHIP_SPAWN.x - 1, SHIP_SPAWN.y),
            new GridPoint2(SHIP_SPAWN.x - 1, SHIP_SPAWN.y + 1),
            new GridPoint2(SHIP_SPAWN.x + 1, SHIP_SPAWN.y - 1),
            new GridPoint2(SHIP_SPAWN.x + 1, SHIP_SPAWN.y),
            new GridPoint2(SHIP_SPAWN.x + 1, SHIP_SPAWN.y + 1)
    );

    IntStream.range(0,15).forEach(i -> {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      TerrainTile tile = gameMap.getTile(randomPos);

      while (!tile.isTraversable() || tile.isOccupied() || clearedTilesAroundShip.contains(randomPos)) {
        randomPos = RandomUtils.random(minPos, maxPos);
        tile = gameMap.getTile(randomPos);
      }

      Entity shipDebris = ShipDebrisFactory.createShipDebris();
      spawnEntity(shipDebris);
      shipDebris.setPosition(terrain.tileToWorldPosition(randomPos));

      tile.setOccupant(shipDebris);
      tile.setOccupied();
    });
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
    spawnEntityAt(newQuestgiverIndicator, new GridPoint2(QUESTGIVER_SPAWN.x, QUESTGIVER_SPAWN.y+2), true, true);
  }

  private void spawnShip() {
    Entity newShip = ShipFactory.createShip();
    spawnEntityAt(newShip, SHIP_SPAWN, true, true);
  }

  private void playMusic() {
    /*
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
    */
    logger.debug("Loading and starting playback of background music.");
    ServiceLocator.getSoundService().getBackgroundMusicService().play(BackgroundMusicType.NORMAL);
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();

    // Leave this in until all sounds are added to the new sound system pls
    resourceService.loadSounds(soundPaths);

    resourceService.loadTextures(texturePaths);
    resourceService.loadTextures(TerrainFactory.getMapTextures());
    resourceService.loadTextureAtlases(textureAtlasPaths);
    resourceService.loadSkins(skinPaths);
    try {
      ServiceLocator.getSoundService().getBackgroundMusicService()
              .loadSounds(Arrays.asList(BackgroundSoundFile.values()));
    } catch (InvalidSoundFileException e) {
      throw new RuntimeException(e);
    }
    // Add effects that are needed
    List<SoundFile> effects = new ArrayList<>();
    effects.add(EffectSoundFile.TRACTOR_HONK);
    effects.add(EffectSoundFile.TRACTOR_START_UP);
    effects.add(EffectSoundFile.IMPACT);
    effects.add(EffectSoundFile.GATE_INTERACT);
    effects.add(EffectSoundFile.INVENTORY_OPEN);
    effects.add(EffectSoundFile.HOTKEY_SELECT);
    effects.add(EffectSoundFile.SHOVEL);
    effects.add(EffectSoundFile.HOE);
    effects.add(EffectSoundFile.WATERING_CAN);
    effects.add(EffectSoundFile.PLACE);
    effects.add(EffectSoundFile.FISHING_CAST);
    effects.add(EffectSoundFile.FISHING_CATCH);
    effects.add(EffectSoundFile.SCYTHE);
    effects.add(EffectSoundFile.GOD_DID);


    try {
      ServiceLocator.getSoundService().getEffectsMusicService().loadSounds(effects);
    } catch (InvalidSoundFileException e) {
      throw new RuntimeException(e);
    }

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(texturePaths);
    resourceService.unloadAssets(textureAtlasPaths);
    resourceService.unloadAssets(soundPaths);
  }

  @Override
  public void dispose() {
    super.dispose();
    this.unloadAssets();
  }


  /**
   * Returns the game map
   */
  public GameMap getMap() {
    return gameMap;
  }

  /**
   * Spawns the player highlight entity
   * Is the yellow square that highlights the tile the player is hovering over
   */
  public void spawnPlayerHighlight() {
    Entity playerHighlight = PlayerHighlightFactory.createPlayerHighlight();
    spawnEntityAt(playerHighlight, PLAYER_SPAWN, true, true);
  }
}
