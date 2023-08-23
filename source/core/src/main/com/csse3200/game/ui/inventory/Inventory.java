package com.csse3200.game.ui.inventory;

import com.csse3200.game.components.Component;
import com.csse3200.game.ui.terminal.commands.Command;
import com.csse3200.game.ui.terminal.commands.DebugCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * State tracker for a debug terminal. Any commands to be actioned through the terminal input should
 * be added to the map of commands.
 */
public class Inventory extends Component {
  private static final Logger logger = LoggerFactory.getLogger(Inventory.class);
  private boolean isOpen = false;

  public Inventory() {
  }

  /** @return console is open */
  public boolean isOpen() {
    return isOpen;
  }

  /**
   * Toggles between the inventory being open and closed.
   */
  public void toggleIsOpen() {
    logger.debug("Toggling Inventory");
    if (isOpen) {
      this.setClosed();
    } else {
      this.setOpen();
    }
  }

  /**
   * Opens the inventory.
   */
  public void setOpen() {
    logger.debug("Opening inventory");
    isOpen = true;
  }

  /**
   * Closes the inventory.
   */
  public void setClosed() {
    logger.debug("Closing inventory");
    isOpen = false;
  }
}
