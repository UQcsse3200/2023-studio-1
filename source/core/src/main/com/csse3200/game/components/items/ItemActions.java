package com.csse3200.game.components.items;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;
import java.util.Arrays;
import java.util.List;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.player.HungerComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;

public class ItemActions extends Component {

  @Override
  public void create() {
    // Just in case we need constructor for later
  }

  /**
   * Uses the item at the given position
   *
   * @param player   the player entity using the item
   * @param mousePos the position of the mouse
   * @return if interaction with tile was success return true else return false.
   */
  public boolean use(Entity player, Vector2 mousePos) {
    Vector2 playerPos = player.getPosition();
    Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);

    ItemComponent type = entity.getComponent(ItemComponent.class);
    // Wasn't an item or did not have ItemComponent class
    if (type == null) {
      return false;
    }

    // Add your item here!!!
    boolean resultStatus;
    TerrainTile tile = getTileAtPosition(playerPos, mousePos);
    if (tile == null) {
      return false;
    }
    switch (type.getItemType()) {
      case HOE -> {
        resultStatus = hoe(playerPos, mousePos);
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
        player.getEvents().trigger("attack", mousePos);
        return true;
      }
      case GUN -> {
        player.getEvents().trigger("shoot", mousePos);
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
      default -> {
        return false;
      }
    }
  }

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
          entity.dispose();
          return;
        case "Nightshade Berry":
          player.getComponent(CombatStatsComponent.class).addHealth(-10);
          player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
          entity.dispose();
          return;
        case "Hammer Flower":
          player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
          entity.dispose();
          return;
        case "Aloe Vera Leaf":
          player.getComponent(HungerComponent.class).increaseHungerLevel(-5);
          player.getComponent(CombatStatsComponent.class).addHealth(30);
          entity.dispose();
          return;
        default:
          // Nothing
      }
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
   *
   * @param playerPos the position of the player
   * @param mousePos  the position of the mouse
   * @return Entity of tile at location else returns null
   */
  private TerrainTile getTileAtPosition(Vector2 playerPos, Vector2 mousePos) {
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

    boolean tileWaterable = isCropTile(tile.getOccupant());
    entity.getComponent(WateringCanLevelComponent.class).incrementLevel(-5);  //decrease the water level by 5 units
    
    if (!tileWaterable) {
      return false;
    }

    // A water amount of 0.5 was recommended by team 7
    tile.getOccupant().getEvents().trigger("water", 0.5f);
    return true;
  }

  /**
   * Harvests the tile at the given position
   *
   * @param tile the tile to be interacted with
   * @return if harvesting was successful return true else return false
   */
  private boolean harvest(TerrainTile tile) {
    boolean tileHarvestable = isCropTile(tile.getOccupant());
    if (tileHarvestable) {
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
    // If there is something to remove
    if (tile.getOccupant() != null) {
      // Trigger the destroy method within that occupant
      tile.getOccupant().getEvents().trigger("destroy", tile);
      return true;
    }
    return false;
  }

  /**
   * Hoes the tile at the given position
   *
   * @param playerPos the position of the player
   * @param mousePos  the position of the mouse
   * @return if hoeing was successful return true else return false
   */
  private boolean hoe(Vector2 playerPos, Vector2 mousePos) {
    TerrainTile tile = getTileAtPosition(playerPos, mousePos);
    if (tile.isOccupied() || !tile.isTillable()) {
      return false;
    }
    // Make a new tile
    Vector2 newPos = getAdjustedPos(mousePos);
    Entity cropTile = createTerrainEntity(newPos);
    ServiceLocator.getEntityService().register(cropTile);
    tile.setOccupant(cropTile);
    tile.setOccupied();
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

    entityToFeed.getEvents().trigger("feed");
    // Feeding animals should remove the food from player inventory
    player.getComponent(InventoryComponent.class).removeItem(entity);
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
    entities.removeIf(entity -> entity.getType() != EntityType.Ship);

    Entity ship = detector.getNearest(entities);
    if (ship == null) {
      return false;
    }
    if (ship.getType() == EntityType.Ship) {
      ship.getEvents().trigger("addPart", 1);
      player.getComponent(InventoryComponent.class).removeItem(entity);
      return true;
    }
    return false;
  }
}
