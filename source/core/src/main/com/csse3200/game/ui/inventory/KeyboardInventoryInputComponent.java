package com.csse3200.game.ui.inventory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.ui.terminal.Terminal;

/**
 * Input handler for the debug terminal for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 *
 * <p>The debug terminal can be opened and closed by pressing 'F1' and a message can be entered via
 * the keyboard.
 */
public class KeyboardInventoryInputComponent extends InputComponent {
  private static final int TOGGLE_OPEN_KEY = Input.Keys.E;
  private Inventory inventory;

  public KeyboardInventoryInputComponent() {
    super(10);
  }

  public KeyboardInventoryInputComponent(Inventory inventory) {
    this();
    this.inventory = inventory;
  }

  @Override
  public void create() {
    super.create();
    inventory = entity.getComponent(Inventory.class);
  }

  /**
   * If the toggle key is pressed, the terminal will open / close.
   *
   * <p>Otherwise, handles input if the terminal is open. This is because keyDown events are
   * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
   * trigger any other input handlers.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    // handle open and close terminal
    if (keycode == TOGGLE_OPEN_KEY) {
      inventory.toggleIsOpen();
      return true;
    }

    return inventory.isOpen();
  }
}
