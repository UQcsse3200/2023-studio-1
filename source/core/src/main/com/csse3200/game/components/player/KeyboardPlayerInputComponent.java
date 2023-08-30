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
  private PlayerActions actions;
  private final int hotKeyOffset = 6;

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
        case Keys.E: // Potentially also interact button later.
          entity.getEvents().trigger("interact");
          return true;
        case Keys.I:
          // inventory tings
          entity.getEvents().trigger("toggleInventory");
          return true;
        case Keys.F:
          triggerEnterEvent();
          return true;
        case Keys.NUM_0: case Keys.NUM_1: case Keys.NUM_2:
        case Keys.NUM_3: case Keys.NUM_4: case Keys.NUM_5:
        case Keys.NUM_6: case Keys.NUM_7: case Keys.NUM_8:
        case Keys.NUM_9:
          triggerHotKeySelection(keycode);
          return true;
        default:
          return false;
      }
    }
    return false;
  }

  /** @see InputProcessor#touchUp(int, int, int, int) */
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (!actions.isMuted()) {
      Vector2 mousePos = new Vector2(screenX, screenY);
      entity.getEvents().trigger("use", entity.getPosition(), mousePos, entity.getComponent(InventoryComponent.class).getHeldItem());
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
    return false;
  }

  private void triggerMoveEvent() {
    if (moveDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("moveStop");
    } else {
      entity.getEvents().trigger("move", moveDirection);
    }
  }

  private void triggerEnterEvent() {
    entity.getEvents().trigger("enterTractor");
  }

  public void setActions(PlayerActions actions) {
    this.actions = actions;
  }

  public Vector2 getWalkDirection() {
    return moveDirection;
  }

  public void setWalkDirection(Vector2 direction) {
    this.moveDirection.set(direction);
  }
   /**
   * Sets the players current held item to that in the provided index of the inventory
   *
   * @param index of the item the user wants to be holding
   */
   public void triggerHotKeySelection(int index) {
     index -= 8;
     if (index < 0) {
       index = 9;
     }
     entity.getEvents().trigger("hotkeySelection", index);
   }
}
