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

@ExtendWith(GameExtension.class)
public class TractorAnimationControllerTest {
    private Entity tractor;
    private AnimationRenderComponent animationRenderComponent;


    @BeforeEach
    void initialiseTest() {
        ServiceLocator.registerRenderService(new RenderService());
        ResourceService resourceService = new ResourceService();
        resourceService.loadTextureAtlases(new String[]{"images/tractor/tractor.atlas"});
        resourceService.loadAll();
        ServiceLocator.registerResourceService(resourceService);

        animationRenderComponent = TractorFactory.setupTractorAnimations();

        tractor = new Entity(EntityType.TRACTOR)
                .addComponent(animationRenderComponent)
                .addComponent(new TractorAnimationController());
        tractor.getComponent(AnimationRenderComponent.class).scaleEntity();
        tractor.create();
    }
}
