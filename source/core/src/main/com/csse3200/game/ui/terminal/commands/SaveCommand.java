package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A command for saving the games current state
 */
public class SaveCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(SaveCommand.class);

  /**
   * saves game through the SaveLoad service
   * @param args command arguments
   */
  public boolean action(ArrayList<String> args) {
    if (!args.isEmpty()) {
      logger.debug("Invalid arguments received for 'save' command: {}", args);
      return false;
    }
    ServiceLocator.getSaveLoadService().save();
    return true;
    }
  }
