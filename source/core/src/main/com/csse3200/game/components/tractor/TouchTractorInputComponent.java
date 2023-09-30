package com.csse3200.game.components.tractor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;


/**
 * Almost certain this class does nothing but is needs as when you make a InputController it makes both
 * May need to be updated later to better match the Keyboard verison.
 */
public class TouchTractorInputComponent extends InputComponent {
  /**
   * The direction the tractor will move in
   */
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  /**
   * Constructor for the tractor, sets same priority as the player's input constructor
   */
  public TouchTractorInputComponent() {
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
    switch (keycode) {
      case Input.Keys.UP:
        walkDirection.add(Vector2Utils.UP);
        triggerWalkEvent();
        triggerAnimationMoveStartEvent("up");
        return true;
      case Input.Keys.LEFT:
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        triggerAnimationMoveStartEvent("left");
        return true;
      case Input.Keys.DOWN:
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        triggerAnimationMoveStartEvent("down");
        return true;
      case Input.Keys.RIGHT:
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        triggerAnimationMoveStartEvent("right");
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
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
  /**
   * Triggers the move event for the tractor
   */
  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
      triggerAnimationMoveStopEvent();
    } else {
      entity.getEvents().trigger("walk", walkDirection);
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
}
