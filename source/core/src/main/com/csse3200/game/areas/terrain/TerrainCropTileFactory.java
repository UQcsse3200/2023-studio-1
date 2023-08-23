package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CropTileConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerrainCropTileFactory{

  private static final Logger logger = LoggerFactory.getLogger(TerrainCropTileFactory.class);

  private static final CropTileConfig stats =
      FileLoader.readClass(CropTileConfig.class, "configs/cropTile.json");

  /**
   * Creates a crop tile entity
   *
   * @param x x-position of the entity to be created
   * @param y y-position of the entity to be created
   * @return created crop tile entity
   */
  public static Entity createTerrainEntity(float x, float y) {
    Vector2 position = new Vector2(x, y);
    return createTerrainEntity(position);
  }

  /**
   * Creates a crop tile entity
   *
   * @param position position of the entity to be created
   * @return created crop tile entity
   */

  public static Entity createTerrainEntity(Vector2 position) {
    logger.debug("Creating crop tile at position {}", position);

    Entity tile = new Entity()
        .addComponent(new ColliderComponent())
        .addComponent(new PhysicsComponent())
        // TODO Change Sprite used in RenderComponent - waiting on #27
        .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
        .addComponent(new CropTileComponent(1,1));
        // TODO Set CropTileComponent params to values in  CropTileConfig class



    // Sets ColliderComponent to sensor and not physics collider
    tile.getComponent(ColliderComponent.class).setSensor(true);

    tile.setPosition(position);
    logger.debug("Registering crop tile {} with entity service", tile);
    ServiceLocator.getEntityService().register(tile);

    return tile;
  }
}
