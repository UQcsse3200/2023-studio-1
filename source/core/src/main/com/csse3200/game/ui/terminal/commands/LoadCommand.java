package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A command for loading the games previous state
 */
public class LoadCommand implements Command {
  private static final Logger logger = LoggerFactory.getLogger(LoadCommand.class);

  /**
   * loads game through the SaveLoad service
   * @param args command arguments
   */
  public boolean action(ArrayList<String> args) {
    if (!args.isEmpty()) {
      logger.debug("Invalid arguments received for 'load' command: {}", args);
      return false;
    }
    ServiceLocator.getSaveLoadService().load();
    return true;
    }
  }
