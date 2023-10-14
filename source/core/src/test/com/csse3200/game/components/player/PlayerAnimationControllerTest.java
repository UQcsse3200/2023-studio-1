package com.csse3200.game.components.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;


@ExtendWith(GameExtension.class)
class PlayerAnimationControllerTest {
    private Entity player;

    private AnimationRenderComponent animationRenderComponent;

    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerRenderService(new RenderService());
        ResourceService resourceService = new ResourceService();
        resourceService.loadTextureAtlases(new String[]{"images/player.atlas"});
        resourceService.loadTextures(new String[]{"images/heart.png"});
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);

        animationRenderComponent = spy(
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/player.atlas", TextureAtlas.class)
                )
        );

        PlayerFactory.setupPlayerAnimator(animationRenderComponent);

        player = new Entity()
                .addComponent(animationRenderComponent)
                .addComponent(new PlayerAnimationController());

        player.getComponent(AnimationRenderComponent.class).scaleEntity();
        player.create();
    }

    @ParameterizedTest(name = "{2} animation played correctly on {0} event trigger")
    @MethodSource({"shouldStartMoveAnimationOnEventTriggerParams"})
    void shouldStartMoveAnimationOnEventTrigger(String animationEvent, String movementDirection, String expectedAnimationName) {
        player.getEvents().trigger(animationEvent, movementDirection);
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
    }

    private static Stream<Arguments> shouldStartMoveAnimationOnEventTriggerParams() {
        return Stream.of(
                // (animationEvent, movementDirection, expectedAnimationName)
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "up", "walk_up"),
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "left", "walk_left"),
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "down", "walk_down"),
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "right", "walk_right"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "up", "run_up"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "left", "run_left"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "down", "run_down"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "right", "run_right")
        );
    }

    @ParameterizedTest(name = "{2} animation isn't reset on subsequent triggers for the same event")
    @MethodSource({"shouldNotResetAnimationParams"})
    void shouldNotResetAnimation(String animationEvent, String movementDirection, String expectedAnimationName) {
        player.getEvents().trigger(animationEvent, movementDirection);
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
        verify(animationRenderComponent).startAnimation(expectedAnimationName);

        // trigger the event again
        player.getEvents().trigger(animationEvent, movementDirection);
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
        // should still have only been called once
        verify(animationRenderComponent).startAnimation(expectedAnimationName);
    }

    private static Stream<Arguments> shouldNotResetAnimationParams() {
        return Stream.of(
                // (animationEvent, movementDirection, expectedAnimationName)
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "up", "walk_up"),
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "left", "walk_left"),
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "down", "walk_down"),
                arguments(PlayerAnimationController.events.ANIMATION_WALK_START.name(), "right", "walk_right"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "up", "run_up"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "left", "run_left"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "down", "run_down"),
                arguments(PlayerAnimationController.events.ANIMATION_RUN_START.name(), "right", "run_right")

        );
    }
}
