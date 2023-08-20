package com.csse3200.game.areas.terrain;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CropTileConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerrainCropTileFactory implements TerrainEntityFactory {

  private static final Logger logger = LoggerFactory.getLogger(TerrainCropTileFactory.class);

  private static final CropTileConfig stats =
      FileLoader.readClass(CropTileConfig.class, "configs/cropTile.json");

  /**
   * Creates a crop tile entity
   *
   * @param x x-position of the entity to be created
   * @param y y-position of the entity to be created
   * @return created entity
   */
  @Override
  public Entity CreateTerrainEntity(float x, float y) {
    logger.debug("Creating crop tile at position ({},{})", x, y);

    Entity tile = new Entity()
        // TODO Change Sprite used in RenderComponent - waiting on #27
        .addComponent(new TextureRenderComponent("images/tool_shovel.png"))
        .addComponent(new CropTileComponent(1,1));
        // TODO Set CropTileComponent params to values in  CropTileConfig class

    // Adds ColliderComponent as sensor and not physics collider
    ColliderComponent sensor = new ColliderComponent();
    sensor.setSensor(true);
    tile.addComponent(sensor);

    tile.setPosition(x, y);
    logger.debug("Registering crop tile {} with entity service", tile);
    ServiceLocator.getEntityService().register(tile);

    return tile;
  }
}
