package com.csse3200.game.areas.terrain;

import com.csse3200.game.entities.Entity;


/**
 * Abstract factory that is inherited by factories that create Terrain Entities
 */
public abstract class TerrainEntityFactory {

  /**
   * Creates terrain entity
   * @return terrain entity
   */
  public abstract Entity CreateTerrainEntity(int x, int y);
}
