package com.csse3200.game.components.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.missions.MissionManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.TimeService;
import com.csse3200.game.utils.math.Vector2Utils;
import jdk.jfr.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.components.items.ItemType;
import com.csse3200.game.components.npc.TamableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.Serial;
import java.util.ArrayList;

@ExtendWith(GameExtension.class)
class TamedFollowTaskTest {
    private TamableComponent tame;
    private ItemComponent fooditem;
    private Entity foodEntity;
    private Entity target;
    private InventoryComponent targetInventory;
    private InventoryComponent targetInvSpy;
    String[] texturePaths = {"images/animals/egg.png"};
    //TestGameArea to register so GameMap can be accessed through the ServiceLocator
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
        gameMap.loadTestTerrain("configs/TestMaps/allDirt20x20_map.txt");

        //Sets the GameMap in the TestGameArea
        gameArea.setGameMap(gameMap);

        //Only needed the assets for the map loading, can be unloaded
        resourceService.unloadAssets(TerrainFactory.getMapTextures());
        resourceService.dispose();
    }

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadTextures(texturePaths);
        TimeService timeService = mock(TimeService.class);
        when(timeService.getEvents()).thenReturn(mock(EventHandler.class));
        ServiceLocator.registerTimeService(timeService);
        ServiceLocator.registerMissionManager(new MissionManager());
        ServiceLocator.getResourceService().loadAll();

        targetInventory = new InventoryComponent(new ArrayList<>());
        targetInvSpy = spy(targetInventory);
        target = new Entity().addComponent(targetInvSpy);
        target.create();

        foodEntity = new Entity(EntityType.ITEM);
        fooditem = new ItemComponent("AFood", ItemType.ANIMAL_FOOD, "images/animals/egg.png");
        //texture is just used as a placeholder.
        //add Animal's favourite food to player/target's spy inventory. (Will be the only item in inventory).
        foodEntity.addComponent(fooditem);
        targetInvSpy.addItem(foodEntity);
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());

        ServiceLocator.registerGameArea(gameArea);
    }

    @Test
    void UntamedAnimalShouldNotFollow() {
        target.setPosition(0f, 6f);

        tame = new TamableComponent(target, 1, 0, "AFood");
        Entity entity = makePhysicsEntity();
        entity.addComponent(tame);
        entity.create();

        entity.setPosition(0f, 0f);
        TamedFollowTask tamedTask = new TamedFollowTask(target, 10, 5, 10, 2f,
                "AFood", Vector2Utils.ONE);

        //Task is not currently active as animal has not been tamed.
        assertTrue(tamedTask.getPriority() < 0);
    }

    @Test
    void testTamedAnimalNotHoldingFavouriteFood() {
        target.setPosition(0f, 6f);
        tame = new TamableComponent(target, 1, 0, "AFood");
        Entity entity = makePhysicsEntity();
        entity.addComponent(tame);
        entity.getComponent(TamableComponent.class).setTame(true);
        entity.create();

        assertTrue(entity.getComponent(TamableComponent.class).isTamed());
        entity.setPosition(0f, 0f);
        TamedFollowTask tamedTask = new TamedFollowTask(target, 10, 5, 10, 2f,
                "AFood", Vector2Utils.ONE);

        entity.setPosition(0f, 0f);
        //Task is not currently active as player is not holding animal's favourite food.
        assertTrue(tamedTask.getPriority() < 0);
    }

    @Test
    void tamedAnimalShouldFollow() {
        target.setPosition(3f, 2f);
        target.getComponent(InventoryComponent.class).setHeldItem(0);
        //player is holding animals favourite food.
        String favouriteFood = "AFood";
        tame = new TamableComponent(target, 1, 0, "AFood");

        AITaskComponent ai = new AITaskComponent().addTask(new TamedFollowTask(target, 10, 10,
                10, 2f, "AFood", Vector2Utils.ONE));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.addComponent(tame);
        entity.getComponent(TamableComponent.class).setTame(true);
        entity.create();
        entity.setPosition(0f, 0f);
        //animal should be tamed
        assertTrue(entity.getComponent(TamableComponent.class).isTamed());

        float initialDistance = entity.getPosition().dst(target.getPosition());
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance < initialDistance);
    }


    @Test
    void tamedAnimalShouldStopatStopDistance() {
        //Will need to be done at stop distance
        //And one more test
        target.setPosition(0f, 2.2f);
        target.getComponent(InventoryComponent.class).setHeldItem(0);
        tame = new TamableComponent(target, 1, 0, "AFood");
        float stoppingDistance = 0.5f;
        TamedFollowTask tameTask =  new TamedFollowTask(target, 10, 10,
                10, stoppingDistance, "AFood", Vector2Utils.ONE);
        AITaskComponent ai = new AITaskComponent().addTask(tameTask);
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.addComponent(tame);
        entity.getComponent(TamableComponent.class).setTame(true);
        entity.create();
        entity.setPosition(0f, 1.5f);
        //animal should be tamed
        assertTrue(entity.getComponent(TamableComponent.class).isTamed());

        for (int i = 0; i < 10; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        //checks if entity stops within specified stopping distance +- (0.10)
        assertTrue(Math.abs(stoppingDistance - newDistance) < 0.10);
    }

    @Test
    void testTamedAnimalShouldFollowinRange() {
        target.setPosition(0f, 20f);
        target.getComponent(InventoryComponent.class).setHeldItem(0); //holding animal's favourite food
        tame = new TamableComponent(target, 1, 0, "AFood");

        Entity entity = makePhysicsEntity();
        entity.addComponent(tame);
        entity.getComponent(TamableComponent.class).setTame(true);
        entity.create();
        entity.setPosition(0f, 2f);
        int priorityVal = 10;

        TamedFollowTask tamedTask = new TamedFollowTask(target, priorityVal, 10, 10, 2f,
                "AFood", Vector2Utils.ONE);
        tamedTask.create(() -> entity);

        //target is out of distance
        assertTrue(tamedTask.getPriority() < 0);

        //target is too close to entity
        target.setPosition(0f, 4f);
        tamedTask.start();
        tamedTask.update();

        // Check tamedTask still active but not moving
        assertEquals(priorityVal, tamedTask.getPriority());
        verify(entity.getComponent(PhysicsMovementComponent.class)).setEnabled(false);
        tamedTask.stop();

        //task priority should be set to normal priority when task is active.
        target.setPosition(7f, 2f);
        tamedTask.start();
        assertEquals(priorityVal, tamedTask.getPriority());

        // too close
        target.setPosition(0f, 3f);
        tamedTask.update();

        // Check tamedTask still active but not moving
        assertEquals(priorityVal, tamedTask.getPriority());
        verify(entity.getComponent(PhysicsMovementComponent.class), times(2)).setEnabled(false);

        //task priority should be re-triggered when target moves within range.
        target.setPosition(7f, 2f);
        tamedTask.update();
        assertEquals(priorityVal, tamedTask.getPriority());

        // When active, should not follow outside follow distance
        target.setPosition(0f, 20f);
        assertTrue(tamedTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(spy(new PhysicsMovementComponent()));
    }

    @AfterEach
    public void cleanUp() {
        // Clears all loaded services
        ServiceLocator.clear();
    }
}
