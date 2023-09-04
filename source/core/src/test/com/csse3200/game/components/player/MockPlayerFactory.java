package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create a mock player entity for testing.
 * Only includes necessary components for testing.
 *
 * <p>Predefined player properties are loaded from a config stored as a json file and should have
 * the properties stores in 'PlayerConfig'.
 */
public class MockPlayerFactory {

    /**
     * Create a mock player entity.
     *
     * @return entity
     */
    public static Entity createPlayer() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForPlayer();

        AnimationRenderComponent animator = setupPlayerAnimator();

        Entity player =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PlayerActions())
                        .addComponent(inputComponent)
                        .addComponent(animator)
                        .addComponent(new PlayerAnimationController());

        player.getComponent(AnimationRenderComponent.class).scaleEntity();
        return player;
    }

    /**
     * Creates the player AnimationRenderComponent and registers the
     * player animations.
     *
     * @return the player animator
     */
    private static AnimationRenderComponent setupPlayerAnimator() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/player.atlas", TextureAtlas.class)
                );
        animator.addAnimation("walk_left", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk_right", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk_down", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("walk_up", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_left", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_right", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_down", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("run_up", 0.05f, Animation.PlayMode.LOOP);
        animator.addAnimation("default", 0.1f, Animation.PlayMode.LOOP);

        return animator;
    }

    private MockPlayerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
