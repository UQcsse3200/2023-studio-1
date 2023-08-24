package com.csse3200.game.ui.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(GameExtension.class)
class TimeCommandTest {

  TimeCommand command;
  ArrayList<String> args;

  @BeforeEach
  void beforeEach() {
    command = new TimeCommand();
    args = new ArrayList<>();
    ServiceLocator.clear();
  }

  @Test
  public void validTimeInput() {
    args.add("10");
    assertTrue(command.isValid(args));
  }

  @Test
  public void tooManyArgs() {
    args.add("10");
    args.add("10");
    assertFalse(command.isValid(args));
  }

  @Test
  public void invalidTimeNumber() {
    args.add("invalid");
    assertFalse(command.isValid(args));
  }

  @Test
  public void invalidTimeRange() {
    args.add("13");
    assertFalse(command.isValid(args));
  }

}