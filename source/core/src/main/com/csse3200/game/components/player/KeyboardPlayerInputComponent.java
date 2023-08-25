package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.components.player.InventoryComponent;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
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
      case Keys.NUM:
        if (keycode == Keys.NUM_0) {
          handleHotKeySelection(0);
        } else {
          handleHotKeySelection(keycode - hotKeyOffset);
        }
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

  /**
   * Sets the players current held item to that in the provided index of the inventory
   *
   * @param index of the item the user wants to be holding
   */
  private void handleHotKeySelection(int index) {
    entity.getComponent(InventoryComponent.class).setHeldItem(index);
  }
}
