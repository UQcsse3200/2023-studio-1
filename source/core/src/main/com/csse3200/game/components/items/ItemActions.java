package com.csse3200.game.components.items;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.plants.PlantComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ParticleService;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.player.HungerComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemActions extends Component {

  private final Random random = new SecureRandom();

  private final Logger logger = LoggerFactory.getLogger(ItemActions.class);

  private final ArrayList<Supplier<Entity>> oceanFish =
          new ArrayList<>(Arrays.asList(ItemFactory::createAtomicAlgaeSeed,
                  ItemFactory::createSpaceSnapperSeed,
                  ItemFactory::createAloeVeraSeed,
                  ItemFactory::createCosmicCobSeed,
                  ItemFactory::createDeadlyNightshadeSeed,
                  ItemFactory::createHammerPlantSeed,
                  ItemFactory::createSalmon,
                  ItemFactory::createBryton,
                  ItemFactory::createMrKrabs,
                  ItemFactory::createBraydan,
                  ItemFactory::createNetty,
                  ItemFactory::createHarry,
                  ItemFactory::createLarry));

  private final ArrayList<Supplier<Entity>> lavaFish =
          new ArrayList<>(Arrays.asList(ItemFactory::createLavaEel,
                  ItemFactory::createSprinklerItem,
                  ItemFactory::createChurchill,
                  ItemFactory::createSanders,
                  ItemFactory::createLola,
                  ItemFactory::createYak3,
                  ItemFactory::createPharLap));

  @Override
  public void create() {
    // Just in case we need constructor for later
    entity.getEvents().addListener("fishCaught", this::getFish);
  }

  private void getFish(String place, Entity player) {
    player.getEvents().trigger(PlayerActions.events.FISH_CAUGHT.name());
    Entity item;
    logger.info("Fish caught!");
    try {
      ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.FISHING_CATCH);
    } catch (Exception e) {
      logger.error("Failed to play fishsound", e);
    }
    switch (place) {
      case "ocean":
        // Get an ocean fish
        item = oceanFish.get(random.nextInt(oceanFish.size())).get();
        ServiceLocator.getMissionManager().getEvents().trigger("FISH");
        break;
      case "lava":
        // Have a chance to receive a drop 1 / 5
        if (random.nextInt(5) == 0) {
          // Get a lava fish
          item = lavaFish.get(random.nextInt(lavaFish.size())).get();
          ServiceLocator.getMissionManager().getEvents().trigger("FISH");
          break;
        }
        // Unlucky
        return;
      case "ḓ̸̺̯̰͍͇̫̻͔̜̮͔̫̘̮̆͗̏͛̃͐̓̓̈́̉͋͒j̴͇̗̱̝̜͌̃ ̷̨̠̝̲͍͚̥̭̪͕̜̯̔̉̑k̷̮̤̺̎͑͊̓̈̽̈́̃̿̐̐͛͘͝h̵͍̳̲̟̝̀̐͗͌̄̔a̶̢̧̛̫̥̙͈̪̲͇̿͒̇́͋̈́̄̉͗̕͜ͅl̷̨͎̻͇̞̩̪̪̦͙̹̳͚̜̍͌͋̀ͅḗ̵̪͕̰̥̊̓̅͌́͠d̶͕͚̦̽̄̏̍͘":
        if (random.nextInt(3) == 0) {
          // God didn't
          return;
        }
        item = ItemFactory.createGoldenFish();
        ServiceLocator.getMissionManager().getEvents().trigger("FISH");
        break;
      default:
        // Error
        logger.error("Error while fishing");
        return;
    }
    // Add to inventory
    logger.info("Added fish to inventory");
    ServiceLocator.getGameArea().getPlayer().getEvents().trigger("startVisualEffect", ParticleService.ParticleEffectType.SUCCESS_EFFECT);
    ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class).addItem(item);
  }

  /**
   * Uses the item at the given position
   *
   * @param player   the player entity using the item
   * @param mousePos the position of the mouse
   * @return if interaction with tile was success return true else return false.
   */
  public boolean use(Entity player, Vector2 mousePos) {
    Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);
    ItemComponent type = entity.getComponent(ItemComponent.class);
    // Wasn't an item or did not have ItemComponent class
    if (type == null) {
      return false;
    }

    // Add your item here!!!
    boolean resultStatus;
    TerrainTile tile = getTileAtPosition(mousePos);
    if (tile == null) {
      return false;
    }
    switch (type.getItemType()) {
      case HOE -> {
        resultStatus = hoe(mousePos);
        return resultStatus;
      }
      case SHOVEL -> {
        resultStatus = shovel(tile);
        return resultStatus;
      }
      case SCYTHE -> {
        resultStatus = harvest(tile);
        return resultStatus;
      }
      case WATERING_CAN -> {
        resultStatus = water(tile);
        return resultStatus;
      }
      case SWORD -> {
        player.getEvents().trigger(PlayerActions.events.ATTACK.name(), mousePos);
        return true;
      }
      case GUN -> {
        player.getEvents().trigger(PlayerActions.events.SHOOT.name(), mousePos);
        return true;
      }
      case TELEPORT_DEVICE -> {
        teleport(player);
        return true;
      }
      case FOOD -> {
        resultStatus = feed(player, mouseWorldPos);
        return resultStatus;
      }
      case FERTILISER -> {
        resultStatus = fertilise(tile);
        return resultStatus;
      }
      case SEED -> {
        resultStatus = plant(tile);
        return resultStatus;
      }
      case PLACEABLE -> {
        resultStatus = place(tile, getAdjustedPos(mousePos));
        return resultStatus;
      }
      case CLUE_ITEM -> {
        entity.getEvents().trigger("mapDisplay");
        return true;
      }
      case SHIP_PART -> {
        resultStatus = repair(player, mouseWorldPos);
        return resultStatus;
      }
      case FISHING_ROD -> {
        return fish(player, mousePos);
      }
      default -> {
        return false;
      }
    }
  }

  private boolean teleport(Entity player) {
    player.setPosition(new Vector2(20, 83));
    return true;
  }

  private Vector2 getAdjustedPosFish(Vector2 mousePos) {
    Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);
    Vector2 adjustedPosition = new Vector2(
            ServiceLocator.getGameArea().getMap().tileCoordinatesToVector(ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(new Vector2(mouseWorldPos.x, mouseWorldPos.y))));

    Vector2 playerPosCenter = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
    playerPosCenter.add(0, -1.0f); // Player entity sprite's feet are located -1.0f below the centre of the entity. ty Hunter

    playerPosCenter = ServiceLocator.getGameArea().getMap().tileCoordinatesToVector(ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(playerPosCenter));
    if (adjustedPosition.x - 0.5 > playerPosCenter.x) {
      playerPosCenter.x += 2;
    } else if (adjustedPosition.x + 0.5 < playerPosCenter.x) {
      playerPosCenter.x -= 2;
    }
    if (adjustedPosition.y - 0.5 > playerPosCenter.y) {
      playerPosCenter.y += 2;
    } else if (adjustedPosition.y + 0.5 < playerPosCenter.y) {
      playerPosCenter.y -= 2;
    }
    return playerPosCenter;
  }

  /**
   * Triggers an event if the tile is a water or lava tiles
   * Uses the event scheduler to trigger getFish() after a random delay
   * @param player - the player entity
   * @param mousePos - the mouse position
   * @return if successful
   */
  private boolean fish(Entity player, Vector2 mousePos) {
    Vector2 nullValue = new Vector2(82, 25);
    if (entity.getEvents().getScheduledEventsSize() != 0) {
      player.getEvents().trigger(PlayerActions.events.FISH_CAUGHT.name());
      entity.getEvents().cancelAllEvents();
      logger.info("Fishing cancelled");
      return false;
    }
    Integer randomNumber;
    Vector2 pos = getAdjustedPosFish(mousePos);
    // If null value then fish in spot
    if (pos.equals(nullValue)) {
      randomNumber = random.nextInt(5) + 1;
      logger.info("Fishing occurred");
      try {
        ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.FISHING_CAST);
      } catch (Exception e) {
        logger.error("Failed to play fishsound", e);
      }
      player.getEvents().trigger(PlayerActions.events.CAST_FISHING_RODS.name(), mousePos);
      entity.getEvents().scheduleEvent(randomNumber, "fishCaught", "ḓ̸̺̯̰͍͇̫̻͔̜̮͔̫̘̮̆͗̏͛̃͐̓̓̈́̉͋͒j̴͇̗̱̝̜͌̃ ̷̨̠̝̲͍͚̥̭̪͕̜̯̔̉̑k̷̮̤̺̎͑͊̓̈̽̈́̃̿̐̐͛͘͝h̵͍̳̲̟̝̀̐͗͌̄̔a̶̢̧̛̫̥̙͈̪̲͇̿͒̇́͋̈́̄̉͗̕͜ͅl̷̨͎̻͇̞̩̪̪̦͙̹̳͚̜̍͌͋̀ͅḗ̵̪͕̰̥̊̓̅͌́͠d̶͕͚̦̽̄̏̍͘", player);
      return true;
    }
    TerrainTile tile = ServiceLocator.getGameArea().getMap().getTile(pos);
    // Is there viable water where the tile would land
    if (tile.getTerrainCategory() == TerrainTile.TerrainCategory.DEEPWATER) {
      // Ocean fish
      randomNumber = random.nextInt(5) + 1;
      logger.info("Fishing occurred");
      try {
        ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.FISHING_CAST);
      } catch (Exception e) {
        logger.error("Failed to play fishsound", e);
      }
      // schedule the event
      player.getEvents().trigger(PlayerActions.events.CAST_FISHING_RODS.name(), mousePos);
      entity.getEvents().scheduleEvent(randomNumber,"fishCaught", "ocean", player);
      return true;
    } else if (tile.getTerrainCategory() == TerrainTile.TerrainCategory.LAVA) {
      // Lava fish
      randomNumber = random.nextInt(10) + 1;
      logger.info("Fishing occurred");
      try {
        ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.FISHING_CAST);
      } catch (Exception e) {
        logger.error("Failed to play fishsound", e);
      }
      // schedule the event
      player.getEvents().trigger(PlayerActions.events.CAST_FISHING_RODS.name(), mousePos);
      entity.getEvents().scheduleEvent(randomNumber,"fishCaught", "lava", player);
      return true;
    }
    return false;
  }

  /**
   * Eats a given food item or power modifier
   * @param player the player entity
   */
  public void eat(Entity player) {
    ItemComponent type = entity.getComponent(ItemComponent.class);
    // Wasn't an item or did not have ItemComponent class
    if (type == null) {
      return;
    }

    if (type.getItemType() == ItemType.FOOD) {
      switch (type.getItemName()) {
        case "Ear of Cosmic Cob":
          player.getComponent(HungerComponent.class).increaseHungerLevel(-10);
          player.getComponent(CombatStatsComponent.class).addHealth(5);
          return;
        case "Nightshade Berry":
          player.getComponent(CombatStatsComponent.class).addHealth(-10);
          player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
          return;
        case "Hammer Flower":
          player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
          player.getComponent(CombatStatsComponent.class).addHealth(5);
          return;
        case "Aloe Vera Leaf":
          player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
          player.getComponent(CombatStatsComponent.class).addHealth(30);
          return;
        case "Lave Eel":
          player.getComponent(HungerComponent.class).increaseHungerLevel(-50);
          player.getComponent(CombatStatsComponent.class).addHealth(100);
          return;
        case "Salmon":
          player.getComponent(HungerComponent.class).increaseHungerLevel(-10);
          player.getComponent(CombatStatsComponent.class).addHealth(5);
          return;
        default:
          // Any fish
          player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
      }
    } else if (type.getItemType() == ItemType.EGG) {
        if (type.getItemName().equals("golden egg")) {
            player.getComponent(PlayerActions.class).setSpeedMultiplier(1.5f);
            player.getEvents().scheduleEvent(20f, "setSpeedMultiplier", 1f);
        } else {
            player.getComponent(HungerComponent.class).increaseHungerLevel(-10);
        }
    } else if (type.getItemType() == ItemType.MILK) {
      player.getComponent(PlayerActions.class).setDamageMultiplier(2.5f);
      player.getEvents().scheduleEvent(5f, "setDamageMultiplier", 1f);
      player.getComponent(CombatStatsComponent.class).addHealth(5);
      player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
    }
  }

  /**
   * Places a placeable object based on its name (from ItemComponent) on a
   * TerrainTile
   *
   * @param tile        - The TerrainTile to place the object on
   * @param adjustedPos - The position of the tile as a Vector2
   * @return the result of whether it was placed
   */
  private boolean place(TerrainTile tile, Vector2 adjustedPos) {
    if (tile == null) {
      return false;
    }
    if (tile.isOccupied() || !tile.isTraversable()) {
      // Do not place
      return false;
    }

    try {
      ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.PLACE);
    } catch (Exception e) {
      logger.error("Failed to play place sound", e);
    }

    // Make the Entity to place
    Entity placeable = FactoryService.getPlaceableFactories()
        .get(entity.getComponent(ItemComponent.class).getItemName()).get();
    /* It is crucial that we set the position of the placeable BEFORE we spawn it in,
    this is a side effect of connectedEntityComponent needing to query the position of the placeable entity. */
    placeable.setPosition(adjustedPos);
    ServiceLocator.getGameArea().spawnEntity(placeable);
    tile.setOccupant(placeable);
    // Placing a placeable item should remove it from inventory
    ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class).removeItem(entity);
    return true;
  }

  /**
   * Gets the tile at the given position. else returns null
   * @param mousePos  the position of the mouse
   * @return Entity of tile at location else returns null
   */
  private TerrainTile getTileAtPosition(Vector2 mousePos) {
    Vector2 pos = getAdjustedPos(mousePos);
    return ServiceLocator.getGameArea().getMap().getTile(pos);
  }


  /**
   * Gets the correct position for the player to interact with based off of the
   * mouse position. Will always return 1 tile to the left, right,
   * up, down, diagonal left up, diagonal right up, diagonal left down, diagonal
   * right down of the player.
   *
   * @param mousePos the position of the mouse
   * @return a vector of the position where the player should hit
   */
  private Vector2 getAdjustedPos(Vector2 mousePos) {
    Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);
    Vector2 adjustedPosition = new Vector2(
            ServiceLocator.getGameArea().getMap().tileCoordinatesToVector(ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(new Vector2(mouseWorldPos.x, mouseWorldPos.y))));

    Vector2 playerPosCenter = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
    playerPosCenter.add(0, -1.0f); // Player entity sprite's feet are located -1.0f below the centre of the entity. ty Hunter

    playerPosCenter = ServiceLocator.getGameArea().getMap().tileCoordinatesToVector(ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(playerPosCenter));
    if (adjustedPosition.x - 0.5 > playerPosCenter.x) {
      playerPosCenter.x += 1;
    } else if (adjustedPosition.x + 0.5 < playerPosCenter.x) {
      playerPosCenter.x -= 1;
    }
    if (adjustedPosition.y - 0.5 > playerPosCenter.y) {
      playerPosCenter.y += 1;
    } else if (adjustedPosition.y + 0.5 < playerPosCenter.y) {
      playerPosCenter.y -= 1;
    }
    return playerPosCenter;
  }



  /**
   * Waters the tile at the given position. Or fill the watering-can if the empty tile is a water tile
   *
   * @param tile the tile to be interacted with
   * @return if watering was successful return true else return false
   */
  private boolean water(TerrainTile tile) {

    WateringCanLevelComponent wateringCan = entity.getComponent(WateringCanLevelComponent.class);
    List<String> waterTiles = Arrays.asList("SHALLOWWATER", "FLOWINGWATER", "DEEPWATER");
    //if the tile is an unoccupied water tile then fill the watering can instead of emptying
    if (!tile.isOccupied() && waterTiles.contains(tile.getTerrainCategory().toString())){
      wateringCan.fillToMax();
      return true;
    }

    //check if there even is any water in the can
    if (wateringCan.isEmpty()){
      return false;
    }

    try {
      ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.WATERING_CAN);
    } catch (Exception e) {
      logger.error("Failed to play wateringCan sound", e);
    }

    boolean tileWaterable = isCropTile(tile.getOccupant());
    entity.getComponent(WateringCanLevelComponent.class).incrementLevel(-2);  //decrease the water level by 5 units
    
    if (!tileWaterable) {
      return false;
    }

    // A water amount of 0.2
    tile.getOccupant().getEvents().trigger("water", 0.2f);
    if (tile.getOccupant().getComponent(CropTileComponent.class).getPlant() != null) {
      ServiceLocator.getMissionManager().getEvents().trigger(
              MissionManager.MissionEvent.WATER_CROP.name(),
              tile.getOccupant().getComponent(CropTileComponent.class).getPlant().getComponent(PlantComponent.class).getPlantType());
    }
    return true;
  }

  /**
   * Harvests the tile at the given position
   *
   * @param tile the tile to be interacted with
   * @return if harvesting was successful return true else return false
   */
  private boolean harvest(TerrainTile tile) {
    try {
      ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.SCYTHE);
    } catch (Exception e) {
      logger.error("Failed to play harvest sound", e);
    }
    boolean tileHarvestable = isCropTile(tile.getOccupant());
    if (tileHarvestable) {
      ServiceLocator.getParticleService().startEffectAtPosition(ParticleService.ParticleEffectType.DIRT_EFFECT, tile.getOccupant().getCenterPosition());
      tile.getOccupant().getEvents().trigger("harvest");
      return true;
    }
    return false;
  }

  /**
   * Shovels the tile at the given position
   *
   * @param tile the tile to be interacted with
   * @return if shoveling was successful return true else return false
   */
  private boolean shovel(TerrainTile tile) {
    try {
      ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.SHOVEL);
    } catch (Exception e) {
      logger.error("Failed to play shovel sound", e);
    }
    // If there is something to remove
    if (tile.getOccupant() != null) {
      Entity occupant = tile.getOccupant();
      // Trigger the destroy method within that occupant
      ServiceLocator.getParticleService().startEffectAtPosition(ParticleService.ParticleEffectType.DIRT_EFFECT, occupant.getCenterPosition());
      occupant.getEvents().trigger("destroy", tile);
      return true;
    }
    return false;
  }

  /**
   * Hoes the tile at the given position
   *
   * @param mousePos the position of the mouse
   * @return if hoeing was successful return true else return false
   */
  private boolean hoe(Vector2 mousePos) {
    try {
      ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.HOE);
    } catch (Exception e) {
      logger.error("Failed to play scy sound", e);
    }
    TerrainTile tile = getTileAtPosition(mousePos);
    if (tile.isOccupied() || !tile.isTillable()) {
      return false;
    }
    // Make a new tile
    Vector2 newPos = getAdjustedPos(mousePos);
    Entity cropTile = createTerrainEntity(newPos);
    ServiceLocator.getEntityService().register(cropTile);
    tile.setOccupant(cropTile);
    tile.setOccupied();
    cropTile.getEvents().trigger("startVisualEffect", ParticleService.ParticleEffectType.DIRT_EFFECT);
    return true;
  }

  /**
   * Fertilises the tile at the given position
   *
   * @param tile the tile to be interacted with
   * @return if fertilising was successful return true else return false
   */
  private boolean fertilise(TerrainTile tile) {
    if (isCropTile(tile.getOccupant())) {
      CropTileComponent cropTileComponent = tile.getOccupant().getComponent(CropTileComponent.class);
      if (!cropTileComponent.isFertilised()) {
        // Fertilising a crop tile should remove the item from the player inventory
        ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class).removeItem(entity);
        tile.getOccupant().getEvents().trigger("fertilise");
      }
      return true;
    }
    return false;
  }

  /**
   * Plants the given seed in the tile at the given position
   *
   * @param tile the tile to be interacted with
   * @return if planting was successful return true else return false
   */
  private boolean plant(TerrainTile tile) {
    if (isCropTile(tile.getOccupant())) {
      CropTileComponent cropTileComponent = tile.getOccupant().getComponent(CropTileComponent.class);
      if (cropTileComponent.getPlant() == null) {
        // Planting using seeds should remove the item from player inventory
        ServiceLocator.getGameArea().getPlayer().getComponent(InventoryComponent.class).removeItem(entity);
        tile.getOccupant().getEvents().trigger("plant", FactoryService.getPlantFactories()
                .get(entity.getComponent(ItemComponent.class).getItemName().replace(" Seeds", "")));
      }
      return true;
    }
    return false;
  }

  /**
   * Checks if the tile is harvestable by checking if it is a CropTile
   *
   * @param tile tile being checked
   * @return true if tile is harvestable else false
   */
  private boolean isCropTile(Entity tile) {
    return (tile != null) && (tile.getComponent(CropTileComponent.class) != null);
  }

  /**
   * Feeds held item to suitable entity.
   * @param player player that will feed item
   * @param mouseWorldPos position to check for feedable entity
   * @return true if feed is successful
   */
  private boolean feed(Entity player, Vector2 mouseWorldPos) {
    InteractionDetector interactionDetector = player.getComponent(InteractionDetector.class);
    if (interactionDetector == null) {
      return false;
    }

    List<Entity> entities = interactionDetector.getEntitiesTowardsPosition(mouseWorldPos);
    entities.removeIf(entity -> entity.getComponent(TamableComponent.class) == null);

    Entity entityToFeed = interactionDetector.getNearest(entities);

    if (entityToFeed == null) {
      return false;
    }

    // Feeding animals should remove the food from player inventory if food is their favourite
    if (entityToFeed.getComponent(TamableComponent.class).getFavouriteFood()
            .equals(entity.getComponent(ItemComponent.class).getItemName())) {
      entityToFeed.getEvents().trigger("feed");
      player.getComponent(InventoryComponent.class).removeItem(entity);
      entityToFeed.getEvents().trigger("startVisualEffect", ParticleService.ParticleEffectType.FEED_EFFECT);
    }

    return true;
  }

  /**
   * Repairs the ship if the player has a ship part item
   *
   * @param player        the player attempting to repair the ship
   * @param mouseWorldPos position of player mouse to check for ship entity
   * @return true if the repair is successful, false otherwise
   */
  private boolean repair(Entity player, Vector2 mouseWorldPos) {
    InteractionDetector detector = player.getComponent(InteractionDetector.class);
    if (detector == null) {
      return false;
    }

    List<Entity> entities = detector.getEntitiesTowardsPosition(mouseWorldPos);
    entities.removeIf(entity -> entity.getType() == null);
    entities.removeIf(entity -> entity.getType() != EntityType.SHIP);

    Entity ship = detector.getNearest(entities);
    if (ship == null) {
      return false;
    }
    if (ship.getType() == EntityType.SHIP) {
      ship.getEvents().trigger(ShipFactory.events.ADD_PART.name(), 1);
      player.getComponent(InventoryComponent.class).removeItem(entity);
      ServiceLocator.getParticleService().startEffectAtPosition(ParticleService.ParticleEffectType.SUCCESS_EFFECT, ship.getPosition().cpy().add(1,0));
      ServiceLocator.getParticleService().startEffectAtPosition(ParticleService.ParticleEffectType.SUCCESS_EFFECT, ship.getPosition().cpy().add(2,0));
      return true;
    }
    return false;
  }
}
