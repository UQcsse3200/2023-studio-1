package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.player.HungerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class GODDIDCommand implements Command{
    private static final Logger logger = LoggerFactory.getLogger(GODDIDCommand.class);

    /**
     * loads game through the SaveLoad service
     * @param args command arguments
     */
    public boolean action(ArrayList<String> args) {
        if (args.size() != 0) {
            logger.debug("Invalid arguments received for 'god' command: {}", args);
            return false;
        }
        // Do god stuff
        ServiceLocator.god = !ServiceLocator.god;
        ServiceLocator.getGameArea().getPlayer().getComponent(HungerComponent.class).setHungerLevel(100);
        ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class).setHealth(100);
        return true;
    }
}
