package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainTile;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.combat.ProjectileComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class PlayerActionsTest {
    private List<Entity> areaEntities;
    private Entity player;
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

        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerResourceService(mock(ResourceService.class));

        ServiceLocator.registerEntityService(mock(EntityService.class));
        ServiceLocator.registerTimeService(new TimeService());

        //ServiceLocator.registerGameArea(mock(GameArea.class));
        ServiceLocator.registerGameArea(gameArea);

        areaEntities = new ArrayList<>();
        player = new Entity();
        player.addComponent(new PlayerActions());
        player.setPosition(0,0);
        Entity chicken = new Entity();
        chicken.addComponent(new CombatStatsComponent(10, 0));
        chicken.setPosition(1,1);
        areaEntities.add(player);
        areaEntities.add(chicken);

        gameArea.setPlayer(player);
        gameArea.spawnEntity(player);
        gameArea.spawnEntity(chicken);

        //when(ServiceLocator.getGameArea().getAreaEntities()).thenReturn(areaEntities);
        when(ServiceLocator.getResourceService().getAsset(any(), any())).thenReturn(mock(Sound.class));

        ServiceLocator.registerCameraComponent(mock(CameraComponent.class));
        when(ServiceLocator.getCameraComponent().screenPositionToWorldPosition(any())).thenReturn(new Vector2(2, 2));
    }

    @Test
    void playerShouldAttackNPC() {
        PlayerActions playerActions = player.getComponent(PlayerActions.class);
        playerActions.attack(new Vector2(2,2));
        assertEquals(5, areaEntities.get(1).getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void playerShouldShootNPC() {
        Entity bullet = new Entity();
        bullet.addComponent(mock(ProjectileComponent.class));
        bullet.addComponent(new CombatStatsComponent(1, 1));
        mockStatic(ProjectileFactory.class);
        when(ProjectileFactory.createPlayerProjectile()).thenReturn(bullet);
        doAnswer((i) -> {
            areaEntities.add(bullet);
            return null;
        }).when(mock(GameArea.class)).spawnEntity(any());
        PlayerActions playerActions = player.getComponent(PlayerActions.class);
        playerActions.shoot(new Vector2(2,2));
        assertEquals(2, areaEntities.size());
    }

    @Test
    void playerMovementWalkTest() {
        Vector2 moveVector = new Vector2(1,0); // moving right
        PlayerActions playerActionsComponent = player.getComponent(PlayerActions.class);
        playerActionsComponent.setMoveDirection(moveVector);
        playerActionsComponent.stopRunning(); // sets player to walking speed

        player.setPosition(new Vector2(1, 18));
        // scaled by walking speed and dirt movement modifier of 1
        Vector2 walkRightOnDirtVector = new Vector2(moveVector).scl(3).scl(1);
        assertEquals(walkRightOnDirtVector, playerActionsComponent.calculateVelocityVector());

        player.setPosition(new Vector2(1, 14));
        // scaled by walking speed and path movement modifier of 1.2
        Vector2 walkRightOnPathVector = new Vector2(moveVector).scl(3).scl(1.2f);
        assertEquals(walkRightOnPathVector, playerActionsComponent.calculateVelocityVector());

        player.setPosition(new Vector2(1, 10));
        System.out.println(player.getPosition());
        // scaled by walking speed and snow movement modifier of 0.8
        Vector2 walkRightOnSnowVector = new Vector2(moveVector).scl(3).scl(0.8f);
        assertEquals(walkRightOnSnowVector, playerActionsComponent.calculateVelocityVector());

        player.setPosition(new Vector2(1, 6));
        // scaled by walking speed and ice movement modifier of 1.5
        Vector2 walkRightOnIceVector = new Vector2(moveVector).scl(3).scl(1.5f);
        assertEquals(walkRightOnIceVector, playerActionsComponent.calculateVelocityVector());

        player.setPosition(new Vector2(1, 2));
        // scaled by walking speed and flowing water movement modifier of 0.4
        Vector2 walkRightOnFlowingWaterVector = new Vector2(moveVector).scl(3).scl(0.4f);
        assertEquals(walkRightOnFlowingWaterVector, playerActionsComponent.calculateVelocityVector());
    }

    @AfterEach
    public void cleanUp() {
        // Clears all loaded services
        List<Entity> areaEntitiesList = gameArea.getAreaEntities();
        Array<Entity> areaEntitiesArray = new Array<>(areaEntitiesList.size());
        for (Entity entity : areaEntitiesList) {
            areaEntitiesArray.add(entity);
        }

        gameArea.removeLoadableEntities(areaEntitiesArray);
        ServiceLocator.clear();
    }
}
