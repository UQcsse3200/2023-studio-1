package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.components.combat.CombatStatsComponent;
import com.csse3200.game.components.player.HungerComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class GODDIDNTCommand implements Command{
    private static final Logger logger = LoggerFactory.getLogger(GODDIDNTCommand.class);

    /**
     * kills the player
     * @param args command arguments
     */
    public boolean action(ArrayList<String> args) {
        if (!args.isEmpty()) {
            logger.debug("Invalid arguments received for 'god didn't' command: {}", args);
            return false;
        }
        // Don't do god stuff
        ServiceLocator.getGameArea().getPlayer().getComponent(HungerComponent.class).setHungerLevel(0);
        ServiceLocator.getGameArea().getPlayer().getComponent(CombatStatsComponent.class).setHealth(0);
        return true;
    }
}
