package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private PlayerActions actions;

  public KeyboardPlayerInputComponent() {
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
        case Keys.W:
          walkDirection.add(Vector2Utils.UP);
          triggerWalkEvent();
          triggerAnimationWalkStartEvent("up");
          return true;
        case Keys.A:
          walkDirection.add(Vector2Utils.LEFT);
          triggerWalkEvent();
          triggerAnimationWalkStartEvent("left");
          return true;
        case Keys.S:
          walkDirection.add(Vector2Utils.DOWN);
          triggerWalkEvent();
          triggerAnimationWalkStartEvent("down");
          return true;
        case Keys.D:
          walkDirection.add(Vector2Utils.RIGHT);
          triggerWalkEvent();
          triggerAnimationWalkStartEvent("right");
          return true;
        case Keys.SPACE:
          entity.getEvents().trigger("attack");
          return true;
        case Keys.F:
          triggerEnterEvent();
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
        case Keys.W:
          walkDirection.sub(Vector2Utils.UP);
          triggerWalkEvent();
          return true;
        case Keys.A:
          walkDirection.sub(Vector2Utils.LEFT);
          triggerWalkEvent();
          return true;
        case Keys.S:
          walkDirection.sub(Vector2Utils.DOWN);
          triggerWalkEvent();
          return true;
        case Keys.D:
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
      entity.getEvents().trigger("walkStop");
      triggerAnimationWalkStopEvent();
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  /**
   * Triggers a player walking animation event for the given direction.
   */
  private void triggerAnimationWalkStartEvent(String direction) {
    entity.getEvents().trigger("animationWalkStart", direction);
  }

  /**
   * Triggers a player walking animation stop event.
   */
  private void triggerAnimationWalkStopEvent() {
    entity.getEvents().trigger("animationWalkStop");
  }

  private void triggerEnterEvent() {
    entity.getEvents().trigger("enterTractor");
  }

  public void setActions(PlayerActions actions) {
    this.actions = actions;
  }

  public Vector2 getWalkDirection() {
    return walkDirection;
  }

  public void setWalkDirection(Vector2 direction) {
    this.walkDirection.set(direction);
  }
}
