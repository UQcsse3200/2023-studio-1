package com.csse3200.game.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;

@ExtendWith(GameExtension.class)
class ServiceLocatorTest {
  @Test
  void shouldGetSetServices() {
    EntityService entityService = new EntityService();
    RenderService renderService = new RenderService();
    PhysicsService physicsService = mock(PhysicsService.class);
    SaveLoadService saveLoadService = new SaveLoadService();
    GameTime gameTime = new GameTime();

    ServiceLocator.registerEntityService(entityService);
    ServiceLocator.registerRenderService(renderService);
    ServiceLocator.registerPhysicsService(physicsService);
    ServiceLocator.registerSaveLoadService(saveLoadService);
    ServiceLocator.registerTimeSource(gameTime);

    assertEquals(ServiceLocator.getEntityService(), entityService);
    assertEquals(ServiceLocator.getRenderService(), renderService);
    assertEquals(ServiceLocator.getPhysicsService(), physicsService);
    assertEquals(ServiceLocator.getSaveLoadService(), saveLoadService);
    assertEquals(ServiceLocator.getTimeSource(), gameTime);

    ServiceLocator.clear();
    assertNull(ServiceLocator.getEntityService());
    assertNull(ServiceLocator.getRenderService());
    assertNull(ServiceLocator.getPhysicsService());
    assertNull(ServiceLocator.getTimeSource());
  }
}