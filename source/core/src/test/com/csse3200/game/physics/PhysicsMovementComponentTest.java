//package com.csse3200.game.physics;
//
//import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.csse3200.game.areas.TestGameArea;
//import com.csse3200.game.areas.terrain.GameMap;
//import com.csse3200.game.areas.terrain.TerrainComponent;
//import com.csse3200.game.areas.terrain.TerrainFactory;
//import com.csse3200.game.components.CameraComponent;
//import com.csse3200.game.components.combat.CombatStatsComponent;
//import com.csse3200.game.components.player.PlayerActions;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.entities.EntityService;
//import com.csse3200.game.extensions.GameExtension;
//import com.csse3200.game.physics.components.PhysicsComponent;
//import com.csse3200.game.services.GameTime;
//import com.csse3200.game.services.ResourceService;
//import com.csse3200.game.services.ServiceLocator;
//import com.csse3200.game.services.TimeService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.doReturn;
//
//@ExtendWith(GameExtension.class)
//class PhysicsMovementComponentTest {
//    private Entity flyingEntity;
//    private Entity nonFlyingEntity;
//    private static final TestGameArea gameArea = new TestGameArea();
//
//    @BeforeAll
//    static void setupGameAreaAndMap() {
//        //necessary for allowing the Terrain factory to properly generate the map with correct tile dimensions
//        ResourceService resourceService = new ResourceService();
//        resourceService.loadTextures(TerrainFactory.getMapTextures());
//        resourceService.loadAll();
//        ServiceLocator.registerResourceService(resourceService);
//
//        //Loads the test terrain into the GameMap
//        TerrainComponent terrainComponent = mock(TerrainComponent.class);
//        doReturn(TerrainFactory.WORLD_TILE_SIZE).when(terrainComponent).getTileSize();
//        GameMap gameMap = new GameMap(new TerrainFactory(new CameraComponent()));
//        gameMap.setTerrainComponent(terrainComponent);
//        gameMap.loadTestTerrain("configs/TestMaps/playerActionsTest_map.txt");
//
//        //Sets the GameMap in the TestGameArea
//        gameArea.setGameMap(gameMap);
//
//        //Only needed the assets for the map loading, can be unloaded
//        resourceService.unloadAssets(TerrainFactory.getMapTextures());
//        resourceService.dispose();
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        // Mock rendering, physics, game time
//        PhysicsService physicsService = new PhysicsService();
//
//        ServiceLocator.registerTimeSource(new GameTime());
//
//        ServiceLocator.registerPhysicsService(physicsService);
//        ServiceLocator.registerResourceService(mock(ResourceService.class));
//
//        ServiceLocator.registerEntityService(mock(EntityService.class));
//        ServiceLocator.registerTimeService(new TimeService());
//
//        ServiceLocator.registerGameArea(gameArea);
//
//        flyingEntity = new Entity();
//        nonFlyingEntity = new Entity();
//
//        flyingEntity.setPosition(new Vector2(8, 10));
//        flyingEntity.setPosition(new Vector2(12, 10));
//
//        //player.addComponent(new PlayerActions());
//
//        gameArea.spawnEntity(flyingEntity);
//        gameArea.spawnEntity(nonFlyingEntity);
//
//        when(ServiceLocator.getResourceService().getAsset(any(), any())).thenReturn(mock(Sound.class));
//
//        ServiceLocator.registerCameraComponent(mock(CameraComponent.class));
//        when(ServiceLocator.getCameraComponent().screenPositionToWorldPosition(any())).thenReturn(new Vector2(2, 2));
//    }
//}
