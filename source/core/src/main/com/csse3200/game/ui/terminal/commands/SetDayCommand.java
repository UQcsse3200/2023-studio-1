package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SetDayCommand implements Command {

  private static final Logger logger = LoggerFactory.getLogger(SetDayCommand.class);

  /**
   * Adjusts the current game time to the specified value
   *
   * @param args command args
   * @return command was successful
   */
  @Override
  public boolean action(ArrayList<String> args) {
    if (!isValid(args)) {
      logger.debug("Invalid arguments received for 'setDay' command: {}", args);
      return false;
    }
    ServiceLocator.getTimeService().setDay(Integer.parseInt(args.get(0)));
    return true;
  }

  Boolean isValid(ArrayList<String> args) {
    if (args.size() != 1) {
      logger.debug("Only 1 argument is needed and {} were given", args.size());
      return false;
    }
    int day;
    try {
      day = Integer.parseInt(args.get(0));
    } catch (NumberFormatException e) {
      logger.debug("Argument provided was not a valid integer");
      return false;
    }
	  return day >= 0;
  }
}
