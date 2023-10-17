package com.csse3200.game.components.tractor;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TractorFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.entities.EntityType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class TractorAnimationControllerTest {
    private Entity tractor;
    private AnimationRenderComponent animationRenderComponent;


    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerRenderService(new RenderService());
        ResourceService resourceService = new ResourceService();
        resourceService.loadTextureAtlases(new String[]{"images/tractor.atlas"});
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);

        animationRenderComponent = spy(TractorFactory.setupTractorAnimations());


        tractor = new Entity(EntityType.TRACTOR)
                .addComponent(animationRenderComponent)
                .addComponent(new TractorAnimationController());
        tractor.getComponent(AnimationRenderComponent.class).scaleEntity();
        tractor.create();
    }

    @ParameterizedTest(name = "{2} animation played correctly when {0} event is triggered")
    @MethodSource("shouldUpdateAnimationOnEvent")
    void shouldUpdateAnimationOnEvent(String animationEvent, String expectedAnimationName, String direction, String mode) {
        if (mode == null) {
            tractor.getEvents().trigger(animationEvent, direction);
            tractor.getEvents().trigger(animationEvent, direction);
        } else {
            tractor.getEvents().trigger(animationEvent, direction, mode);
            tractor.getEvents().trigger(animationEvent, direction, mode);
        }
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
        verify(animationRenderComponent, times(1)).startAnimation(expectedAnimationName);

    }

    private static Stream<Arguments> shouldUpdateAnimationOnEvent() {
        return Stream.of(
                arguments("idle", "idle_right", "right", null),
                arguments("idle", "idle_left", "left", null),
                arguments("idle", "idle_up", "up", null),
                arguments("idle", "idle_down", "down", null),

                arguments("stopMoving", "stop_left_normal", "left", "NORMAL"),
                arguments("stopMoving", "stop_right_normal", "right", "NORMAL"),
                arguments("stopMoving", "stop_up_normal", "up", "NORMAL"),
                arguments("stopMoving", "stop_down_normal", "down", "NORMAL"),

                arguments("startMoving", "move_left_normal", "left", "NORMAL"),
                arguments("startMoving", "move_right_normal", "right", "NORMAL"),
                arguments("startMoving", "move_up_normal", "up", "NORMAL"),
                arguments("startMoving", "move_down_normal", "down", "NORMAL")

        );
    }


//
//    @ParameterizedTest(name = "{2} animation played correctly when {0} event is triggered")
//    @MethodSource("shouldUpdateAnimationOnEvent")
//    void checkAlreadyAnimated(String animationEvent, String expectedAnimationName, String direction, String mode) {
//        if (mode == null) {
//            tractor.getEvents().trigger(animationEvent, direction);
//        } else {
//            tractor.getEvents().trigger(animationEvent, direction, mode);
//        }
//        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
//    }
//
//    private static Stream<Arguments> checkAlreadyAnimated() {
//        return Stream.of(
//                arguments("idle", "idle_right", "right", null),
//                arguments("idle", "idle_left", "left", null),
//                arguments("idle", "idle_up", "up", null),
//                arguments("idle", "idle_down", "down", null),
//
//                arguments("stopMoving", "stop_left_normal", "left", "NORMAL"),
//                arguments("stopMoving", "stop_right_normal", "right", "NORMAL"),
//                arguments("stopMoving", "stop_up_normal", "up", "NORMAL"),
//                arguments("stopMoving", "stop_down_normal", "down", "NORMAL"),
//
//                arguments("startMoving", "move_left_normal", "left", "NORMAL"),
//                arguments("startMoving", "move_right_normal", "right", "NORMAL"),
//                arguments("startMoving", "move_up_normal", "up", "NORMAL"),
//                arguments("startMoving", "move_down_normal", "down", "NORMAL")
//
//        );
//    }
}