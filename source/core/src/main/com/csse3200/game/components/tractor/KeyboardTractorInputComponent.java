package com.csse3200.game.components.tractor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.sound.EffectSoundFile;
import com.csse3200.game.services.sound.InvalidSoundFileException;
import com.csse3200.game.utils.math.Vector2Utils;

public class KeyboardTractorInputComponent extends InputComponent {
  /**
   * The direction the tractor will move in
   */
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  /**
   * Constructor for the tractor, sets same priority as the player's input constructor
   */
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
    if (!entity.getComponent(TractorActions.class).isMuted()) {
      switch (keycode) {
        case Input.Keys.W:
          walkDirection.add(Vector2Utils.UP);
          triggerMoveEvent();
          return true;
        case Input.Keys.A:
          walkDirection.add(Vector2Utils.LEFT);
          triggerMoveEvent();
          return true;
        case Input.Keys.S:
          walkDirection.add(Vector2Utils.DOWN);
          triggerMoveEvent();
          return true;
        case Input.Keys.D:
          walkDirection.add(Vector2Utils.RIGHT);
          triggerMoveEvent();
          return true;
        case Input.Keys.F:
          walkDirection.sub(Vector2.Zero);
          triggerExitEvent();
          return true;
        case Input.Keys.SPACE:
          try {
            ServiceLocator.getSoundService().getEffectsMusicService().play(EffectSoundFile.TRACTOR_HONK);
          } catch (InvalidSoundFileException e) {
            throw new RuntimeException(e);
          }
          return true;
        case Input.Keys.T:
          entity.getEvents().trigger("toggleConeLight");
          return true;
        case Input.Keys.NUM_1:
          entity.getComponent(TractorActions.class).setMode(TractorMode.NORMAL);
          return true;
        case Input.Keys.NUM_2:
          entity.getComponent(TractorActions.class).setMode(TractorMode.TILLING);
          return true;
        case Input.Keys.NUM_3:
          entity.getComponent(TractorActions.class).setMode(TractorMode.HARVESTING);
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
    if (!entity.getComponent(TractorActions.class).isMuted()) {
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
    } else {
      entity.getEvents().trigger("move", walkDirection);
    }
  }

  /**
   * Triggers the event to leave the tractor
   */
  private void triggerExitEvent() {
    entity.getEvents().trigger("exitTractor");
  }

  /**
   * Returns the direction the tractor is moving.
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
