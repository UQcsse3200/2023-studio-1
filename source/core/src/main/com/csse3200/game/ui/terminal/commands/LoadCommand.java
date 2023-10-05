package com.csse3200.game.ui.terminal.commands;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.services.ServiceLocator;

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
    if (args.size() > 1) {
      logger.debug("Invalid arguments received for 'load' command: {}", args);
      return false;
    }
    if (args.size() == 1) {
      if (ServiceLocator.getSaveLoadService().validSaveFile(args.get(0))) {
        ServiceLocator.getSaveLoadService().load(args.get(0));
      }
    }
    ServiceLocator.getSaveLoadService().load();
    return true;
    }
  }
