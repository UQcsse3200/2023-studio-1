package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.GameMap;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.items.ItemComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

/**
 * This class listens to events relevant to a player entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;
    String animationPrev;

    Entity fishingRod;
    String fishingDirection;

    public enum events {
        ANIMATION_WALK_START,
        ANIMATION_RUN_START,
        ANIMATION_WALK_STOP,
        ANIMATION_INTERACT
    }

    @Override
    public void create() {
        super.create();

        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(events.ANIMATION_WALK_START.name(), this::animationWalkStart);
        entity.getEvents().addListener(events.ANIMATION_RUN_START.name(), this::animationRunStart);
        entity.getEvents().addListener(events.ANIMATION_WALK_STOP.name(), this::animationWalkStop);
        entity.getEvents().addListener(events.ANIMATION_INTERACT.name(), this::animationInteract);
        entity.getEvents().addListener(PlayerActions.events.FISH_CAUGHT.name(), this::stopFishing);
        entity.getEvents().addListener(PlayerActions.events.CAST_FISHING_RODS.name(), this::castFishingRod);
        entity.getEvents().addListener(PlayerActions.events.USE.name(), this::use);

        animator.startAnimation("default");
    }

    @Override
    public void update() {
        fishingRod.setCenterPosition(entity.getCenterPosition());
        if (fishingDirection != null && fishingRod.getComponent(AnimationRenderComponent.class).isFinished()) {
            fishingRod.getComponent(AnimationRenderComponent.class).startAnimation(String.format("fishing_%s", fishingDirection));
            fishingDirection = null;
        }
    }

    public void addFishingRodAnimatorEntity(Entity fishingRod) {
        this.fishingRod = fishingRod;
        this.fishingRod.create();
    }

    void stopFishing() {
        fishingRod.getComponent(AnimationRenderComponent.class).stopAnimation();
        animator.stopAnimation();
        fishingDirection = null;
    }

    void castFishingRod(Vector2 mousePos) {
        String direction = getDirection(mousePos);
        fishingRod.getComponent(AnimationRenderComponent.class).startAnimation(String.format("cast_%s", direction));
        animator.startAnimation(String.format("fishing_%s", direction));
        fishingDirection = direction;
    }

    void use(Vector2 mousePos, Entity itemInHand) {
        if (itemInHand != null && itemInHand.getComponent(ItemComponent.class) != null) {
            String direction = getDirection(mousePos);
            String animation = String.format("%s_%s", itemInHand.getComponent(ItemComponent.class).getItemName().toLowerCase(),
                    direction);
            if (!Objects.equals(animator.getCurrentAnimation(), animation)) {
                if (animator.hasAnimation(animation)) {
                    animator.startAnimation(animation);
                } else {
                    entity.getEvents().trigger(events.ANIMATION_INTERACT.name(), direction);
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
        GameMap map = ServiceLocator.getGameArea().getMap();
        Vector2 mouseWorldPos = ServiceLocator.getCameraComponent().screenPositionToWorldPosition(mousePos);
        Vector2 adjustedPosition = new Vector2(
                map.tileCoordinatesToVector(map.vectorToTileCoordinates(new Vector2(mouseWorldPos.x, mouseWorldPos.y))));

        Vector2 playerPosCenter = ServiceLocator.getGameArea().getPlayer().getCenterPosition();
        playerPosCenter.add(0, -1.0f); // Player entity sprite's feet are located -1.0f below the centre of the entity. ty Hunter

        playerPosCenter = map.tileCoordinatesToVector(map.vectorToTileCoordinates(playerPosCenter));

        if (adjustedPosition.y - 0.5> playerPosCenter.y) {
            return "up";
        } else if (adjustedPosition.y  + 0.5 < playerPosCenter.y) {
            return "down";
        }else if (adjustedPosition.x - 0.5 > playerPosCenter.x) {
            return "right";
        } else if (adjustedPosition.x + 0.5 < playerPosCenter.x) {
            return "left";
        }
        // Default
        return "down";
    }

    /**
     * Check if the current (non-looping) animation has completed
     */
    public boolean readyToPlay() {
        if (animator.getCurrentAnimation() == null) {
            return true;
        } else if (animator.getCurrentAnimation().contains("interact") || animator.getCurrentAnimation().contains("hoe")
                || animator.getCurrentAnimation().contains("shovel") || animator.getCurrentAnimation().contains("scythe")
                || animator.getCurrentAnimation().contains("watering_can")) {
            return animator.isFinished();
        } else return !animator.getCurrentAnimation().contains("fishing");
    }

    /**
     * Starts the player walking animation in the given direction.
     *
     * @param direction "left", "right", "down" or "up"
     */
    void animationWalkStart(String direction) {
        String animation = String.format("walk_%s", direction);
        if (!Objects.equals(animator.getCurrentAnimation(), animation) || !readyToPlay()) {
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
        if (!Objects.equals(animator.getCurrentAnimation(), animation) || !readyToPlay()) {
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


	    if (animator.getCurrentAnimation() == null
			    || (!animator.getCurrentAnimation().equals(animation) && animator.isFinished())
			    || testBypass) {

            animator.startAnimation(animation);
            animationPrev = animation;
        }


    }

    /**
     * Play the interaction animation
     */
    void animationInteract(String direction) {
        if (!readyToPlay()) {
            return;
        }
        // Get the current animation name to get the facing direction
        String animation = String.format("interact_%s", direction);
        if (!Objects.equals(animator.getCurrentAnimation(), animation)) {
            animator.startAnimation(animation);
        }
    }
}
