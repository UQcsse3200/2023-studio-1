package com.csse3200.game.components.items;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainCropTileFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.InteractionDetector;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.PlantFactory;
import com.csse3200.game.services.FactoryService;
import com.csse3200.game.services.ServiceLocator;
import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

import java.util.List;
import java.util.function.Function;

import java.util.List;

import static com.csse3200.game.areas.terrain.TerrainCropTileFactory.createTerrainEntity;

public class ItemActions extends Component {

  private GameMap map;

  @Override
  public void create() {
    // Just in case we need constructor for later
  }

  /**
   * Uses the item at the given position
   *
   * @param player   the player entity using the item
   * @param mousePos the position of the mouse
   * @param map      item to use/ interact with tile
   * @return if interaction with tile was success return true else return false.
   */
  public boolean use(Entity player, Vector2 mousePos, GameMap map) {
    this.map = map;

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
        resultStatus = place(tile, getAdjustedPos(playerPos, mousePos));
        return resultStatus;
      }
      default -> {
        return false;
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
    ServiceLocator.getGameArea().spawnEntity(placeable);
    placeable.setPosition(adjustedPos);
    tile.setOccupant(placeable);
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
    Vector2 pos = getAdjustedPos(playerPos, mousePos);
    return ServiceLocator.getGameArea().getMap().getTile(pos);
  }


  /**
   * Gets the correct position for the player to interact with based off of the
   * mouse position. Will always return 1 tile to the left, right,
   * up, down, diagonal left up, diagonal right up, diagonal left down, diagonal
   * right down of the player.
   *
   * @param playerPos the position of the player
   * @param mousePos  the position of the mouse
   * @return a vector of the position where the player should hit
   */
  private Vector2 getAdjustedPos(Vector2 playerPos, Vector2 mousePos) {
    Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);
    Vector2 adjustedPosition = new Vector2(
            ServiceLocator.getGameArea().getMap().tileCoordinatesToVector(ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(new Vector2(mouseWorldPos.x, mouseWorldPos.y))));

    Vector2 playerPosCenter = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
    playerPosCenter.add(0, -1.0f); // Player entity sprite's feet are located -1.0f below the centre of the entity. ty Hunter

    playerPosCenter = ServiceLocator.getGameArea().getMap().tileCoordinatesToVector(ServiceLocator.getGameArea().getMap().vectorToTileCoordinates(playerPosCenter));
    ;
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
   * Waters the tile at the given position.
   *
   * @param tile the tile to be interacted with
   * @return if watering was successful return true else return false
   */
  private boolean water(TerrainTile tile) {
    boolean tileWaterable = isCropTile(tile.getOccupant());
    if (!tileWaterable) {
      return false;
    }

    // A water amount of 0.5 was recommended by team 7
    tile.getOccupant().getEvents().trigger("water", 0.5f);
    //item.getComponent(WateringCanLevelComponent.class).incrementLevel(-5); //TODO
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
      // If it is a chest check if it has stuff in it
      if (tile.getOccupant().getComponent(InventoryComponent.class) != null) {
        if (tile.getOccupant().getComponent(InventoryComponent.class).getInventory().size() >= 1) {
          return false;
        }
      }
      // TODO Below here is temp code all that should be called here is whatever is surronding by the other two todos
      // Just dont have time and want it out
      if (
              List.of(EntityType.Tile, EntityType.ShipDebris)
                      .contains(tile.getOccupant().getType())
      ) {
        // TODO this good
        tile.getOccupant().getEvents().trigger("destroy", tile);
        return true;
        // TODO Above this is amazing
      } else {
        Entity placedItem = tile.getOccupant();
        Vector2 newPos = placedItem.getPosition();
        tile.removeOccupant();

        Entity droppedItem = FactoryService.getItemFactories().get(placedItem.getType().toString()).get();
        ServiceLocator.getGameArea().spawnEntity(droppedItem);
        droppedItem.setPosition(newPos);
        //placedItem.getEvents().trigger("destroy"); //TODO: add trigger event to all placeable items so dynamic textures can be updated
        ServiceLocator.getGameArea().removeEntity(placedItem);
      }
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
    Vector2 newPos = getAdjustedPos(playerPos, mousePos);
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
      tile.getOccupant().getEvents().trigger("fertilise");
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
      tile.getOccupant().getEvents().trigger("plant", FactoryService.getPlantFactories()
              .get(entity.getComponent(ItemComponent.class).getItemName().replace(" seed", "")));
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
    entities.removeIf(entity -> entity.getComponent(TamableComponent.class).isTamed()); //TODO: axolotl? handle that

    Entity entityToFeed = interactionDetector.getNearest(entities);

    if (entityToFeed == null) {
      return false;
    }

    entityToFeed.getEvents().trigger("feed");
    return true;
  }
}
