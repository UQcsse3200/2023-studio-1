package com.csse3200.game.areas.terrain;

import com.csse3200.game.entities.Entity;


/**
 * Abstract factory that is inherited by factories that create Terrain Entities
 */
public interface TerrainEntityFactory {

  /**
   * Creates a terrain entity
   * @param x x-position of the entity to be created
   * @param y y-position of the entity to be created
   * @return created entity
   */
  public Entity CreateTerrainEntity(float x, float y);
}
