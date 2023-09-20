package com.csse3200.game.ui.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.TimeService;


@ExtendWith(GameExtension.class)
class SetTimeCommandTest {

  SetTimeCommand command;
  ArrayList<String> args;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.clear();
    command = new SetTimeCommand();
    args = new ArrayList<>();
  }

  @Test
  public void validTimeInput() {
    String timeArg = "10";
    args.add(timeArg);
    ServiceLocator.registerTimeSource(mock(GameTime.class));
    TimeService timeService = mock(TimeService.class);
    ServiceLocator.registerTimeService(timeService);
    assertTrue(command.isValid(args));
    command.action(args);
    verify(timeService, times(1)).setHour(Integer.parseInt(timeArg));
  }

  @Test
  public void tooManyArgs() {
    args.add("10");
    args.add("10");
    assertFalse(command.isValid(args));
    assertFalse(command.action(args));
  }

  @Test
  public void invalidTimeNumber() {
    args.add("invalid");
    assertFalse(command.isValid(args));
  }

  @Test
  public void invalidTimeRange() {
    args.add("24");
    assertFalse(command.isValid(args));
  }

}