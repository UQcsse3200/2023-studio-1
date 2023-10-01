package com.csse3200.game.components.ship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.spy;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityType;
import com.csse3200.game.entities.factories.ShipFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;


@ExtendWith(GameExtension.class)
public class ShipAnimationControllerTest {
    private Entity ship;
    private AnimationRenderComponent animationRenderComponent;

    // code edited from PlayerAnimationControllerTest by team 2
    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerRenderService(new RenderService());
        ResourceService resourceService = new ResourceService();
        resourceService.loadTextureAtlases(new String[]{"images/ship/ship.atlas"});
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);

        animationRenderComponent = spy(
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/ship/ship.atlas", TextureAtlas.class)
                )
        );

        ShipFactory.setupShipAnimations();

        ship = new Entity(EntityType.Ship)
                .addComponent(animationRenderComponent)
                .addComponent(new ShipAnimationController());

        ship.getComponent(AnimationRenderComponent.class).scaleEntity();
        ship.create();
    }

    @ParameterizedTest(name = "{2} animation played correctly on {0} event trigger")
    @MethodSource({"shouldUpdateAnimationOnProgressUpdate"})
    void shouldUpdateAnimationOnProgressUpdate(String animationEvent, int progress, String expectedAnimationName) {
        ship.getEvents().trigger(animationEvent, progress);
        assertEquals(expectedAnimationName, animationRenderComponent.getCurrentAnimation());
    }

    private static Stream<Arguments> shouldUpdateAnimationOnProgressUpdate() {
        return Stream.of(
                // (animationEvent, movementDirection, expectedAnimationName)
                arguments("animateShipStage", -10, "default"),
                arguments("animateShipStage", 0, "ship_0"),
                arguments("animationWalkStart", 4, "ship_1"),
                arguments("animationWalkStart", 6, "ship_2"),
                arguments("animationWalkStart", 10, "ship_3"),
                arguments("animationRunStart", 12, "ship_3"),
                arguments("animationRunStart", 16, "ship_4"),
                arguments("animationRunStart", 20, "ship_5"),
                arguments("animationRunStart", 30, "ship_5")
        );
    }
}
