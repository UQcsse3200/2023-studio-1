package com.csse3200.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.weather.ClimateController;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.Task.Status;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class PanicTaskTest {
    private Entity entity;
    private PhysicsMovementComponent movementComponent;
    private PanicTask panicTask;
    //TestGameArea to register so GameMap can be accessed through the ServiceLocator
    private static final TestGameArea gameArea = spy(new TestGameArea());

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

        // Set up climate controller
        ClimateController climateController = new ClimateController();
        when(gameArea.getClimateController()).thenReturn(climateController);

        //Only needed the assets for the map loading, can be unloaded
        resourceService.unloadAssets(TerrainFactory.getMapTextures());
        resourceService.dispose();
    }

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(1f / 60f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerGameArea(gameArea);

        panicTask = new PanicTask("panicTrigger", 1f, 10, new Vector2(1f, 1f), new Vector2(2f, 2f));
        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(panicTask);
        movementComponent = spy(new PhysicsMovementComponent());
        entity = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(aiTaskComponent)
                .addComponent(movementComponent);
    }

    @Test
    void testStartMovesEntity() {
        entity.create();
        entity.setPosition(10f, 10f); // changed from 0,0 to 10,10 so that entity does not walk out of bounds
        EventListener0 callback = mock(EventListener0.class);
        entity.getEvents().addListener("runStart", callback);

        Vector2 position = entity.getPosition();
        entity.getEvents().trigger("panicTrigger");

        // Run the game for a few cycles
        for (int i = 0; i < 10; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            entity.earlyUpdate();
            entity.update();
        }

        // Check speed changed, position changed, eventTriggered
        verify(movementComponent, atLeast(1)).setMaxSpeed(new Vector2(2f, 2f));
        verify(callback).handle();
        assertNotEquals(position, entity.getPosition());
    }

    @Test
    void testPanicTaskUpdate() {
        entity.create();
        entity.setPosition(10f, 10f); // changed from 0,0 to 10,10 so that entity does not walk out of bounds
        entity.getEvents().trigger("panicTrigger");

        // increase delta to decrease loop size
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(0.25f);
        for(int i = 0; i < 10; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            entity.earlyUpdate();
            entity.update();
        }

        // Verify that the PanicTask renews the movement task, and sets new physicsMovementDirection
        verify(movementComponent, atLeast(3)).setTarget(any(Vector2.class));
    }

    @Test
    void testPanicFinishes() {
        entity.create();
        entity.setPosition(10f, 10f); // changed from 0,0 to 10,10 so that entity does not walk out of bounds
        entity.getEvents().trigger("panicTrigger");

        // increase delta to decrease loop size
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(0.25f);
        for(int i = 0; i < 10; i++) {
            ServiceLocator.getPhysicsService().getPhysics().update();
            entity.earlyUpdate();
            entity.update();
        }

        assertEquals(Status.INACTIVE, panicTask.getStatus());
    }

    @AfterEach
    public void cleanUp() {
        // Clears all loaded services
        ServiceLocator.clear();
    }
}
