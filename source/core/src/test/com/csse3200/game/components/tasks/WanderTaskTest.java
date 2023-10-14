package com.csse3200.game.components.tasks;

import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WanderTaskTest {
  //TestGameArea to register so GameMap can be accessed through the ServiceLocator
  private static final TestGameArea gameArea = new TestGameArea();
  @Mock
  GameTime gameTime;

  @BeforeAll
  static void setupGameAreaAndMap() {
    //necessary for allowing the Terrain factory to properly generate the map with correct tile dimensions
    ResourceService resourceService = new ResourceService();
    resourceService.loadTextures(TerrainFactory.getMapTextures());
    resourceService.loadAll();
    ServiceLocator.registerResourceService(resourceService);

    //Loads the test terrain into the GameMap
    TerrainComponent terrainComponent = mock(TerrainComponent.class);
    doReturn(TerrainFactory.WORLD_TILE_SIZE).when(terrainComponent).getTileSize();
    GameMap gameMap = new GameMap(new TerrainFactory(new CameraComponent()));
    gameMap.setTerrainComponent(terrainComponent);
    gameMap.loadTestTerrain("configs/TestMaps/allDirt20x20_map.txt");

    //Sets the GameMap in the TestGameArea
    gameArea.setGameMap(gameMap);

    //Only needed the assets for the map loading, can be unloaded
    resourceService.unloadAssets(TerrainFactory.getMapTextures());
    resourceService.dispose();
  }

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerGameArea(gameArea);
  }

  @Test
  void shouldTriggerEvent() {
    WanderTask wanderTask = new WanderTask(Vector2Utils.ONE, 1f);

    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(wanderTask);
    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
    entity.create();

    // Register callbacks
    EventListener0 callback = mock(EventListener0.class);
    entity.getEvents().addListener("wanderStart", callback);

    wanderTask.start();

    verify(callback).handle();
  }
}