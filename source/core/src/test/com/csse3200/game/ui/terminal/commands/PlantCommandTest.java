package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

/**
 * Tests for PlantCommand class
 */
@ExtendWith({})
public class PlantCommandTest {
    PlantCommand plantCommand;

    @BeforeEach
    public void setUp() {
        plantCommand = new PlantCommand();
    }

    /**
     * test for action method with valid arguments
     */
    @Test
    public void testActionWithValidArgs() {
        ArrayList<String> args = new ArrayList<String>();
        args.add("");

        Assertions.assertTrue(plantCommand.action(args));
    }

    /**
     * test for action method with invalid arguments
     */
    @Test
    public void testActionWithInvalidArgs() {
        ArrayList<String> args = new ArrayList<String>();
        args.add("");
        args.add("");
        boolean result = plantCommand.action(args);
        Assertions.assertFalse(result);
    }

    /**
     * Test for isValid with valid arguments
     */
    @Test
    public void testIsValidWithValidArgs() {
        ArrayList<String> args = new ArrayList<String>();
        args.add("");

        Assertions.assertTrue(plantCommand.isValid(args));
    }

    /**
     * Test for isValid with invalid arguments
     */
    @Test
    public void testIsValidWithInvalidArgs() {
        ArrayList<String> args = new ArrayList<String>();
        args.add("");
        args.add("");

        Assertions.assertFalse(plantCommand.isValid(args));
    }
}
