package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
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
        entity.getEvents().addListener("use", this::use);

        animator.startAnimation("default");
    }

    void use(Vector2 mousePos, Entity itemInHand) {
        if (itemInHand != null && itemInHand.getComponent(ItemComponent.class) != null) {
            String direction = getDirection(mousePos);
            String animation = String.format("%s_%s", itemInHand.getComponent(ItemComponent.class).getItemName().toLowerCase(),
                    direction);
            if (!animator.getCurrentAnimation().equals(animation)) {
                if (animator.hasAnimation(animation)) {
                    animator.startAnimation(animation);
                } else {
                    entity.getEvents().trigger("animationInteract", direction);
                }
            }
        }
    }

    /**
     * Gets the direction the animation should be player
     *
     * @param mousePos the position of the mouse
     * @return a String either up, down, left or right depending on where the mouse is in relation to the player.
     */
    private String getDirection(Vector2 mousePos) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();


        int playerXPos = width/2;
        int playerYPos = height/2;

        int xDelta = 0;

        if (playerYPos + 48 < mousePos.y) {
            return "down";
        } else if (playerYPos - 48 > mousePos.y) {
            return "up";
        }

        if (playerXPos - 24 > mousePos.x){
            xDelta -= 1;
        } else if (playerXPos + 24 < mousePos.x) {
            xDelta += 1;
        }

        if (xDelta == -1) {
            return "left";
        } else if (xDelta == 1) {
            return "right";
        }
        return "up";
    }

    /**
     * Check if the current (non-looping) animation has completed
     */
    public boolean readyToPlay() {
        if (animator.getCurrentAnimation().contains("interact") || animator.getCurrentAnimation().contains("hoe")
                || animator.getCurrentAnimation().contains("shovel") || animator.getCurrentAnimation().contains("scythe")
                || animator.getCurrentAnimation().contains("watering_can")) {
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
        if (!animator.getCurrentAnimation().equals(animation) || !readyToPlay()) {
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
        if (!animator.getCurrentAnimation().equals(animation) || !readyToPlay()) {
            animator.startAnimation(animation);
        }
    }

    /**
     * Starts the default player animation when they stop walking.
     */
    void animationWalkStop(String direction, int animationRandomizer, boolean testBypass) {

        if (!readyToPlay()) {
            return;
        }

        String animationType;
        if (animationRandomizer > 50){
            animationType = "blink";
        } else if (animationRandomizer > 25){
            animationType = "yawn";
        } else {
            animationType = "snooze";
        }

        String animation = String.format("%s_%s", animationType, direction);


        if (!animator.getCurrentAnimation().equals(animation) && animator.isFinished() || testBypass) {

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
