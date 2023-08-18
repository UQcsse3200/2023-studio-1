package com.csse3200.game.areas.terrain;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class TerrainCropTileFactory implements TerrainEntityFactory {

  /**
   * Creates a terrain entity
   *
   * @param x x-position of the entity to be created
   * @param y y-position of the entity to be created
   * @return created entity
   */
  @Override
  public Entity CreateTerrainEntity(int x, int y) {
    Entity tile = new Entity()
        .addComponent(new ColliderComponent())
        .addComponent(new TextureRenderComponent("images/tool_shovel.png"));

    // Implement crop tile component
    // Change sprite used in texture
    // Load in tile config

    PhysicsUtils.setScaledCollider(tile, 1.0f, 1.0f);

    return tile;
  }
}
