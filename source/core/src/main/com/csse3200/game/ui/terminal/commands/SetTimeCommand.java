package com.csse3200.game.ui.terminal.commands;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetTimeCommand implements Command {

  private static final Logger logger = LoggerFactory.getLogger(SetTimeCommand.class);
  private final int MIN_TIME = 0;
  private final int MAX_TIME = 23;

  /**
   * Adjusts the current game time to the specified value
   *
   * @param args command args
   * @return command was successful
   */
  @Override
  public boolean action(ArrayList<String> args) {
    if (!isValid(args)) {
      logger.debug("Invalid arguments received for 'setTime' command: {}", args);
      return false;
    }
    return true;
  }

  Boolean isValid(ArrayList<String> args) {
    if (args.size() != 1) {
      logger.debug("Only 1 argument is needed and {} were given", args.size());
      return false;
    }
    int time;
    try {
      time = Integer.parseInt(args.get(0));
    } catch (NumberFormatException e) {
      logger.debug("Argument provided was not a valid integer");
      return false;
    }
    // TODO Clarify with time-team on the duration of the days
    if (MIN_TIME > time || time > MAX_TIME) {
      logger.debug("Argument given must be between {} and {} inclusive", MIN_TIME, MAX_TIME);
      return false;
    }
    return true;
  }
}
