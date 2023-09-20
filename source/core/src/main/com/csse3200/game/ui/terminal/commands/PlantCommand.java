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
        switch (args.get(0)) {
            case "seedling" ->events.trigger("forceSeedling");
            case "sprout" -> events.trigger("forceSprout");
            case "juvenile" -> events.trigger("forceJuvenile");
            case "adult" -> events.trigger("forceAdult");
            case "decay" -> events.trigger("forceDecay");
            case "dead" -> events.trigger("forceDead");
            default -> {
                return false;
            }
        }
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
