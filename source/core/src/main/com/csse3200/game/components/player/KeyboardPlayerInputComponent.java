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
  private final Vector2 moveDirection = Vector2.Zero.cpy();

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
    switch (keycode) {
      case Keys.W:
        moveDirection.add(Vector2Utils.UP);
        triggerMoveEvent();
        return true;
      case Keys.A:
        moveDirection.add(Vector2Utils.LEFT);
        triggerMoveEvent();
        return true;
      case Keys.S:
        moveDirection.add(Vector2Utils.DOWN);
        triggerMoveEvent();
        return true;
      case Keys.D:
        moveDirection.add(Vector2Utils.RIGHT);
        triggerMoveEvent();
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
        return true;
      case Keys.SHIFT_LEFT:
        entity.getEvents().trigger("run");
        return true;
      case Keys.I:
        //asdasdasd
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
      case Keys.W:
        moveDirection.sub(Vector2Utils.UP);
        triggerMoveEvent();
        return true;
      case Keys.A:
        moveDirection.sub(Vector2Utils.LEFT);
        triggerMoveEvent();
        return true;
      case Keys.S:
        moveDirection.sub(Vector2Utils.DOWN);
        triggerMoveEvent();
        return true;
      case Keys.D:
        moveDirection.sub(Vector2Utils.RIGHT);
        triggerMoveEvent();
        return true;
      case Keys.SHIFT_LEFT:
        entity.getEvents().trigger("runStop");
        return true;
      default:
        return false;
    }
  }

  private void triggerMoveEvent() {
    if (moveDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("moveStop");
    } else {
      entity.getEvents().trigger("move", moveDirection);
    }
  }
}