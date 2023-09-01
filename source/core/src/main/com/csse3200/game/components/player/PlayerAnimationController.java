package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a player entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;
    String animationPrev;
    @Override
    public void create() {
        super.create();

        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("animationWalkStart", this::animationWalkStart);
        entity.getEvents().addListener("animationRunStart", this::animationRunStart);
        entity.getEvents().addListener("animationWalkStop", this::animationWalkStop);
        entity.getEvents().addListener("animationInteract", this::animationInteract);

        animator.startAnimation("default");
    }
    
    /**
     * Check if the current (non-looping) animation has completed
     */
    boolean readyToPlay() {
        if (animator.getCurrentAnimation().contains("interact")) {
            return animator.isFinished();
        }
        return true;
    }

    /**
     * Starts the player walking animation in the given direction.
     *
     * @param direction "left", "right", "down" or "up"
     */
    void animationWalkStart(String direction) {
        String animation = String.format("walk_%s", direction);
        if (!animator.getCurrentAnimation().equals(animation)) {
            animator.startAnimation(animation);
        }
    }

    /**
     * Starts the player running animation in the given direction.
     *
     * @param direction "left", "right", "down" or "up"
     */
    void animationRunStart(String direction) {
        String animation = String.format("run_%s", direction);
        if (!animator.getCurrentAnimation().equals(animation)) {
            animator.startAnimation(animation);
        }
    }

    /**
     * Starts the default player animation when they stop walking.
     */
    void animationWalkStop(String direction, int AnimationRandomizer) {

        if (!readyToPlay()) {
            return;
        }

        String animationType;
        if (AnimationRandomizer > 50){
            animationType = "blink";
        } else if (AnimationRandomizer > 25){
            animationType = "yawn";
        } else {
            animationType = "sleep";
        }

        String animation = String.format("idle_%s_%s", direction, animationType);


        if (!animator.getCurrentAnimation().equals(animation) && animator.isFinished()) {

            animator.startAnimation(animation);
            animationPrev = animation;
        }


    }

    /**
     * Play the interaction animation
     */
    void animationInteract(String direction) {
        // Get the current animation name to get the facing direction
        String animation = String.format("interact_%s", direction);
        if (!animator.getCurrentAnimation().equals(animation)) {
            animator.startAnimation(animation);
        }
    }
}
