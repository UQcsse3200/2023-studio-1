package com.csse3200.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class  GameArea implements Disposable {
  protected TerrainComponent terrain;
  protected List<Entity> areaEntities;
  private static final Logger logger = LoggerFactory.getLogger(GameArea.class);
  private Entity player;
  private final ArrayList<EntityType> loadableTypes = new ArrayList<>(Arrays.asList(EntityType.Tile, EntityType.Cow,
          EntityType.Cow, EntityType.Chicken, EntityType.Astrolotl, EntityType.Plant, EntityType.Tile,
          EntityType.OxygenEater, EntityType.ShipDebris, EntityType.Ship, EntityType.Sprinkler, EntityType.Pump,
          EntityType.Fence, EntityType.Light, EntityType.Gate, EntityType.Chest));

  protected GameArea() {
    areaEntities = new ArrayList<>();
  }

  /** Create the game area in the world. */
  public abstract void create();

  /** Dispose of all internal entities in the area */
  public void dispose() {
    for (Entity entity : areaEntities) {
      entity.dispose();
    }
  }

  /**
   * Spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  public void spawnEntity(Entity entity) {
    areaEntities.add(entity);
    ServiceLocator.getEntityService().register(entity);
  }

  /**
   * de-spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  protected void deSpawnEntity(Entity entity) {
    areaEntities.remove(entity);
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   *
   * @param entity Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param centerX true to center entity X on the tile, false to align the bottom left corner
   * @param centerY true to center entity Y on the tile, false to align the bottom left corner
   */
  public void spawnEntityAt(
      Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
    float tileSize = terrain.getTileSize();

    if (centerX) {
      worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
    }
    if (centerY) {
      worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
    }

    entity.setPosition(worldPos);
    spawnEntity(entity);
  }



  public void removeEntity(Entity entity) {
    entity.setEnabled(false);
    areaEntities.remove(entity);
    Gdx.app.postRunnable(entity::dispose);
  }

  /**
   * Loops through the games npcs and removes them. Removes all
   *  animals, crop tiles, and plants from the game.
   * @param entities Array of entities currently in game.
   */
  public void removeLoadableEntities(Array<Entity> entities) {
    for (Entity e : entities) {
      if (loadableTypes.contains(e.getType())) {
        removeEntity(e);
      }
    }
  }

  public Entity getPlayer() {
    return player;
  }
 // public abstract Entity getPlayer();

  public abstract ClimateController getClimateController();

  public abstract Entity getTractor();

  public abstract GameMap getMap();

  public void setPlayer(Entity customPlayer) { player = customPlayer; }

  public ArrayList<EntityType> getLoadableTypes() {
    return loadableTypes;
  }
}
