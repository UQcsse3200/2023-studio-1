package com.csse3200.game.components.items;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.areas.terrain.CropTileComponent;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.InteractionColliderComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import java.util.List;
import com.csse3200.game.entities.factories.PlantFactory;
import java.util.function.Function;

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
   * @param player the player entity using the item
   * @param mousePos  the position of the mouse
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
      System.out.println("Map team pls fix");
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
      case FOOD -> { // TODO: THIS IS ITEM TYPE IS JUST FOR TESTING PURPOSES, REPLACE WITH PLANT DROP TYPE
        InteractionColliderComponent interactionCollider = player.getComponent(InteractionColliderComponent.class);
        List<Entity> entitiesTowardsMouse = interactionCollider.getEntitiesTowardsPosition(mouseWorldPos);
        Entity feedableEntity = interactionCollider.getSuitableEntity(entitiesTowardsMouse, ItemType.FOOD);
        resultStatus = feed(feedableEntity);
        if (!resultStatus) {
          // consume it yourself instead??
          // consume(player)
          resultStatus = true;
        }
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
      default -> {
        return false;
      }
    }
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
    return map.getTile(pos);
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
    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();

    int playerXPos = width / 2;
    int playerYPos = height / 2;

    int xDelta = 0;
    int yDelta = 0;

    if (playerXPos - 24 > mousePos.x) {
      xDelta -= 1;
    } else if (playerXPos + 24 < mousePos.x) {
      xDelta += 1;
    }

    if (playerYPos + 48 < mousePos.y) {
      yDelta -= 1;
    } else if (playerYPos - 48 > mousePos.y) {
      yDelta += 1;
    }
    return new Vector2(playerPos.x + xDelta, playerPos.y + yDelta);
  }

  /**
   * Waters the tile at the given position.
   *
   * @param tile the tile to be interacted with
   * @return if watering was successful return true else return false
   */
  private boolean water(TerrainTile tile) {
    boolean tileWaterable = isCropTile(tile.getCropTile());
    if (!tileWaterable) {
      return false;
    }

    // A water amount of 0.5 was recommended by team 7
    tile.getCropTile().getEvents().trigger("water", 0.5);
    entity.getComponent(WateringCanLevelComponent.class).incrementLevel(-5);
    return true;
  }

  /**
   * Harvests the tile at the given position
   *
   * @param tile the tile to be interacted with
   * @return if harvesting was successful return true else return false
   */
  private boolean harvest(TerrainTile tile) {
    boolean tileHarvestable = isCropTile(tile.getCropTile());
    if (tileHarvestable) {
      tile.getCropTile().getEvents().trigger("harvest");
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
    if (tile.getCropTile() != null) {
      tile.getCropTile().getEvents().trigger("destroy");
      tile.setUnOccupied();
      tile.setCropTile(null);
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
    if (tile.getCropTile() != null || !tile.isTillable()) {
      return false;
    }
    // Make a new tile
    Vector2 newPos = getAdjustedPos(playerPos, mousePos);

    Entity cropTile = createTerrainEntity(newPos);
    tile.setCropTile(cropTile);
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
    if (isCropTile(tile.getCropTile())) {
      tile.getCropTile().getEvents().trigger("fertilise");
      return true;
    }
    return false;
  }

    /**
     * Plants the given seed seed in the tile at the given position
     *
     * @param tile the tile to be interacted with
     * @return if planting was successful return true else return false
     */
  private boolean plant(TerrainTile tile) {
    Function<CropTileComponent, Entity> plantFactoryMethod;
    if (isCropTile(tile.getCropTile())) {
        switch (entity.getComponent(ItemComponent.class).getItemName()) {
            case "aloe vera seed" -> {
                plantFactoryMethod = PlantFactory::createAloeVera;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            case "atomic algae seed" -> {
                plantFactoryMethod = PlantFactory::createAtomicAlgae;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            case "cosmic cob seed" -> {
                plantFactoryMethod = PlantFactory::createCosmicCob;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            case "deadly nightshade seed" -> {
                plantFactoryMethod = PlantFactory::createDeadlyNightshade;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            case "hammer plant seed" -> {
                plantFactoryMethod = PlantFactory::createHammerPlant;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            case "horticultural heater seed" -> {
                plantFactoryMethod = PlantFactory::createHorticulturalHeater;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            case "space snapper seed" -> {
                plantFactoryMethod = PlantFactory::createVenusFlyTrap;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            case "tobacco seed" -> {
                plantFactoryMethod = PlantFactory::createNicotianaTabacum;
                tile.getCropTile().getEvents().trigger("plant", plantFactoryMethod);
            }
            default -> {
                System.out.println("Something went wrong");
                throw new IllegalArgumentException("Explode");
            }
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

  private boolean feed(Entity feedableEntity) {
    if (feedableEntity == null) {
      return false;
    }

    feedableEntity.getEvents().trigger("feed");
    return true;
  }
}
