package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a player entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();

        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("animationWalkStart", this::animationWalkStart);
        entity.getEvents().addListener("animationWalkStop", this::animationWalkStop);
        entity.getEvents().addListener("animationInteract", this::animationInteract);

        animator.startAnimation("default");
    }

    /**
     * Starts the player walking animation in the given direction.
     *
     * @param direction "left", "right", "down" or "up"
     */
    void animationWalkStart(String direction) {
        animator.startAnimation(String.format("walk_%s", direction));
    }

    /**
     * Starts the default player animation when they stop walking.
     */
    void animationWalkStop() {
        animator.startAnimation("default");
    }

    /**
     * Play the interaction animation
     */
    void animationInteract() {
        animator.startAnimation("interact");
    }
}
