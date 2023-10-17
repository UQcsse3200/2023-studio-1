package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.ship.ShipDebrisComponent;
import com.csse3200.game.entities.EntitiesSpawner;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntitySpawner;
import com.csse3200.game.entities.factories.*;
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
          "images/progress-bar/part2day1.png",
          "images/progress-bar/part2day2.png",
          "images/progress-bar/part2day3.png",
          "images/progress-bar/part2day4.png",
          "images/progress-bar/part2day5.png",
          "images/progress-bar/part2day6.png",
          "images/progress-bar/part3day1.png",
          "images/progress-bar/part3day2.png",
          "images/progress-bar/part3day3.png",
          "images/progress-bar/part3day4.png",
          "images/progress-bar/part3day5.png",
          "images/progress-bar/part3day6.png",
          "images/progress-bar/part3day7.png",
          "images/progress-bar/part3day8.png",
          "images/progress-bar/part3day9.png",

          "images/Player_Hunger/hunger_bar_outline.png",
          "images/Player_Hunger/hunger_bar_fill.png",
          "images/projectiles/oxygen_eater_projectile.png",
          "images/projectiles/gun_projectile.png",

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
          "images/ship/clue_item.png",
          "images/ship/ship_part_tile.png",
          "images/ship/part_tile_indicator.png",
		  "images/walkietalkie.png",
		  "images/teleporter.png",
          
          "images/PauseMenu/Pause_Overlay.jpg",
          "images/PauseMenu/Pausenew.jpg",
          "images/miniMap/shipIcon.png",
          "images/miniMap/playerIcon.png",
          "images/miniMap/plantIcon.png",
          "images/miniMap/questGiverIcon.png",
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
          "images/placeable/sprinkler/sprinkler_animation.png",
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
      "images/projectiles/gun_projectile.atlas", "images/player_fishing.atlas", "images/walkietalkie.atlas",
      "images/animals/animal_effects.atlas", "images/cutscene.atlas", "images/placeable/sprinkler/sprinkler_animation.atlas",
      "images/shipeater.atlas", "images/plants/plant_aoe.atlas"
  };

  private static final String[] soundPaths = {
          "sounds/Impact4.ogg", "sounds/car-horn-6408.mp3",
          "sounds/animals/AstrolotlFeed.mp3",  "sounds/animals/BatAttack.mp3", "sounds/animals/ChickenFeed.mp3",
          "sounds/animals/ChickenDeath.mp3", "sounds/animals/CowFeed.mp3",
          "sounds/animals/CowDeath.mp3",  "sounds/animals/DeathOxygenEater.mp3",
          "sounds/animals/DeathBats.mp3", "sounds/animals/DragonflyAttackPlant.mp3",
          "sounds/animals/DragonFlyAttackPlayer.mp3",
          "sounds/animals/OxygenEaterAttack.mp3", "sounds/animals/TamedAnimal.mp3",
          "sounds/plants/click.wav", "sounds/plants/decay.wav",
          "sounds/plants/destroy.wav", "sounds/plants/nearby.wav",
          "sounds/plants/aloeVera/clickLore.wav", "sounds/plants/aloeVera/decayLore.wav",
          "sounds/plants/aloeVera/destroyLore.wav", "sounds/plants/aloeVera/nearbyLore.wav",
          "sounds/plants/cosmicCob/clickLore.wav", "sounds/plants/cosmicCob/decayLore.wav",
          "sounds/plants/cosmicCob/destroyLore.wav", "sounds/plants/cosmicCob/nearbyLore.wav",
          "sounds/plants/hammerPlant/clickLore.wav", "sounds/plants/hammerPlant/decayLore.wav",
          "sounds/plants/hammerPlant/destroyLore.wav", "sounds/plants/hammerPlant/nearbyLore.wav",
          "sounds/plants/nightshade/clickLore.wav", "sounds/plants/nightshade/decayLore.wav",
          "sounds/plants/nightshade/destroyLore.wav", "sounds/plants/nightshade/nearbyLore.wav",
          "sounds/plants/spaceSnapper/clickLore.wav", "sounds/plants/spaceSnapper/decayLore.wav",
          "sounds/plants/spaceSnapper/destroyLore.wav", "sounds/plants/spaceSnapper/nearbyLore.wav",
          "sounds/plants/atomicAlgae/clickLore.wav", "sounds/plants/atomicAlgae/decayLore.wav",
          "sounds/plants/atomicAlgae/destroyLore.wav", "sounds/plants/atomicAlgae/nearbyLore.wav",
          "sounds/player/PlayerDeath.mp3", "sounds/player/PlayerGetsHit.mp3",
          "sounds/weapons/GunAttack.mp3",  "sounds/weapons/GunReload.mp3",
          "sounds/weapons/SwordHitEntity.mp3", "sounds/weapons/SwordSwing.mp3",
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
    climateController.initialiseEvents();

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

    //Spawning behaviour for passive animals
    List<EntitySpawner> passiveSpawners = new ArrayList<>();
    passiveSpawners.add(new EntitySpawner(1, player2 -> NPCFactory.createAstrolotl(),
            0, 1, 0, 0, 10));
    passiveSpawners.add(new EntitySpawner(5, player3 -> NPCFactory.createChicken(),
            1, 3, 8, 4, 1));
    passiveSpawners.add(new EntitySpawner(5, player2 -> NPCFactory.createCow(),
            1, 4, 12, 4, 1));
    EntitiesSpawner passiveSpawner = new EntitiesSpawner(passiveSpawners);
    passiveSpawner.setGameAreas(this);

    //Initial spawns
    passiveSpawner.spawnNow();
    passiveSpawner.startPeriodicSpawning();

    //Spawning behaviour for hostiles
    List<EntitySpawner> hostileSpawners = new ArrayList<>();
    hostileSpawners.add(new EntitySpawner(3, player1 -> NPCFactory.createOxygenEater(),
            2, 1, 0, 8, 3));
    hostileSpawners.add(new EntitySpawner(4, player1 -> NPCFactory.createDragonfly(),
            1, 1, 8, 8, 2));
    hostileSpawners.add(new EntitySpawner(5, player1 -> NPCFactory.createBat(),
            1, 1, 16, 8, 1));


    hostileSpawner = new EntitiesSpawner(hostileSpawners);
    hostileSpawner.setGameAreas(this);

    // play background music
    ServiceLocator.getSoundService().getBackgroundMusicService().play(BackgroundMusicType.NORMAL);

    ShipDebrisComponent.clearCanSpawnShipEater();

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
    ui.addComponent(new GameAreaDisplay("Gardens of the Galaxy"));
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
    effects.add(EffectSoundFile.SWITCH_TOOLBAR);
    effects.add(EffectSoundFile.DRAG_ITEM);
    effects.add(EffectSoundFile.DROP_ITEM);
    effects.add(EffectSoundFile.DELETE_ITEM);
    effects.add(EffectSoundFile.COW_FEED);
    effects.add(EffectSoundFile.ASTROLOTL_FEED);
    effects.add(EffectSoundFile.CHICKEN_FEED);
    effects.add(EffectSoundFile.TAMED_ANIMAL);
    effects.add(EffectSoundFile.ATTACK_MISS);
    effects.add(EffectSoundFile.ATTACK_HIT);
    effects.add(EffectSoundFile.GUN_ATTACK);
    effects.add(EffectSoundFile.OXYGEN_EAT_DEATH);
    effects.add(EffectSoundFile.DRAGONFLY_DEATH);
    effects.add(EffectSoundFile.DEATH_BATS);
    effects.add(EffectSoundFile.CHICKEN_DEATH);
    effects.add(EffectSoundFile.PLAYER_DEATH);
    effects.add(EffectSoundFile.GUN_RELOAD);
    effects.add(EffectSoundFile.COW_DEATH);
    effects.add(EffectSoundFile.PLAYER_DAMAGE);
    effects.add(EffectSoundFile.BAT_ATTACK);
    effects.add(EffectSoundFile.OXYGEN_ATTACK);
    effects.add(EffectSoundFile.DRAGONFLY_ATTACK_PLAYER);
    effects.add(EffectSoundFile.DRAGONFLY_ATTACK_PLANT);
    effects.add(EffectSoundFile.PLANT_CLICK);
    effects.add(EffectSoundFile.PLANT_DECAY);
    effects.add(EffectSoundFile.PLANT_DESTROY);
    effects.add(EffectSoundFile.PLANT_NEARBY);
    effects.add(EffectSoundFile.ALOE_VERA_CLICK_LORE);
    effects.add(EffectSoundFile.ALOE_VERA_DECAY_LORE);
    effects.add(EffectSoundFile.ALOE_VERA_DESTROY_LORE);
    effects.add(EffectSoundFile.ALOE_VERA_NEARBY_LORE);
    effects.add(EffectSoundFile.COSMIC_COB_CLICK_LORE);
    effects.add(EffectSoundFile.COSMIC_COB_DECAY_LORE);
    effects.add(EffectSoundFile.COSMIC_COB_DESTROY_LORE);
    effects.add(EffectSoundFile.COSMIC_COB_NEARBY_LORE);
    effects.add(EffectSoundFile.HAMMER_PLANT_CLICK_LORE);
    effects.add(EffectSoundFile.HAMMER_PLANT_DECAY_LORE);
    effects.add(EffectSoundFile.HAMMER_PLANT_DESTROY_LORE);
    effects.add(EffectSoundFile.HAMMER_PLANT_NEARBY_LORE);
    effects.add(EffectSoundFile.DEADLY_NIGHTSHADE_CLICK_LORE);
    effects.add(EffectSoundFile.DEADLY_NIGHTSHADE_DECAY_LORE);
    effects.add(EffectSoundFile.DEADLY_NIGHTSHADE_DESTROY_LORE);
    effects.add(EffectSoundFile.DEADLY_NIGHTSHADE_NEARBY_LORE);
    effects.add(EffectSoundFile.SPACE_SNAPPER_CLICK_LORE);
    effects.add(EffectSoundFile.SPACE_SNAPPER_DECAY_LORE);
    effects.add(EffectSoundFile.SPACE_SNAPPER_DESTROY_LORE);
    effects.add(EffectSoundFile.SPACE_SNAPPER_NEARBY_LORE);
    effects.add(EffectSoundFile.ATOMIC_ALGAE_CLICK_LORE);
    effects.add(EffectSoundFile.ATOMIC_ALGAE_DECAY_LORE);
    effects.add(EffectSoundFile.ATOMIC_ALGAE_DESTROY_LORE);
    effects.add(EffectSoundFile.ATOMIC_ALGAE_NEARBY_LORE);
    effects.add(EffectSoundFile.ACID_BURN);
    effects.add(EffectSoundFile.BLIZZARD);
    effects.add(EffectSoundFile.LIGHTNING_STRIKE);
    effects.add(EffectSoundFile.SOLAR_SURGE);
    effects.add(EffectSoundFile.STORM);
    effects.add(EffectSoundFile.SURGE);
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
    resourceService.unloadAssets(TerrainFactory.getMapTextures());
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
