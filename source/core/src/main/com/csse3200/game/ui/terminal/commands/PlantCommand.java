package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * PlantCommand class
 */
public class PlantCommand implements Command {

    /**
     * Logger for PlantCommand class for debugging
     */
    private static final Logger logger = LoggerFactory.getLogger(PlantCommand.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean action(ArrayList<String> args) {
        if (!isValid(args)) {
            logger.debug("Invalid arguments received for 'plant' command: {}", args);
            return false;
        }
        EventHandler events = ServiceLocator.getPlantCommandService().getEvents();

        events.trigger("forceGrowthStage", args.get(0));  // Removed unnecessary cast
        return true;
    }

    /**
     * Validates the command arguments.
     * @param args command arguments
     * @return is valid
     */
    boolean isValid(ArrayList<String> args) {
        return args.size() == 1;
    }
}
