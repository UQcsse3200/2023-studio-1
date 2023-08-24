package com.csse3200.game.components.tractor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

public class KeyboardTractorInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private TractorActions actions;

    public KeyboardTractorInputComponent() {
        super(5);
    }

    /**
     * Triggers tractor events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        if (!actions.isMuted()) {
            switch (keycode) {
                case Input.Keys.W:
                    walkDirection.add(Vector2Utils.UP);
                    triggerMoveEvent();
                    triggerAnimationMoveStartEvent("up");
                    return true;
                case Input.Keys.A:
                    walkDirection.add(Vector2Utils.LEFT);
                    triggerMoveEvent();
                    triggerAnimationMoveStartEvent("left");
                    return true;
                case Input.Keys.S:
                    walkDirection.add(Vector2Utils.DOWN);
                    triggerMoveEvent();
                    triggerAnimationMoveStartEvent("down");
                    return true;
                case Input.Keys.D:
                    walkDirection.add(Vector2Utils.RIGHT);
                    triggerMoveEvent();
                    triggerAnimationMoveStartEvent("right");
                    return true;
                case Input.Keys.F:
                    walkDirection.sub(Vector2.Zero);
                    triggerExitEvent();
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * Triggers tractor events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        if (!actions.isMuted()) {
            switch (keycode) {
                case Input.Keys.W:
                    walkDirection.sub(Vector2Utils.UP);
                    triggerMoveEvent();
                    return true;
                case Input.Keys.A:
                    walkDirection.sub(Vector2Utils.LEFT);
                    triggerMoveEvent();
                    return true;
                case Input.Keys.S:
                    walkDirection.sub(Vector2Utils.DOWN);
                    triggerMoveEvent();
                    return true;
                case Input.Keys.D:
                    walkDirection.sub(Vector2Utils.RIGHT);
                    triggerMoveEvent();
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * Triggers the move event for the tractor
     */
    private void triggerMoveEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("moveStop");
            triggerAnimationMoveStopEvent();
        } else {
            entity.getEvents().trigger("move", walkDirection);
        }
    }

    /**
     * Triggers the tractors walking animation event for the given direction.
     */
    private void triggerAnimationMoveStartEvent(String direction) {
        entity.getEvents().trigger("startMoving", direction, "Tool");
    }

    /**
     * Triggers the tractor walking animation stop event.
     */
    private void triggerAnimationMoveStopEvent() {
        entity.getEvents().trigger("stopMoving", "Tool");
    }

    /**
     * Triggers the event to leave the tractor
     */
    private void triggerExitEvent() {
        entity.getEvents().trigger("exitTractor");
    }

    /**
     * Should be used to set the action variable to the tractor's TractorAction Component
     *
     * @param actions the TractorActions component from the tractor entity
     */
    public void setActions(TractorActions actions) {
        this.actions = actions;
    }

    /**
     * Returns the direction the the tractor is moving.
     * Called walkDirection for consistency with the player
     *
     * @return the direction the tractor is moving as a Vector
     */
    public Vector2 getWalkDirection() {
        return walkDirection;
    }

    /**
     * Sets the movement direction of the tractor to a given direction.
     * Used to align player and tractor movement when getting in and out.
     *
     * @param direction - The direction to set the walkDirection of the tractor.
     */
    public void setWalkDirection(Vector2 direction) {
        this.walkDirection.set(direction);
    }
}
