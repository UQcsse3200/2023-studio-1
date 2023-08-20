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
     * Triggers player events on specific keycodes.
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
                    triggerWalkEvent();
                    triggerAnimationMoveStartEvent("up");
                    return true;
                case Input.Keys.A:
                    walkDirection.add(Vector2Utils.LEFT);
                    triggerWalkEvent();
                    triggerAnimationMoveStartEvent("left");
                    return true;
                case Input.Keys.S:
                    walkDirection.add(Vector2Utils.DOWN);
                    triggerWalkEvent();
                    triggerAnimationMoveStartEvent("down");
                    return true;
                case Input.Keys.D:
                    walkDirection.add(Vector2Utils.RIGHT);
                    triggerWalkEvent();
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
     * Triggers player events on specific keycodes.
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
                    triggerWalkEvent();
                    return true;
                case Input.Keys.A:
                    walkDirection.sub(Vector2Utils.LEFT);
                    triggerWalkEvent();
                    return true;
                case Input.Keys.S:
                    walkDirection.sub(Vector2Utils.DOWN);
                    triggerWalkEvent();
                    return true;
                case Input.Keys.D:
                    walkDirection.sub(Vector2Utils.RIGHT);
                    triggerWalkEvent();
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    private void triggerWalkEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("moveStop");
            triggerAnimationMoveStopEvent();
        } else {
            entity.getEvents().trigger("move", walkDirection);
        }
    }

    /**
     * Triggers a player walking animation event for the given direction.
     */
    private void triggerAnimationMoveStartEvent(String direction) {
        entity.getEvents().trigger("startMoving", direction, "Tool");
    }

    /**
     * Triggers a player walking animation stop event.
     */
    private void triggerAnimationMoveStopEvent() {
        entity.getEvents().trigger("stopMoving", "Tool");
    }


    private void triggerExitEvent() {
        entity.getEvents().trigger("exitTractor");
    }

    public void setActions(TractorActions actions) {
        this.actions = actions;
    }
}
