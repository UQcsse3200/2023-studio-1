package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class PlantCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(PlantCommand.class);
    private EventHandler events;

    @Override
    public boolean action(ArrayList<String> args) {
        if (!isValid(args)) {
            logger.debug("Invalid arguments received for 'plant' command: {}", args);
            return false;
        }
        events = ServiceLocator.getPlantCommandService().getEvents();
        events.trigger("forceGrowthStage", (String)args.get(0));
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
