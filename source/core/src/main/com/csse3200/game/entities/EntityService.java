package com.csse3200.game.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.Array;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 *
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class EntityService {
  private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
  private static final int INITIAL_CAPACITY = 16;
  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);
  private static boolean paused = false;
  private static boolean pauseStartFlag = false;
  private static boolean pauseEndFlag = false;

  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    entities.add(entity);
    entity.create();
  }

  /**
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   * @param entity entity to be removed.
   */
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    entities.removeValue(entity, true);
  }
  
  /**
   * Update all registered entities. Should only be called from the main game loop.
   */
  public void update() {
    for (Entity entity : entities) {
      if (!paused) {
        if(pauseEndFlag) {
          entity.togglePauseAnimations(true);
        }
        entity.earlyUpdate();
        entity.update();
      }
      if(pauseStartFlag) {
        entity.togglePauseAnimations(true);
      }
    }
    EntityService.pauseStartFlag = false;
    EntityService.pauseEndFlag = false;

  }

  public static void pauseAndResume() {
    paused = !paused;
    if (paused) {
      pauseStartFlag = true;
    } else {
      pauseEndFlag = true;
    }
  }

  public static void pauseGame() {
    paused = true;
    pauseStartFlag = true;
  }

  public static void unpauseGame() {
    paused = false;
    pauseEndFlag = true;
  }

  public static boolean pauseCheck() {
    return paused;
  }

  /**
   * Dispose all entities.
   */
  public void dispose() {
    for (Entity entity : entities) {
      entity.dispose();
    }
  }

  /**
   * Get number of entities registered
   *
   * @return number of entities
   */
  public int getSize() {
    return entities.size;
  }

   /**
   * Returns Array of entities of all entities in game
   * @return Array of entities in game
   */
  public Array<Entity> getEntities() {
    return entities;
  }
}
