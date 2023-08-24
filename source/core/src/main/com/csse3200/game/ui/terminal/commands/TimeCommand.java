package com.csse3200.game.ui.terminal.commands;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeCommand implements Command{
  private static final Logger logger = LoggerFactory.getLogger(TimeCommand.class);

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
    return false;
  }

  Boolean isValid(ArrayList<String> args) {
    return args.size() == 1;
    // TODO check whether time given is valid time value
    // TODO check whether time number value is within correct range
  }
}
