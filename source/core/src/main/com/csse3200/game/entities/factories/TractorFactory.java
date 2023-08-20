package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.tractor.KeyboardTractorInputComponent;
import com.csse3200.game.components.tractor.TractorActions;
import com.csse3200.game.components.tractor.TractorAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class TractorFactory {

    public static Entity createTractor(Entity player) {

        AnimationRenderComponent animator = setupTractorAnimations();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTractor();

        Entity tractor =
            new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new TractorAnimationController())
                .addComponent(new ColliderComponent())
                .addComponent(animator)
                .addComponent(inputComponent)
                .addComponent(new TractorActions());


        tractor.getComponent(AnimationRenderComponent.class).scaleEntity();
        tractor.getComponent(TractorActions.class).setPlayer(player);
        tractor.getComponent(KeyboardTractorInputComponent.class).setActions(tractor.getComponent(TractorActions.class));
        tractor.scaleWidth(3f);
        tractor.scaleHeight(3f);
        return tractor;
    }

    private static AnimationRenderComponent setupTractorAnimations() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/tractor.atlas", TextureAtlas.class)
                );
        animator.addAnimation("Tractor_Move_left_Tool", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("Tractor_Move_right_Tool", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("Tractor_Move_down_Tool", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("Tractor_Move_up_Tool", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("Tractor_Idle_Tool", 0.1f, Animation.PlayMode.LOOP);

        return animator;
    }
}
