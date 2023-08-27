package com.csse3200.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
class TerrainCropTileFactoryTest {
  private static Entity tile;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.clear();
    ServiceLocator.registerResourceService(mock(ResourceService.class));
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerPhysicsService(new PhysicsService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
  }

  @Test
  public void cropTileComponentDetected() {
    tile = TerrainCropTileFactory.createTerrainEntity(0,0);
    CropTileComponent cropTileComponent = tile.getComponent(CropTileComponent.class);
    assertNotNull(cropTileComponent);
  }

  @Test
  public void cropTilePositionCorrect() {
    Vector2 position = new Vector2(0,0);
    tile = TerrainCropTileFactory.createTerrainEntity(position);
    assertEquals(position, tile.getPosition());
  }
}
