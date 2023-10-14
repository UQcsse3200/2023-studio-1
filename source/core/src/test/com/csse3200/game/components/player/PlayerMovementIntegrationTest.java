package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.stream.Stream;

import com.csse3200.game.areas.TestGameArea;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;


@ExtendWith(GameExtension.class)
class PlayerMovementIntegrationTest {
    private Entity player;
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
        gameMap.loadTestTerrain("configs/TestMaps/exampleTestMap.txt");

        //Sets the GameMap in the TestGameArea
        gameArea.setGameMap(gameMap);

        //Only needed the assets for the map loading, can be unloaded
        resourceService.unloadAssets(TerrainFactory.getMapTextures());
        resourceService.dispose();
    }

    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerGameArea(gameArea);

        ResourceService resourceService = new ResourceService();
        resourceService.loadTextureAtlases(new String[]{"images/player.atlas"});
        resourceService.loadTextures(new String[]{"images/heart.png"});
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/player.atlas", TextureAtlas.class)
                );

        PlayerFactory.setupPlayerAnimator(animator);

        player = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PlayerActions())
                .addComponent(animator)
                .addComponent(new PlayerAnimationController());

        player.getComponent(AnimationRenderComponent.class).scaleEntity();
        player.create();
    }

    @ParameterizedTest(name = "{2} animation played correctly when {0}")
    @MethodSource({"shouldPlayCorrectMoveAnimationParams"})
    void shouldPlayCorrectMoveAnimation(Vector2 moveDirection, boolean isRunning, String expectedAnimationName) {
        AnimationRenderComponent animationRenderComponent = player.getComponent(AnimationRenderComponent.class);
        PlayerActions playerActionsComponent = player.getComponent(PlayerActions.class);

        if (isRunning) player.getEvents().trigger(PlayerActions.events.RUN.name());
        player.getEvents().trigger(PlayerActions.events.MOVE.name(), moveDirection);
        playerActionsComponent.update();
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
    }

    private static Stream<Arguments> shouldPlayCorrectMoveAnimationParams() {
        return Stream.of(
                // ((testDescription, moveDirection), isRunning, expectedAnimationName)
                arguments(named("walking up", Vector2Utils.UP), false, "walk_up"),
                arguments(named("walking left", Vector2Utils.LEFT), false, "walk_left"),
                arguments(named("walking down", Vector2Utils.DOWN), false, "walk_down"),
                arguments(named("walking right", Vector2Utils.RIGHT), false, "walk_right"),
                arguments(named("walking up & right", Vector2Utils.UP.add(Vector2Utils.RIGHT)), false, "walk_up"),
                arguments(named("walking up & left", Vector2Utils.UP.add(Vector2Utils.LEFT)), false, "walk_up"),
                arguments(named("walking down & right", Vector2Utils.DOWN.add(Vector2Utils.RIGHT)), false, "walk_down"),
                arguments(named("walking down & left", Vector2Utils.DOWN.add(Vector2Utils.LEFT)), false, "walk_down"),
                arguments(named("running up", Vector2Utils.UP), true, "run_up"),
                arguments(named("running left", Vector2Utils.LEFT), true, "run_left"),
                arguments(named("running down", Vector2Utils.DOWN), true, "run_down"),
                arguments(named("running right", Vector2Utils.RIGHT), true, "run_right"),
                arguments(named("running up & right", Vector2Utils.UP.add(Vector2Utils.RIGHT)), true, "run_up"),
                arguments(named("running up & left", Vector2Utils.UP.add(Vector2Utils.LEFT)), true, "run_up"),
                arguments(named("running down & right", Vector2Utils.DOWN.add(Vector2Utils.RIGHT)), true, "run_down"),
                arguments(named("running down & left", Vector2Utils.DOWN.add(Vector2Utils.LEFT)), true, "run_down")
        );
    }

    @ParameterizedTest(name = "default animation played correctly when stopped after {0}")
    @MethodSource({"shouldReturnToDefaultAnimationOnStopParams"})
    void shouldReturnToDefaultAnimationOnStop(String prevDirection, String expectedAnimation, int randomiser) {
        AnimationRenderComponent animationRenderComponent = player.getComponent(AnimationRenderComponent.class);
        player.getEvents().trigger(PlayerAnimationController.events.ANIMATION_WALK_STOP.name(),prevDirection, randomiser, true );
        assertEquals(expectedAnimation, animationRenderComponent.getCurrentAnimation());
    }

    private static Stream<Arguments> shouldReturnToDefaultAnimationOnStopParams() {
        return Stream.of(
                // ((testDescription, prev moveDirection), isRunning, expectedAnimationName)
                arguments("right", "snooze_right", 5 ),
                arguments("right", "blink_right", 55 ),
                arguments("right", "yawn_right", 30 ),
                arguments("left", "snooze_left", 5 ),
                arguments("left", "blink_left", 55 ),
                arguments("left", "yawn_left", 30 ),
                arguments("down", "snooze_down", 5 ),
                arguments("down", "blink_down", 55 ),
                arguments("down", "yawn_down", 30 ),
                arguments("up", "snooze_up", 5 ),
                arguments("up", "blink_up", 55 ),
                arguments("up", "yawn_up", 30 )
        );
    }

    @AfterEach
    public void cleanUp() {
        // Clears all loaded services
        ServiceLocator.clear();
    }
}
