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
      case Keys.NUM_0:
        entity.getComponent(InventoryComponent.class).setHeldItem(0);
        return true;
      case Keys.NUM_1:
        entity.getComponent(InventoryComponent.class).setHeldItem(1);
        return true;
      case Keys.NUM_2:
        entity.getComponent(InventoryComponent.class).setHeldItem(2);
        return true;
      case Keys.NUM_3:
        entity.getComponent(InventoryComponent.class).setHeldItem(3);
        return true;
      case Keys.NUM_4:
        entity.getComponent(InventoryComponent.class).setHeldItem(4);
        return true;
      case Keys.NUM_5:
        entity.getComponent(InventoryComponent.class).setHeldItem(5);
        return true;
      case Keys.NUM_6:
        entity.getComponent(InventoryComponent.class).setHeldItem(6);
        return true;
      case Keys.NUM_7:
        entity.getComponent(InventoryComponent.class).setHeldItem(7);
        return true;
      case Keys.NUM_8:
        entity.getComponent(InventoryComponent.class).setHeldItem(8);
        return true;
      case Keys.NUM_9:
        entity.getComponent(InventoryComponent.class).setHeldItem(9);
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
}
