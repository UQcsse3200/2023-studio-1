package com.csse3200.game.ui.inventory;

import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.ui.terminal.Terminal;

/**
 * Input handler for the debug terminal for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 *
 * <p>The debug terminal can be opened and closed by scrolling vertically and a message can be entered via
 * the keyboard.
 */
public class TouchInventoryInputComponent extends InputComponent {
  private Inventory inventory;

  public TouchInventoryInputComponent() {
    super(10);
  }

  public TouchInventoryInputComponent(Inventory inventory) {
    this();
    this.inventory = inventory;
  }


  @Override
  public void create() {
    super.create();
    inventory = entity.getComponent(Inventory.class);
  }

  /**
   * Handles input if the terminal is open. This is because keyDown events are
   * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
   * trigger any other input handlers.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    return inventory.isOpen();
  }
}
