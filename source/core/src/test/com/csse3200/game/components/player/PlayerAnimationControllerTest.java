package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@ExtendWith(GameExtension.class)
public class PlayerAnimationControllerTest {
    private Entity player;

    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerPhysicsService(new PhysicsService());

        ResourceService resourceService = new ResourceService();
        resourceService.loadTextureAtlases(new String[]{"images/player.atlas"});
        resourceService.loadTextures(new String[]{"images/heart.png"});
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);

        player = MockPlayerFactory.createPlayer();
        player.create();
    }

    @ParameterizedTest(name = "{1} animation played correctly")
    @MethodSource({"testParams"})
    void shouldPlayCorrectWalkAnimation(int inputKey, String expectedAnimationName) {
        KeyboardPlayerInputComponent inputComponent = player.getComponent(KeyboardPlayerInputComponent.class);
        AnimationRenderComponent animationRenderComponent = player.getComponent(AnimationRenderComponent.class);

        inputComponent.keyDown(inputKey);
        player.update();
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
    }

    private static Stream<Arguments> testParams() {
        return Stream.of(
                // (inputKey, expectedAnimationName)
                arguments(Keys.W, "walk_up"),
                arguments(Keys.A, "walk_left"),
                arguments(Keys.S, "walk_down"),
                arguments(Keys.D, "walk_right")
        );
    }
}
