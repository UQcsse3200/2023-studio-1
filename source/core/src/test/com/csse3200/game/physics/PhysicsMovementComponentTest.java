package com.csse3200.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(GameExtension.class)
class PhysicsMovementComponentTest {
    private Entity flyingEntity;
    private Entity nonFlyingEntity;
    private static final TestGameArea gameArea = new TestGameArea();

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
        gameMap.loadTestTerrain("configs/TestMaps/playerActionsTest_map.txt");

        //Sets the GameMap in the TestGameArea
        gameArea.setGameMap(gameMap);

        //Only needed the assets for the map loading, can be unloaded
        resourceService.unloadAssets(TerrainFactory.getMapTextures());
        resourceService.dispose();
    }

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        PhysicsService physicsService = new PhysicsService();

        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerResourceService(mock(ResourceService.class));
        ServiceLocator.registerEntityService(mock(EntityService.class));
        ServiceLocator.registerGameArea(gameArea);
    }

    @Test
    void nonFlyingEntitiesMovementModifiersTest() {
        nonFlyingEntity = new Entity(EntityType.DUMMY);
        nonFlyingEntity.addComponent(new PhysicsComponent());
        nonFlyingEntity.addComponent(new PhysicsMovementComponent());
        gameArea.spawnEntity(nonFlyingEntity);

        Vector2 moveVector = new Vector2(1,0); // moving right
        PhysicsMovementComponent physicsMovementComponent = nonFlyingEntity.getComponent(
                PhysicsMovementComponent.class);

        nonFlyingEntity.setPosition(new Vector2(1, 18));
        physicsMovementComponent.setTarget(new Vector2(5, 18));
        // scaled by max speed and dirt movement modifier of 1
        Vector2 walkRightOnDirtVector = new Vector2(moveVector).scl(1).scl(1);
        assertEquals(walkRightOnDirtVector, physicsMovementComponent.calculateVelocityVector());

        nonFlyingEntity.setPosition(new Vector2(1, 14));
        physicsMovementComponent.setTarget(new Vector2(5, 14));
        // scaled by max speed and path movement modifier of 1.2
        Vector2 walkRightOnPathVector = new Vector2(moveVector).scl(1).scl(1.2f);
        assertEquals(walkRightOnPathVector, physicsMovementComponent.calculateVelocityVector());

        nonFlyingEntity.setPosition(new Vector2(1, 10));
        physicsMovementComponent.setTarget(new Vector2(5, 10));
        // scaled by max speed and snow movement modifier of 0.8
        Vector2 walkRightOnSnowVector = new Vector2(moveVector).scl(1).scl(0.8f);
        assertEquals(walkRightOnSnowVector, physicsMovementComponent.calculateVelocityVector());

        nonFlyingEntity.setPosition(new Vector2(1, 6));
        physicsMovementComponent.setTarget(new Vector2(5, 6));
        // scaled by max speed and ice movement modifier of 1.5
        Vector2 walkRightOnIceVector = new Vector2(moveVector).scl(1).scl(1.5f);
        assertEquals(walkRightOnIceVector, physicsMovementComponent.calculateVelocityVector());

        nonFlyingEntity.setPosition(new Vector2(1, 2));
        physicsMovementComponent.setTarget(new Vector2(5, 2));
        // scaled by max speed and flowing water movement modifier of 0.4
        Vector2 walkRightOnFlowingWaterVector = new Vector2(moveVector).scl(1).scl(0.4f);
        assertEquals(walkRightOnFlowingWaterVector, physicsMovementComponent.calculateVelocityVector());
    }

    @Test
    void flyingEntitiesMovementModifiersTest() {
        flyingEntity = new Entity(EntityType.BAT);
        flyingEntity.addComponent(new PhysicsComponent());
        flyingEntity.addComponent(new PhysicsMovementComponent());
        gameArea.spawnEntity(flyingEntity);

        Vector2 moveVector = new Vector2(1,0); // moving right
        PhysicsMovementComponent physicsMovementComponent = flyingEntity.getComponent(
                PhysicsMovementComponent.class);

        flyingEntity.setPosition(new Vector2(1, 18));
        physicsMovementComponent.setTarget(new Vector2(5, 18));
        // scaled by max speed and movement modifier of 1
        Vector2 walkRightOnDirtVector = new Vector2(moveVector).scl(1).scl(1);
        assertEquals(walkRightOnDirtVector, physicsMovementComponent.calculateVelocityVector());

        flyingEntity.setPosition(new Vector2(1, 14));
        physicsMovementComponent.setTarget(new Vector2(5, 14));
        // scaled by max speed and movement modifier of 1
        Vector2 walkRightOnPathVector = new Vector2(moveVector).scl(1).scl(1);
        assertEquals(walkRightOnPathVector, physicsMovementComponent.calculateVelocityVector());

        flyingEntity.setPosition(new Vector2(1, 10));
        physicsMovementComponent.setTarget(new Vector2(5, 10));
        // scaled by max speed and movement modifier of 1
        Vector2 walkRightOnSnowVector = new Vector2(moveVector).scl(1).scl(1);
        assertEquals(walkRightOnSnowVector, physicsMovementComponent.calculateVelocityVector());

        flyingEntity.setPosition(new Vector2(1, 6));
        physicsMovementComponent.setTarget(new Vector2(5, 6));
        // scaled by max speed and movement modifier of 1
        Vector2 walkRightOnIceVector = new Vector2(moveVector).scl(1).scl(1);
        assertEquals(walkRightOnIceVector, physicsMovementComponent.calculateVelocityVector());

        flyingEntity.setPosition(new Vector2(1, 2));
        physicsMovementComponent.setTarget(new Vector2(5, 2));
        // scaled by max speed and movement modifier of 1
        Vector2 walkRightOnFlowingWaterVector = new Vector2(moveVector).scl(1).scl(1);
        assertEquals(walkRightOnFlowingWaterVector, physicsMovementComponent.calculateVelocityVector());
    }

    @AfterEach
    public void cleanUp() {
        // Clears all loaded services
        ServiceLocator.clear();
    }
}
